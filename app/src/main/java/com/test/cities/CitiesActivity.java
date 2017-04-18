package com.test.cities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

public class CitiesActivity extends AppCompatActivity {

    private CitiesDao citiesDao = new CitiesDao(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        String countryName = getIntent().getStringExtra("countryName");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citiesDao.findCitiesByCountryName(countryName));
        ListView listView = (ListView) findViewById(R.id.cities_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = ((TextView) view).getText().toString();
                new CitiesInfoTask().execute(city);
            }
        });
    }

    private class CitiesInfoTask extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... city) {
            FileDownloader fileDownloader = new FileDownloader();
            String jsonString = fileDownloader.downloadFile("http://api.geonames.org/wikipediaSearchJSON?q=" + Arrays.toString(city) + "&maxRows=20&username=pavlo.babenko ");
            System.out.println(jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
