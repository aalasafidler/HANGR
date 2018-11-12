package com.hangr.hangr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class CreateOutfit extends AppCompatActivity{

    CarouselView tops_carousel, bottoms_carousel, shoes_carousel;

    //Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/camera-app/Hangr_20181025__143243.jpg");


    int[] tops_images = {R.drawable.t083263large, R.drawable.t3539717726366large, R.drawable.t5397177446928large, R.drawable.t35397121456317large, R.drawable.t35397175921984large};
    int[] bottoms_images = {R.drawable.b35397177259627large, R.drawable.b5397177424742large, R.drawable.b3539717742438large};
    int[] shoes_images = {R.drawable.s3539717635451large, R.drawable.s35397172277367large, R.drawable.s35397176100838large, R.drawable.s35397176103853large, R.drawable.s35397176433110large};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_outfit);
        getSupportActionBar().setTitle("Create an Outfit");

        // Scroll view for tops
        LinearLayout topsGallery = findViewById(R.id.tops_gallery);

        LayoutInflater inflater = LayoutInflater.from(CreateOutfit.this);

        for (int i = 0; i < 6; i++) {
            View view = inflater.inflate(R.layout.tops_items, topsGallery, false);

            ImageView topsImageView = view.findViewById(R.id.tops_imageview);

            topsImageView.setImageResource(R.drawable.logo);
            topsGallery.addView(view);

        }

        // Dealing with the bottoms
        TextView bottoms_text = findViewById(R.id.bottoms_text);
        bottoms_carousel = (CarouselView) findViewById(R.id.bottoms_carousel);
        bottoms_carousel.setPageCount(bottoms_images.length);
        bottoms_carousel.setImageListener(bottoms_Listener);

        // Dealing with the shoes
        TextView shoes_text = findViewById(R.id.shoes_text);
        shoes_carousel = (CarouselView) findViewById(R.id.shoes_carousel);
        shoes_carousel.setPageCount(shoes_images.length);
        shoes_carousel.setImageListener(shoes_Listener);
    }
//    //https://www.youtube.com/watch?v=kknBxoCOYXI
    @Override
            public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.outfits_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.view_saved_outfits:
                openSavedOutfits();
                return true;

            case R.id.settings:
                openSavedOutfits();
                return true;

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

    public void viewGallery(){
        Intent intent = new Intent(this, ViewAllItems.class);
        startActivity(intent);
    }

    // When run opens Saved Outfits activity.
    public void openSavedOutfits() {
        Intent intent = new Intent(this, SavedOutfits.class);
        startActivity(intent);
    }

    // When run opens start up activity.
    public void goBack() {
        Intent intent = new Intent(this, StartUp.class);
        startActivity(intent);
    }

    ImageListener tops_Listener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(tops_images[position]);
        }
    };

    ImageListener bottoms_Listener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(bottoms_images[position]);
        }
    };

    ImageListener shoes_Listener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(shoes_images[position]);
        }
    };

}
