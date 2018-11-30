package com.hangr.hangr;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.util.ArrayList;

public class SavedOutfits extends AppCompatActivity {

    CarouselView saved_outfits_carousel;
    int[] savedOutfitsArray = {R.drawable.o1, R.drawable.o2, R.drawable.o3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_outfits);
        getSupportActionBar().setTitle("Saved Outfits");

        saved_outfits_carousel = (CarouselView) findViewById(R.id.saved_outfits_carousel);
        saved_outfits_carousel.setPageCount(savedOutfitsArray.length);
        saved_outfits_carousel.setImageListener(imageListener);


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(savedOutfitsArray[position]);
        }
    };


    //    //https://www.youtube.com/watch?v=kknBxoCOYXI
    // This method creates the menu in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.saved_outfits_menu, menu);
        return true;
    }

    // This method controls what is done when menu items are selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back:
                goBack();
                return true;

            case R.id.go_to_camera:
                openCamera();
                return true;

            case R.id.all_items:
                gogallery();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // When run opens Saved Outfits activity.
    public void goBack() {
        Intent intent = new Intent(this, CreateOutfit.class);
        startActivity(intent);
    }


    // When run opens Saved Outfits activity.
    public void gogallery() {
        Intent intent = new Intent(this, test.class);
        startActivity(intent);
    }

    public void openCamera() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(camera_intent);

    }
}

