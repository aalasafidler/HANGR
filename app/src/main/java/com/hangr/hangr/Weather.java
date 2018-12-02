package com.hangr.hangr;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather extends AppCompatActivity {

    private TextView weatherToday, weatherTomorrow, weatherDay3;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().setTitle("Weather Forecast");

        weatherToday = findViewById(R.id.weatherToday);
        weatherTomorrow = findViewById(R.id.weatherTommorrow);
        weatherDay3 = findViewById(R.id.weatherDay3);

        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);

        if (isNetworkAvailable() == true) {
            findWeather();
        }
        else{
            weatherToday.setTextColor(this.getResources().getColor(R.color.faded_blue));
            weatherToday.setText("Weather information not available - please connect to internet.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void findWeather() {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=Dublin,Ireland&appid=4acded150a8a59a5e4a0a894906adf09&units=metric";
        // Send GET request to openweathermap

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // all in weather dictionary
                    JSONArray list = response.getJSONArray("list");
                    // T O D A Y
                    JSONObject hourZero = list.getJSONObject(0);
                    String dt = hourZero.getString("dt");
                    String dt_txt = hourZero.getString("dt_txt");
                    // Second nested dictionary
                    JSONObject main = hourZero.getJSONObject("main");
                    String temp_min = main.getString("temp_min");
                    String temp_max = main.getString("temp_max");
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");

                    JSONArray weather = hourZero.getJSONArray("weather");
                    JSONObject zero = weather.getJSONObject(0);
                    String description = zero.getString("description");

                    weatherToday.setText("Weather forecast for " + dt_txt);
                    textView3.setText("Minimums temperature: " + temp_min + "\n" +
                            "Maxmimum temperature: " + temp_max + "\n" +
                            "Pressure: " + pressure + "\n" +
                            "Humidity: " + humidity + "%" + "\n" +
                            "Forecast: " + description);

                    // T O M O R R O W
                    JSONObject hourEight = list.getJSONObject(8);
                    String dt_txt2 = hourEight.getString("dt_txt");
                    JSONObject main2 = hourEight.getJSONObject("main");
                    String temp_min2 = main2.getString("temp_min");
                    String temp_max2 = main2.getString("temp_max");
                    String pressure2 = main2.getString("pressure");
                    String humidity2 = main2.getString("humidity");

                    JSONArray weather2 = hourEight.getJSONArray("weather");
                    JSONObject zero2 = weather2.getJSONObject(0);
                    String description2 = zero2.getString("description");

               weatherTomorrow.setText("Weather forecast for " + dt_txt2);
                    textView4.setText("Minimum temperature: " + temp_min2 + "\n" +
                            "Maxmimum temperature: " + temp_max2 + "\n" +
                            "Pressure: " + pressure2 + "\n" +
                            "Humidity: " + humidity2 + "%" + "\n" +
                            "Forecast: " + description2);

                    // DAY 3
                    JSONObject hourSixteen = list.getJSONObject(16);
                    String dt_txt3 = hourSixteen.getString("dt_txt");
                    JSONObject main3 = hourSixteen.getJSONObject("main");
                    String temp_min3 = main3.getString("temp_min");
                    String temp_max3 = main3.getString("temp_max");
                    String pressure3 = main3.getString("pressure");
                    String humidity3 = main3.getString("humidity");

                    JSONArray weather3 = hourSixteen.getJSONArray("weather");
                    JSONObject zero3 = weather3.getJSONObject(0);
                    String description3 = zero3.getString("description");

                    weatherDay3.setText("Weather forecast for " + dt_txt3);
                    textView5.setText("Minimum temperature: " + temp_min3 + "\n" +
                            "Maxmimum temperature: " + temp_max3 + "\n" +
                            "Pressure: " + pressure3 + "\n" +
                            "Humidity: " + humidity3 + "%" + "\n" +
                            "Forecast: " + description3);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);}
    // This method controls what is done when menu items are selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back:
                goBack();
                return true;

            case R.id.view_gallery:
                viewGallery();
                return true;

            case R.id.view_saved_outfits:
                viewOutfits();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // When this method is run, Saved Outfits activity is opened.
    public void goBack() {
        Intent intent = new Intent(this, StartUp.class);
        startActivity(intent);
    }

    // When this method is run, all saved items will be shown.
    public void viewGallery() {
        Intent intent = new Intent(this, ViewAllItems.class);
        startActivity(intent);

    }

    // When this method is run, all saved items will be shown.
    public void viewOutfits() {
        Intent intent = new Intent(this, ViewOutfits.class);
        startActivity(intent);

    }
}
