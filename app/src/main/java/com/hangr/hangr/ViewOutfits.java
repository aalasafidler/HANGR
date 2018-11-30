package com.hangr.hangr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewOutfits extends AppCompatActivity {

    // All Dillon's
    CarouselView savedOutfits;
    File[] outfitImages;
    List<Bitmap> outfitBitmaps = new ArrayList<>();

    // Aalasa's
    List<Bitmap> tops_images = new ArrayList<>();
    Bitmap selectedTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_outfits);

        savedOutfits = findViewById(R.id.saved_outfits_carousel);

        // The folder where all the outfits you've made are saved
        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Outfits");

        outfitImages = folder.listFiles();

        String type = "image/*";
        String filename = "/myPhoto.jpg";
        String mediaPath = Environment.getExternalStorageDirectory() + filename;
        System.out.println(outfitImages);


        // TODO: Make bitmaps scale to the size of the carouselview
        for (File outfit : outfitImages) {
            String filePath = outfit.getPath();
            System.out.println(filePath);
            Bitmap outfitBitmap = BitmapFactory.decodeFile(filePath);
            outfitBitmaps.add(outfitBitmap);
        }

        savedOutfits.setPageCount(outfitImages.length);
        savedOutfits.setImageListener(outfits_Listener);
    }

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

            case R.id.share:
                share();
                return true;

            default:
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
        Intent intent = new Intent(this, test.class); //ViewAllItems.class);
        startActivity(intent);
    }

    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

}