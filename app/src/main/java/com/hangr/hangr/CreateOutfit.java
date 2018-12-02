package com.hangr.hangr;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateOutfit extends AppCompatActivity{
    Button saveOutfitButton;
    CarouselView tops_carousel, bottoms_carousel, shoes_carousel;
    Bitmap selectedTop, selectedBottom, selectedShoes;

    public static WardrobeItemDatabase wardrobeItemDatabase;

    // Lists that will hold different items based on their style
    List<Bitmap> tops_images = new ArrayList<>();
    List<Bitmap> bottoms_images = new ArrayList<>();
    List<Bitmap> shoes_images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_outfit);
        getSupportActionBar().setTitle("Create an Outfit");

        // Build database
        wardrobeItemDatabase = Room.databaseBuilder(getApplicationContext(), WardrobeItemDatabase.class, "itemsdb.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        // Get all items in database
        List<WardrobeItem> items = CreateOutfit.wardrobeItemDatabase.wardrobeItemDao().getItems();

        // Add each item's image to the correct list
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

        // Save the outfit to a file by merging the 3 selected images on top of each other into 1 image
        saveOutfitButton = findViewById(R.id.save_outfit_button);
        saveOutfitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the images currently selected within the CarouselViews
                selectedTop = tops_images.get(tops_carousel.getCurrentItem());
                selectedBottom = bottoms_images.get(bottoms_carousel.getCurrentItem());
                selectedShoes = shoes_images.get(shoes_carousel.getCurrentItem());

                // Initialise the file that the outfit will be saved to
                File file = getOutfitFile();
                System.out.println("File: " + file);

                // Merge the 3 images into 1 image
                Bitmap mergedOutfit = mergeMultiple(new Bitmap[]{selectedTop, selectedBottom, selectedShoes});

                // Save the outfit image
                try (FileOutputStream out = new FileOutputStream(file)) {
                    mergedOutfit.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Toast.makeText(CreateOutfit.this, "Saved your outfit!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Change activity to view the outfits you've made
                openSavedOutfits();
            }
        });

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

    // When run opens View Outfits activity.
    public void openSavedOutfits() {
        Intent intent = new Intent(this, ViewOutfits.class);
        startActivity(intent);
    }

    // When run opens start up activity.
    public void goBack() {
        Intent intent = new Intent(this, StartUp.class);
        startActivity(intent);
    }


    // Listeners which set the images contained within each CarouselView
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


    private File getOutfitFile() {
        // Creates folder and file to save outfit image into

        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Outfits");

        if (!folder.exists()) {
            folder.mkdir();
        }

        // Create collision resistant filename using timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
        String imageFileName = "HangrOutfit_" + timeStamp + ".jpg";

        File image_file = new File(folder, imageFileName);
        return image_file;
    }

    private Bitmap mergeMultiple(Bitmap[] parts){
        // Merges 3 bitmaps together (vertically stacked) to create one single outfit bitmap

        // Create blank canvas with the right scale which bitmaps will be placed onto
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth(), parts[0].getHeight() + parts[1].getHeight() + parts[2].getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        int top = 0; // number of pixels down from the top of the canvas to add the bitmap

        // Draw bitmaps below each other
        // I get a weird error trying to increase height of canvas in a loop, manually adding for now
        canvas.drawBitmap(parts[0], 0, 0, paint);
        top += parts[0].getHeight();
        canvas.drawBitmap(parts[1], 0, top, paint);
        top += parts[1].getHeight();
        canvas.drawBitmap(parts[2], 0, top, paint);

        return result;
    }

}
