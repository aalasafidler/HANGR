package com.hangr.hangr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewOutfits extends AppCompatActivity {
    /**
     * View all outfits created by the user
     */

    // Initialise layout elements
    CarouselView savedOutfits;
    File[] outfitImages;
    List<Bitmap> outfitBitmaps = new ArrayList<>();
    Button shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_outfits);

        savedOutfits = findViewById(R.id.saved_outfits_carousel);

        // Get the folder where all the outfits you've made are saved
        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Outfits");

        outfitImages = folder.listFiles();

        // Create a bitmap of each outfit and add it to the bitmap arraylist
        for (File outfit : outfitImages) {
            String filePath = outfit.getPath();
            System.out.println(filePath);
            Bitmap outfitBitmap = BitmapFactory.decodeFile(filePath);
            outfitBitmaps.add(outfitBitmap);
        }

        // Set page number and listener for the outfits CarouselView
        savedOutfits.setPageCount(outfitImages.length);
        savedOutfits.setImageListener(outfits_Listener);

        // Share the outfit image currently selected by the user if share button is clicked
        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                // Gets position of the currently selected outfit within the CarouselView
                int position = savedOutfits.getCurrentItem();

                File outfitToShare = outfitImages[position];

                sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ViewOutfits.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        outfitToShare));
                sendIntent.setType("image/jpeg");

                // Opens intent to share image with other apps
                startActivity(Intent.createChooser(sendIntent, "Send to.."));
                
            }
        });

    }

    // Sets images displayed within outfits CarouselView
    ImageListener outfits_Listener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageBitmap(outfitBitmaps.get(position));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewing_outfits_menu, menu);
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

    // When this method is run, start up activity is opened.
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