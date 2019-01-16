package com.example.szafa.myway;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class OptionsActivity extends AppCompatActivity {

    private Options options;

    public static final String OPTIONS_ID = "com.example.szafa.OPTIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinnerMean = findViewById(R.id.spinnerMean);
        Switch switchStartCurrent = findViewById(R.id.switchStartCurrent);
        Switch switchStopCurrent = findViewById(R.id.switchStopCurrent);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transport_means, R.layout.support_simple_spinner_dropdown_item);
        spinnerMean.setAdapter(adapter);

        Intent intent = getIntent();
        options = (Options) intent.getSerializableExtra(OPTIONS_ID);
        Hashtable<TransportMean, String> meanToStrDict = new Hashtable<>();
        meanToStrDict.put(TransportMean.Car, "Car");
        meanToStrDict.put(TransportMean.Bike, "Bike");
        meanToStrDict.put(TransportMean.Foot, "Foot");

        final Hashtable<String, TransportMean> strToMeanDict = new Hashtable<>();
        for(Map.Entry<TransportMean, String> entry : meanToStrDict.entrySet()){
            strToMeanDict.put(entry.getValue(), entry.getKey());
        }

        String meanStr = meanToStrDict.get(options.getTransportMean());
        spinnerMean.setSelection(adapter.getPosition(meanStr));
        switchStartCurrent.setChecked(options.isStartCurrent());
        switchStopCurrent.setChecked(options.isStopCurrent());

        spinnerMean.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mean = (String) adapter.getItem(position);
                options.setTransportMean(strToMeanDict.get(mean));
                updateOptions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        switchStartCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                options.setStartCurrent(isChecked);
                updateOptions();
            }
        });
        switchStopCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                options.setStopCurrent(isChecked);
                updateOptions();
            }
        });
    }

    private void updateOptions(){
        Intent intent = new Intent();
        intent.putExtra(OPTIONS_ID, options);
        setResult(RESULT_OK, intent);
    }

}
