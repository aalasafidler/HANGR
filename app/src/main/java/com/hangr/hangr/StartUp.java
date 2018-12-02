package com.hangr.hangr;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StartUp extends AppCompatActivity {
    /**
     * StartUp activity displays buttons to navigate through our app and some weather info
     */

    // Initialise elements on the startup activity
    private Button addItembtn, viewOutfitsbtn, addOutfitsbtn, viewItemsbtn;
    private TextView weatherInfoTextView;
    public static WardrobeItemDatabase wardrobeItemDatabase;
    static final int CAM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Initialises layout and elements of startup screen
         * Adds listeners to buttons and displays weather info
         */

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_up);

        // Hide navbar
        hideNavigationBar();

        //Build database
        wardrobeItemDatabase = Room.databaseBuilder(getApplicationContext(), WardrobeItemDatabase.class, "itemsdb.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        // Opens camera when add item button clicked
        addItembtn = findViewById(R.id.addItembtn);
        addItembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewItemActivity();
            }
        });

        // Opens create outfit screen when add outfit button clicked
        addOutfitsbtn = findViewById(R.id.addOutfitsbtn);
        addOutfitsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateOutfitActivity();
            }
        });

        // Opens the view all outfits activity when view outfits button clicked
        viewOutfitsbtn = findViewById(R.id.viewOutfitsbtn);
        viewOutfitsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewOutfitsActivity();
            }
        });

        // Opens the view all items gallery activity when view items button clicked
        viewItemsbtn = findViewById(R.id.viewItemsbtn);
        viewItemsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewItemsActivity();
            }
        });


        // Initialise weather and temperature textviews, fill them with weather info
        weatherInfoTextView = findViewById(R.id.weatherInfoTextView);
        weatherInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeatherActivity();
            }
        });

        if (isNetworkAvailable()) {
            findWeather();
        }
        else{
            weatherInfoTextView.setTextColor(this.getResources().getColor(R.color.faded_blue));
            weatherInfoTextView.setText("Weather information not available");
        }
    }
    private boolean isNetworkAvailable() {
        // Check for internet connection
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    public void findWeather() {
        // Requests weather from OpenWeatherMap API and displays weather information

        // Url holds location and api key for the request
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Dublin,Ireland&appid=4acded150a8a59a5e4a0a894906adf09&units=metric";


        // Send GET request to openweathermap and pick apart the response
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Pick apart the JSON response
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    double temp_int = Double.parseDouble(temp);

                    if (temp_int > 15){
                        weatherInfoTextView.setText("It will be warm today with " + description + " and temperatures of " + temp_int + "°C");
                    } else if (temp_int < 15 && temp_int > 10) {
                        weatherInfoTextView.setText("It will be mild today with " + description + " and temperatures of " + temp_int + "°C");
                    } else if (temp_int < 10 && temp_int > 6) {
                        weatherInfoTextView.setText("It will be cold today with " + description + " and temperatures of " + temp_int + "°C. Wrap up!");
                    } else if (description == "broken clouds") {
                        weatherInfoTextView.setText("It will be cloudy today with temperatures around " + temp_int + "°C");
                    } else{
                        weatherInfoTextView.setText("Wrap up warm today as it will be cold, temperatures just " + temp_int + "°C");
                    }



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
        queue.add(jor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();

    }

    private void hideNavigationBar() {
        // Hides elements of the navigation bar we don't want to display
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    public void openNewItemActivity() {
        // Creates intent to take a picture
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Initialise the file that the picture you are about to take will be saved into
        File file = getFile();
        System.out.println("Picture will be saved to: " + file);

        // Saves the picture you took into the file
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(StartUp.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file));

        // Keeps track of the most recent picture taken by the user
        NewItem.setMostRecentPic(file);
        startActivityForResult(camera_intent, CAM_REQUEST);
    }

    public void openCreateOutfitActivity() {
        Intent intent = new Intent(this, CreateOutfit.class);
        startActivity(intent);
    }

    public void openViewOutfitsActivity() {
        Intent intent = new Intent(this, ViewOutfits.class);
        startActivity(intent);
    }

    public void openViewItemsActivity() {
        Intent intent = new Intent(this, ViewAllItems.class);
        startActivity(intent);
    }

    public void openWeatherActivity() {
        Intent intent = new Intent(this, Weather.class);
        startActivity(intent);
    }


    private File getFile() {
        // Makes directory and filename for picture to be saved

        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        System.out.println("Folder: " + folder);

        if (!folder.exists()) {
            folder.mkdir();
        }

        // Creates collision resistant filename for the image using timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
        String imageFileName = "Hangr_" + timeStamp + ".jpg";

        File image_file = new File(folder, imageFileName);
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // This method is called when you return from taking the picture with camera intent
        // Sends the user to the new item screen, not back to the start screen

        super.onActivityResult(requestCode, resultCode, data);

        // 1 is the code when returning from the camera
        // Starts the NewItem activity after picture is taken
        if (requestCode == 1) {
            Intent home = new Intent(StartUp.this, NewItem.class
            );
            startActivity(home);
        }
    }

    public void printDatabaseToConsole() {
        /**
         * Prints all rows in the Rooms database to the console in Android studio
         * Workaround function to view all items in the database
         * This function was used during development, not needed for production
         * To use this function: need to create a button on Startup with id print_items_button and call this method onclick
         */

//        viewItemsButton = findViewById(R.id.print_items_button);
//        viewItemsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<WardrobeItem> items = StartUp.wardrobeItemDatabase.wardrobeItemDao().getItems();
//
//                String info = "ID\tcategory\tlocation\tstyle\tcolour\tclean\timageFilePath\n\n";
//
//                for (WardrobeItem item : items) {
//                    int id = item.getId();
//                    String category = item.getCategory();
//                    String location = item.getLocation();
//                    String style = item.getStyle();
//                    String colour = item.getColour();
//                    String clean = Boolean.toString(item.getClean());
//                    String imageFilePath = item.getImageFilePath();
//
//                    info += id + "\t" + category + "\t" + location + "\t" + style + "\t" + colour + "\t" + clean + "\t" + imageFilePath + "\n";
//                }
//
//                Toast.makeText(StartUp.this, "Check terminal in android studio.", Toast.LENGTH_SHORT).show();
//                System.out.println(info);
//            }
//        });
    }
}