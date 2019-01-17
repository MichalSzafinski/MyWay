package com.example.szafa.myway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ClientListActivity extends AppCompatActivity {

    public static final String NEW_LIST_ID = "com.example.szafa.NEW_CLIENTS";

    private ClientsDbHelper db;
    private ClientAdapter clientAdapter;
    private ArrayList<Client> clientsToVisit;
    private ArrayList<Client> allClients;
    private EditText txtFind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        RecyclerView clientView = (RecyclerView) findViewById(R.id.clientView);
        db = ClientsDbHelper.getDbHelper();
        clientView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        clientsToVisit = (ArrayList<Client>) intent.getSerializableExtra(RouteLocationsActivity.LIST_ID);
        clientAdapter = new ClientAdapter(new ArrayList<Client>(), clientsToVisit, this);
        clientView.setAdapter(clientAdapter);
        txtFind = findViewById(R.id.textFind);
        txtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterClients();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClients();
        filterClients();
    }

    public void onButtonAddClientClick(View view){
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

    public void returnNewClient(Client client){
        clientsToVisit.add(client);
        Intent intent = new Intent();
        intent.putExtra(NEW_LIST_ID, clientsToVisit);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void filterClients(){
        ArrayList<Client> filtered = new ArrayList<>();
        String key = txtFind.getText().toString();
        for(Client c : allClients){
            if(c.getName().toLowerCase().contains(key.toLowerCase())){
                filtered.add(c);
            }
        }
        clientAdapter.setData(filtered);
        clientAdapter.notifyDataSetChanged();
    }

    public void loadClients(){
        allClients = db.getClients();
    }
}
