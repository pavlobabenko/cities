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

import static com.test.cities.DBOpenHelper.COLUMN_NAME_CITY;
import static com.test.cities.DBOpenHelper.COLUMN_NAME_COUNTRY;
import static com.test.cities.DBOpenHelper.COLUMN_NAME_COUNTRY_ID;
import static com.test.cities.DBOpenHelper.TABLE_NAME_CITIES;
import static com.test.cities.DBOpenHelper.TABLE_NAME_COUNTRIES;

public class CountriesDao {

    private DBOpenHelper dbOpenHelper;

    public CountriesDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public List<String> readCountries() {
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME_COUNTRIES + ";", null);
        List<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COUNTRY)));
        }
        cursor.close();
        return arrayList;
    }

    public void jsonToDataBase(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray countriesJSONArray = jsonObject.names();
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        for (int i = 0; i < countriesJSONArray.length(); i++) {
            try {
                int country_id = i + 1;
                String country = countriesJSONArray.getString(i);
                ContentValues valuesCountries = new ContentValues();
                valuesCountries.put(COLUMN_NAME_COUNTRY_ID, country_id);
                valuesCountries.put(COLUMN_NAME_COUNTRY, country);
                database.insert(DBOpenHelper.TABLE_NAME_COUNTRIES, null, valuesCountries);

                JSONArray citiesJSONArray = jsonObject.getJSONArray(country);
                for (int j = 0; j < citiesJSONArray.length(); j++) {
                    String city = citiesJSONArray.getString(j);
                    ContentValues valuesCities = new ContentValues();
                    valuesCities.put(COLUMN_NAME_CITY, city);
                    valuesCities.put(COLUMN_NAME_COUNTRY_ID, country_id);
                    database.insert(TABLE_NAME_CITIES, null, valuesCities);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasEntries() {
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME_COUNTRIES + ";", null).getCount() != 0;
    }
}