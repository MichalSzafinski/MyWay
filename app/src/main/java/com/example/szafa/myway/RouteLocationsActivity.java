package com.example.szafa.myway;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RouteLocationsActivity extends AppCompatActivity {

    public static final String LIST_ID = "com.example.szafa.CLIENTS_TO_VISIT";
    private static final int ADD_LOCATION_REQUEST = 1;

    private ArrayList<Client> clientsToVisit;
    private LocationAdapter locationAdapter;

    public void SetClientsToVisit(ArrayList<Client> l)
    {
        clientsToVisit = l;
        Intent intent = new Intent();
        intent.putExtra(LIST_ID, clientsToVisit);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_locations);
        RecyclerView locationsView = (RecyclerView) findViewById(R.id.locationsView);
        Intent intent = getIntent();
        clientsToVisit = (ArrayList<Client>) intent.getSerializableExtra(LIST_ID);
        locationAdapter = new LocationAdapter(clientsToVisit, this);
        locationsView.setAdapter(locationAdapter);
        locationsView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationAdapter.notifyDataSetChanged();
    }

    public void onButtonAddLocationClick(View view){
        Intent intent = new Intent(this, ClientListActivity.class);
        intent.putExtra(LIST_ID, clientsToVisit);
        startActivityForResult(intent, ADD_LOCATION_REQUEST);
    }
    public void onButtonOkClick(View view){

        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_LOCATION_REQUEST){
            if(resultCode == RESULT_OK){
                clientsToVisit = (ArrayList<Client>) data.getSerializableExtra(ClientListActivity.NEW_LIST_ID);
                locationAdapter.setData(clientsToVisit);
                locationAdapter.notifyDataSetChanged();

                Intent intent = new Intent();
                intent.putExtra(LIST_ID, clientsToVisit);
                setResult(RESULT_OK, intent);
            }
        }
    }
}
