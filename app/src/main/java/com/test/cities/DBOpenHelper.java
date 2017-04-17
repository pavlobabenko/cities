package com.test.cities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "Cities.db";
    static final String TABLE_NAME_COUNTRIES = "countries";
    static final String TABLE_NAME_CITIES = "cities";
    static final String COLUMN_NAME_COUNTRY_ID = "country_id";
    static final String COLUMN_NAME_COUNTRY = "country";
    static final String COLUMN_NAME_CITY = "city";
    private final String SQL_TABLE_COUNTRIES_CREATE = "CREATE TABLE " +
            TABLE_NAME_COUNTRIES + " (" +
            COLUMN_NAME_COUNTRY_ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME_COUNTRY + " TEXT);";
    private final String SQL_TABLE_CITIES_CREATE = "CREATE TABLE " +
            TABLE_NAME_CITIES + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_CITY + " TEXT," +
            COLUMN_NAME_COUNTRY_ID +" INTEGER, FOREIGN KEY ("+COLUMN_NAME_COUNTRY_ID+") REFERENCES countries("+COLUMN_NAME_COUNTRY_ID+"));";

    DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TABLE_COUNTRIES_CREATE);
        db.execSQL(SQL_TABLE_CITIES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

