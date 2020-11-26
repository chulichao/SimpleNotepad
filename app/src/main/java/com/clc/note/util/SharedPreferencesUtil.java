package com.clc.note.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private SharedPreferences mPreferences = AppContext.getInstance().getSharedPreferences(null, Context.MODE_PRIVATE);

    public void setKey(String key,String value){
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putString(key,value);
        edit.commit();
    }

    public String getValue(String key){
        return mPreferences.getString(key,"-1");
    }

    public void removeKey(String key){
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.remove(key);
        edit.commit();
    }
}
