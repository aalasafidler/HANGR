package com.hangr.hangr;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;


public class CreateOutfit extends AppCompatActivity{

    CarouselView tops_carousel, bottoms_carousel, shoes_carousel;
//    Bitmap picture = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.hangr.hangr/files/Pictures/Hangr_20181112__140519.jpg");

    public static WardrobeItemDatabase wardrobeItemDatabase;

    List<Bitmap> tops_images = new ArrayList<>();
    List<Bitmap> bottoms_images = new ArrayList<>();
    List<Bitmap> shoes_images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_outfit);
        getSupportActionBar().setTitle("Create an Outfit");

        wardrobeItemDatabase = Room.databaseBuilder(getApplicationContext(), WardrobeItemDatabase.class, "itemsdb.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        List<WardrobeItem> items = CreateOutfit.wardrobeItemDatabase.wardrobeItemDao().getItems();

        for (WardrobeItem item : items) {
            String style = item.getStyle();
            Bitmap picture = BitmapFactory.decodeFile(item.getImageFilePath());

            switch (style) {
                case "Tops":
                    tops_images.add(picture);
                    break;
                case "Bottoms":
                    bottoms_images.add(picture);
                    break;
                case "Shoes":
                    shoes_images.add(picture);
                    break;
                default:
                    System.out.println("Error matching style.");
            }
        }

        // Initialise the carousel views
        tops_carousel = findViewById(R.id.tops_carousel);
        bottoms_carousel = findViewById(R.id.bottoms_carousel);
        shoes_carousel = findViewById(R.id.shoes_carousel);

        // Set the page count and listeners for each carousel
        tops_carousel.setPageCount(tops_images.size());
        tops_carousel.setImageListener(tops_Listener);

        bottoms_carousel.setPageCount(bottoms_images.size());
        bottoms_carousel.setImageListener(bottoms_Listener);

        shoes_carousel.setPageCount(shoes_images.size());
        shoes_carousel.setImageListener(shoes_Listener);

    }

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
            imageView.setImageBitmap(tops_images.get(position));
        }
    };

    ImageListener bottoms_Listener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageBitmap(bottoms_images.get(position));
        }
    };

    ImageListener shoes_Listener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageBitmap(shoes_images.get(position));
        }
    };

}
