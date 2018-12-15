package com.example.szafa.myway;

import android.content.Context;
import android.widget.Toast;

public class Tools {
    //function to show error messages
    public static void exceptionToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
