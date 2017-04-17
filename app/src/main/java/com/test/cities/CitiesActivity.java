package com.test.cities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.test.cities.DBOpenHelper.COLUMN_NAME_CITY;
import static com.test.cities.DBOpenHelper.COLUMN_NAME_COUNTRY_ID;
import static com.test.cities.DBOpenHelper.TABLE_NAME_CITIES;

public class CitiesActivity extends AppCompatActivity {

    ListView listView;
    int id;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        id = getIntent().getIntExtra("id", 0);

        dbOpenHelper = new DBOpenHelper(this);
        database = dbOpenHelper.getReadableDatabase();
        listView = (ListView) findViewById(R.id.cities_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readCities());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private ArrayList<String> readCities() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME_CITIES + " WHERE " + COLUMN_NAME_COUNTRY_ID + "= " + Integer.toString(id) + ";", null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CITY)));
        }
        cursor.close();
        return arrayList;
    }
}
