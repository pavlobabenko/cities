package com.test.cities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json";

    private CountriesDao countriesDao = new CountriesDao(this);
    private ProgressDialog progressDialog;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        if (countriesDao.hasEntries()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countriesDao.readCountries());
            listView.setAdapter(adapter);
        } else {
            new DownloadTask().execute();
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
                countriesDao.jsonToDataBase(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, countriesDao.readCountries());
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
}

