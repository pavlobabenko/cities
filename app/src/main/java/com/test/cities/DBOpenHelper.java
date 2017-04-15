package com.test.cities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "Cities.db";
    final String TABLE_NAME_COUNTRIES = "countries";
    private final String TABLE_NAME_CITIES = "cities";
    final String COLUMN_NAME_COUNTRY = "country";
    private final String COLUMN_NAME_CITY = "city";
    private final String COLUMN_NAME_COUNTRY_ID = "country_id";
    private final String SQL_TABLE_COUNTRIES_CREATE = "CREATE TABLE " +
            TABLE_NAME_COUNTRIES + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_COUNTRY + " TEXT);";
    private final String SQL_TABLE_CITIES_CREATE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_CITIES + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_COUNTRY + " TEXT);";

    DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TABLE_COUNTRIES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

