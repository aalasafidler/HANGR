package com.hangr.hangr;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class SavedOutfits extends AppCompatActivity {

    CarouselView saved_outfits_carousel;
    int[] savedOutfitsArray = {R.drawable.o1, R.drawable.o2, R.drawable.o3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_outfits);


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
}

