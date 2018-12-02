package com.hangr.hangr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SensorEventListener {
    /**
     * Activity where user adds information to be associated with the image they just took
     * Saves the item to a database
     */

    // Initialise layout elements
    Button cancel_button, save_button;
    Spinner Category, Style, Colour;
    EditText Location;
    CheckBox Clean;
    ImageView Preview;
    Bitmap previewBitmap;


    // Sensor
    private SensorManager mSensorManager;
    private Sensor mLight;

    // Getters and setters for most recent picture taken by the user, used to show preview of image
    private static File mostRecentPic;

    public static void setMostRecentPic(File file) {
        mostRecentPic = file;
    }

    public static File getMostRecentPic() {
        return mostRecentPic;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        getSupportActionBar().setTitle("Add New Item");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialises style dropdown menu
        Spinner spinner1 = findViewById(R.id.style_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        //Initialises category dropdown menu
        Spinner spinner2 = findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.styles, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        //Initialises colours dropdown menu
        Spinner spinner3 = findViewById(R.id.colour_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.colours, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);

        // Display preview of the image you just took
        previewBitmap = BitmapFactory.decodeFile(mostRecentPic.getAbsolutePath());
        Preview = findViewById(R.id.preview_imageview);
        Preview.setImageBitmap(previewBitmap);

        // Initialise save button and layout elements you want to save information from
        save_button = findViewById(R.id.save_button);
        cancel_button = findViewById(R.id.cancel_button);
        Category = findViewById(R.id.category_spinner);
        Location = findViewById(R.id.location_edittext);
        Style = findViewById(R.id.style_spinner);
        Colour = findViewById(R.id.colour_spinner);
        Clean = findViewById(R.id.clean_checkbox);

        // Saves item info to database when save button is clicked
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collect values chosen from spinners etc.
                String category = Category.getSelectedItem().toString();
                String location = Location.getText().toString();
                String style = Style.getSelectedItem().toString();
                String colour = Colour.getSelectedItem().toString();
                boolean clean = Clean.isChecked();

                // Set the values chosen for the item
                WardrobeItem wardrobeItem = new WardrobeItem();
                wardrobeItem.setCategory(category);
                wardrobeItem.setLocation(location);
                wardrobeItem.setStyle(style);
                wardrobeItem.setColour(colour);
                wardrobeItem.setClean(clean);
                // Saves file path of most recent image as a String
                wardrobeItem.setImageFilePath(mostRecentPic.getAbsolutePath());

                // Save the item to the database
                StartUp.wardrobeItemDatabase.wardrobeItemDao().addItem(wardrobeItem);
                Toast.makeText(NewItem.this, "Item added successfully!", Toast.LENGTH_SHORT).show();

                // Resets the spinners to default values
                Category.setSelection(0, true);
                Location.setText("Home");
                Style.setSelection(0, true);
                Colour.setSelection(0, true);
                Clean.setChecked(false);


                // Open view all items activity
                viewGallery();

            }
        });

        // Initialise Light Sensor
        mSensorManager = (SensorManager) getSystemService(NewItem.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Cancel button brings user back to startup activity
        cancel_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),StartUp.class);
                startActivity(i);
            }
        });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_item_menu, menu);
        return true;
    }

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

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    // Gets the light measurement from light sensor
    public final void onSensorChanged(SensorEvent event) {
        float lightMeasurement = event.values[0];

        // If lux measurement is below 10 the phone is in darkness, the image taken is likely too dark to be visible
        if (lightMeasurement < 10) {
            String text = "That picture is very dark, please take another";
            Toast.makeText(NewItem.this, text, Toast.LENGTH_SHORT).show();

            // Unregister the listener so it doesn't constantly use resources, not essential to run constantly
            mSensorManager.unregisterListener(this);
        }

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // When this method is run, View Outfits activity is opened.
    public void goBack() {
        Intent intent = new Intent(this, StartUp.class);
        startActivity(intent);
    }

    // When this method is run, all saved items will be shown.
    public void viewGallery() {
        Intent intent = new Intent(this, ViewAllItems.class);
        startActivity(intent);
    }
}
