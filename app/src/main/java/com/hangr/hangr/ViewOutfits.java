package com.hangr.hangr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
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

        savedOutfits.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(ViewOutfits.this, "Clicked item: "+  getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Outfits"), Toast.LENGTH_SHORT).show();
                share(position);
            }
        });

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
// INSTAGRAM intent shite
// String type = "image/*";
//        String filename = "/myPhoto.jpg";
//        String mediaPath = Environment.getExternalStorageDirectory() + filename;

        switch (item.getItemId()) {
            case R.id.go_back:
                goBack();
                return true;

            case R.id.view_gallery:
                viewGallery();
                return true;


            case R.id.share:
                //createInstagramIntent(type, mediaPath);
                share();
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

    //public void share() {

    public void share() {
        String folder = Environment.DIRECTORY_PICTURES + "/Outfits/HangrOutfit_20181127_164839";
        //String uri = ImageView.setImageBitmap(Uri.parse(new File(Environment.DIRECTORY_PICTURES + "/Outfits/*.jpg").toString()));


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, folder);
        sendIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    public void share(int position) {
        String folder =  "" + getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Outfits/0.jpg");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(folder));
        sendIntent.setType("image/*");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }
    }

//    public void share() {
//
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.setType("text/plain");
//        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
//    }

//    private void createInstagramIntent(String type, String mediaPath){
//
//        // Create the new Intent using the 'Send' action.
//        Intent share = new Intent(Intent.ACTION_SEND);
//
//        // Set the MIME type
//        share.setType(type);
//
//        // Create the URI from the media
//        File media = new File(mediaPath);
//        Uri uri = Uri.fromFile(media);
//
//        // Add the URI to the Intent.
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//
//        // Broadcast the Intent.
//        startActivity(Intent.createChooser(share, "Share to"));
//    }

//}