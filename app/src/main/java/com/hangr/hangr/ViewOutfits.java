package com.hangr.hangr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewOutfits extends AppCompatActivity {
    CarouselView savedOutfits;
    File[] outfitImages;
    List<Bitmap> outfitBitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_outfits);

        savedOutfits = findViewById(R.id.saved_outfits_carousel);

        // The folder where all the outfits you've made are saved
        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Outfits");

        outfitImages = folder.listFiles();


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
}
