package com.test.cities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


public class MainActivity extends AppCompatActivity {


    private final String url = "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";

    private ProgressDialog progressDialog;
    private ListView listView;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        dbOpenHelper = new DBOpenHelper(this);
        database = dbOpenHelper.getReadableDatabase();
        if (database.rawQuery("SELECT * FROM " + TABLE_NAME_COUNTRIES + ";", null).getCount() == 0) {
            new DownloadTask().execute();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readCountries());
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CitiesActivity.class);
                intent.putExtra("id", position + 1);
                startActivity(intent);
            }
        });
    }

    private List<String> readCountries() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME_COUNTRIES + ";", null);
        List<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COUNTRY)));
        }
        cursor.close();
        return arrayList;
    }

    private class DownloadTask extends AsyncTask<Void, Void, Void> {

        private FileDownloader fileDownloader = new FileDownloader();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Подождите, идет загрузка");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String jsonString = fileDownloader.downloadFile(url);
                jsonToDataBase(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, readCountries());
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    private void jsonToDataBase(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray countriesJSONArray = jsonObject.names();
        database = dbOpenHelper.getWritableDatabase();
        for (int i = 0; i < countriesJSONArray.length(); i++) {
            try {
                int country_id = i + 1;
                String country = countriesJSONArray.getString(i);
                ContentValues valuesCountries = new ContentValues();
                valuesCountries.put(COLUMN_NAME_COUNTRY_ID, country_id);
                valuesCountries.put(COLUMN_NAME_COUNTRY, country);
                database.insert(TABLE_NAME_COUNTRIES, null, valuesCountries);

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
}

