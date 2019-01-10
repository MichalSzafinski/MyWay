package com.example.szafa.myway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
    }

    public void onButtonSaveClick(View view){
        ClientsDbHelper db = ClientsDbHelper.getDbHelper();
        String name = ((EditText)findViewById(R.id.txtName)).getText().toString();
        String address = ((EditText)findViewById(R.id.txtAddress)).getText().toString();
        String phone = ((EditText)findViewById(R.id.txtPhone)).getText().toString();
        db.addClient(new Client(name, address, phone));
        finish();
    }
}
