package com.syd.oden.gesturelock.view;

import android.content.Context;
import android.content.SharedPreferences;

public class GesturePreference {
    private Context context;
    private final String fileName = "filename";
    private String nameTable = "nameTable";

    public GesturePreference(Context context, int nameTableId) {
        this.context = context;
        if (nameTableId != -1)
            this.nameTable = nameTable + nameTableId;
    }

    public void WriteStringPreference(String data) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nameTable, data);
        editor.apply();
    }

    public String ReadStringPreference() {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return preferences.getString(nameTable, "null");
    }

}
