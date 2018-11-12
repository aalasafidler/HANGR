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
    Button cancel_button;
    Button save_button;
    Spinner Category;
    EditText Location;
    Spinner Style;
    Spinner Colour;
    CheckBox Clean;
    ImageView Preview;
    Bitmap previewBitmap;
    private static File mostRecentPic;

    private SensorManager mSensorManager;
    private Sensor mLight;

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

        System.out.println("Most recent pic: " + getMostRecentPic().toString());


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

        save_button = (Button) findViewById(R.id.save_button);

        Category = findViewById(R.id.category_spinner);
        Location = findViewById(R.id.location_edittext);
        Style = findViewById(R.id.style_spinner);
        Colour = findViewById(R.id.colour_spinner);
        Clean = findViewById(R.id.clean_checkbox);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collect options chosen from spinners etc.
                String category = Category.getSelectedItem().toString();
                String location = Location.getText().toString();
                String style = Style.getSelectedItem().toString();
                String colour = Colour.getSelectedItem().toString();
                boolean clean = Clean.isChecked();

                // Save the options chosen for the item
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

                // Reset the spinners to default values
                Category.setSelection(0, true);
                Location.setText("");
                Style.setSelection(0, true);
                Colour.setSelection(0, true);
                Clean.setChecked(false);
            }
        });
        // Light Sensor
        mSensorManager = (SensorManager) getSystemService(NewItem.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

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
    public final void onSensorChanged(SensorEvent event) {
        float lightMeasurement = event.values[0];
        // Do something with this sensor data.

        // If lux measurement is below 10 the phone is in darkness, display toast to move to brighter room
        if (lightMeasurement < 10) {
            String text = "Room too dark, lux: " + lightMeasurement;
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
        // Activates toast when dropdown is selected.
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
}
