package com.example.szafa.myway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RouteLocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_locations);
    }

    public void onButtonAddLocationClick(View view){
        Intent intent = new Intent(this, ClientListActivity.class);
        startActivity(intent);
    }
}
