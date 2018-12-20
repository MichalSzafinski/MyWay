package com.example.szafa.myway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ClientListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        RecyclerView clientView = (RecyclerView) findViewById(R.id.clientView);
        clientView.setAdapter(new ClientAdapter(new String[]{"Adam Adamiak", "Bartosz Bereszyński", "Harry Potter"}));
        clientView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onButtonAddClientClick(View view){
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

    public void onButtonClientClick(View view){
        finish();
    }
}
