package com.test.cities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CitiesDao {

    private DBOpenHelper dbOpenHelper;

    public CitiesDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public List<String> findCitiesByCountryName(String countryName) {
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT cities.city FROM cities, countries WHERE cities.country_id = countries.country_id AND countries.country = '"+countryName+"';", null);
        List<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("city")));
        }
        cursor.close();
        return arrayList;
    }
}