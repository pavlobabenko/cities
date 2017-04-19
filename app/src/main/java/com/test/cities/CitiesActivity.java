package com.test.cities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class CitiesActivity extends AppCompatActivity {

    private CitiesDao citiesDao = new CitiesDao(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        String countryName = getIntent().getStringExtra("countryName");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citiesDao.findCitiesByCountryName(countryName));
        ListView listView = (ListView) findViewById(R.id.citiesList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = ((TextView) view).getText().toString();
                System.out.println(city);
                new CitiesInfoTask().execute(city);
            }
        });
    }

    private class CitiesInfoTask extends AsyncTask<String, Void, String> {

        String cityName;
        String cityInfo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... city) {
            cityName = city[0];
            String cityNameInUrl = "";
            try {
                cityNameInUrl = URLEncoder.encode(cityName,"UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileDownloader fileDownloader = new FileDownloader();
            String jsonString = fileDownloader.downloadFile("http://api.geonames.org/wikipediaSearchJSON?q=" + cityNameInUrl + "&username=pavlo.babenko ");
            try {
                JSONObject cityInfoJSONObject = new JSONObject(jsonString);
                JSONArray cityInfoJSONArray = cityInfoJSONObject.getJSONArray("geonames");
                JSONObject jsonCityInfo = cityInfoJSONArray.getJSONObject(0);
                cityInfo = jsonCityInfo.getString("summary");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cityInfo;
        }

        @Override
        protected void onPostExecute(String cityInfo) {
            super.onPostExecute(cityInfo);
            final AlertDialog.Builder builder = new AlertDialog.Builder(CitiesActivity.this);
            builder.setMessage(cityInfo).setTitle(cityName);
            builder.create().show();
        }
    }
}
