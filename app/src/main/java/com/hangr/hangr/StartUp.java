package com.hangr.hangr;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class StartUp extends AppCompatActivity {
    private Button button;
    private Button outfitButton;
    private Button viewItemsButton;
    private TextView weatherInfoTextView;
    private TextView tempTextView;
    public static WardrobeItemDatabase wardrobeItemDatabase;
    //private Button v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_up);

        //Build database
        wardrobeItemDatabase = Room.databaseBuilder(getApplicationContext(),WardrobeItemDatabase.class,"itemsdb.db").allowMainThreadQueries().build();


        hideNavigationBar();
        button = findViewById(R.id.newItemButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewItemActivity();
            }
        });
        weatherInfoTextView = findViewById(R.id.weatherInfoTextView);
        tempTextView = findViewById(R.id.tempTextView);

//        aalasaButton = findViewById(R.id.aalasaButton);
//        aalasaButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCreateOutfitActivity();
//            }
//        });

        outfitButton = findViewById(R.id.outfitButton);
        outfitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateOutfitActivity();
            }
        });

        viewItemsButton = findViewById(R.id.print_items_button);
        viewItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<WardrobeItem> items = StartUp.wardrobeItemDatabase.wardrobeItemDao().getItems();

                String info = "ID\tcategory\tlocation\tstyle\n\n";

                for (WardrobeItem item : items) {
                    int id = item.getId();
                    String category = item.getCategory();
                    String location = item.getLocation();
                    String style = item.getStyle();

                    info += id + "\t" + category + "\t" + location + "\t" + style + "\n";
                }

                Toast.makeText(StartUp.this, "Check terminal in android studio.", Toast.LENGTH_SHORT).show();
                System.out.println(info);
            }
        });

        findWeather();
    }

    public void findWeather(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Dublin,Ireland&appid=4acded150a8a59a5e4a0a894906adf09&units=metric";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");

                    weatherInfoTextView.setText("The weather will be: " + description);

                    double temp_int = Double.parseDouble(temp);
//                    double centi = (temp_int - 32) / 1.800;
//                    centi = Math.round(centi);
//                    int i = (int)centi;
                    tempTextView.setText("The temp will be " + temp_int + " celsius");

                }catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
    @Override
    protected void onResume() {
        super.onResume();

        hideNavigationBar();

    }

    private void hideNavigationBar(){
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    public void openNewItemActivity() {
        Intent intent = new Intent(this, NewItem.class);
        startActivity(intent);
    }

    public void openCreateOutfitActivity() {
        Intent intent = new Intent(this, CreateOutfit.class);
        startActivity(intent);
    }
}
