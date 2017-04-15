package com.test.cities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ListView listView;
    DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        dbOpenHelper = new DBOpenHelper(this);
        if (readDatabase().isEmpty()) {
            new DownloadTask().execute();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readDatabase());
            listView.setAdapter(adapter);
        }
    }

    private ArrayList<String> readDatabase() {
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = database.query(dbOpenHelper.TABLE_NAME_COUNTRIES,
                null,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(1));
        }
        cursor.close();
        return arrayList;
    }

    private class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Подождите, идет загрузка");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
//            deleteDatabase(DBOpenHelper.DATABASE_NAME);
            try {
                createDatabase(jsonParse(downloadJSON()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, readDatabase());
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    private void createDatabase(ArrayList<String> arrayCountries) {
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        for (String country : arrayCountries) {
            ContentValues values = new ContentValues();
            values.put(dbOpenHelper.COLUMN_NAME_COUNTRY, country);
            database.insert(dbOpenHelper.TABLE_NAME_COUNTRIES, null, values);
        }
    }

    private String downloadJSON() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json");
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            return readInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readInputStream(InputStream inputStream) {
        try (Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }

    private ArrayList<String> jsonParse(String jsonString) throws JSONException {
        System.out.println(jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray namesJsonArray = jsonObject.names();
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < namesJsonArray.length(); i++) {
            try {
                array.add(namesJsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(array);
        return array;
    }
}

