package com.example.szafa.myway;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class ClientsDbHelper extends SQLiteOpenHelper {

    public static class ClientEntry implements BaseColumns {
        public static final String TABLE_NAME = "Clients";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_ADDRESS = "Address";
        public static final String COLUMN_NAME_PHONE = "Phone";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Clients.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ClientEntry.TABLE_NAME + " (" +
                    ClientEntry._ID + " INTEGER PRIMARY KEY," +
                    ClientEntry.COLUMN_NAME_ADDRESS + " TEXT," +
                    ClientEntry.COLUMN_NAME_PHONE + " TEXT," +
                    ClientEntry.COLUMN_NAME_NAME + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ClientEntry.TABLE_NAME;

    private static ClientsDbHelper dbHelper = null;

    public static ClientsDbHelper initDbHelper(Context context){
        dbHelper = new ClientsDbHelper(context);
        return dbHelper;
    }

    private ClientsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static ClientsDbHelper getDbHelper() {
        return dbHelper;
    }

    public void addClient(Client client){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClientEntry.COLUMN_NAME_ADDRESS, client.getAddress());
        values.put(ClientEntry.COLUMN_NAME_NAME, client.getName());
        values.put(ClientEntry.COLUMN_NAME_PHONE, client.getPhone());
        long id = db.insert(ClientEntry.TABLE_NAME, null, values);
        client.setId(id);
    }

    public ArrayList<Client> getClients(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
            ClientEntry._ID,
            ClientEntry.COLUMN_NAME_NAME,
            ClientEntry.COLUMN_NAME_ADDRESS,
            ClientEntry.COLUMN_NAME_PHONE
        };
        String sortOrder = ClientEntry._ID + " ASC";

        Cursor cursor = db.query(ClientEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
        ArrayList<Client> clients = new ArrayList<>();
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ClientEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ClientEntry.COLUMN_NAME_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(ClientEntry.COLUMN_NAME_ADDRESS));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(ClientEntry.COLUMN_NAME_PHONE));
            clients.add(new Client(id, name, address, phone));
        }
        return clients;
    }
}
