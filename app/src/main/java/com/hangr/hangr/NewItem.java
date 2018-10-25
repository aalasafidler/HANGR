package com.hangr.hangr;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SensorEventListener {
    Button camera_button;
    static final int CAM_REQUEST = 1;

    private SensorManager mSensorManager;
    private Sensor mLight;

    // test push

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        getSupportActionBar().setTitle("Add New Item");


        //Initialises style dropdown menu
        Spinner spinner1 = findViewById(R.id.styleSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        //Initialises category dropdown menu
        Spinner spinner2 = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.styles, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        // Camera button
        camera_button = (Button) findViewById(R.id.camera_button);

        // Opens Camera on click, saves picture to sdcard
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(NewItem.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file));


                startActivityForResult(camera_intent, CAM_REQUEST);
            }
        });

        // Light Sensor
        mSensorManager = (SensorManager) getSystemService(NewItem.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lightMeasurement = event.values[0];
        // Do something with this sensor data.

        // If lux measurement is below the phone is in darkness, display toast to move to brighter room
        if (lightMeasurement < 10) {
            String text = "Room too dark, lux: " + lightMeasurement;
            Toast.makeText(NewItem.this, text, Toast.LENGTH_SHORT).show();
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


    private File getFile(){
        // Makes directory and filename for picture to be saved
        File folder = new File("sdcard/camera-app");
        if(!folder.exists()){
            folder.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
        String imageFileName = "Hangr_" + timeStamp + ".jpg";

        File image_file = new File(folder, imageFileName);
        return image_file;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Activates toast when dropdown is selected.
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
