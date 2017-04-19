package com.test.cities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountriesDao {

    private DBOpenHelper dbOpenHelper;

    public CountriesDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public List<String> findCountries() {
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM countries;", null);
        List<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("country")));
        }
        cursor.close();
        return arrayList;
    }

    public void insertToDataBase(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray countriesJSONArray = jsonObject.names();
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            for (int i = 0; i < countriesJSONArray.length(); i++) {
                try {
                    int country_id = i + 1;
                    String country = countriesJSONArray.getString(i);
                    ContentValues valuesCountries = new ContentValues();
                    valuesCountries.put("country_id", country_id);
                    valuesCountries.put("country", country);
                    database.insert("countries", null, valuesCountries);
                    JSONArray citiesJSONArray = jsonObject.getJSONArray(country);
                    for (int j = 0; j < citiesJSONArray.length(); j++) {
                        String city = citiesJSONArray.getString(j);
                        ContentValues valuesCities = new ContentValues();
                        valuesCities.put("city", city);
                        valuesCities.put("country_id", country_id);
                        database.insert("cities", null, valuesCities);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }

    public boolean isDataBaseEmpty() {
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        return !database.rawQuery("SELECT * FROM countries;", null).moveToFirst();
    }
}