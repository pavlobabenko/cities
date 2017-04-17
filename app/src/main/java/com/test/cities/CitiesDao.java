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

    public List<String> findCitiesByCountryId(int countryId) {
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBOpenHelper.TABLE_NAME_CITIES + " WHERE " + DBOpenHelper.COLUMN_NAME_COUNTRY_ID + "= " + Integer.toString(countryId) + ";", null);
        List<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME_CITY)));
        }
        cursor.close();
        return arrayList;
    }
}