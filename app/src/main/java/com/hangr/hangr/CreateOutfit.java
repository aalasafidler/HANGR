package com.hangr.hangr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class CreateOutfit extends AppCompatActivity{

    CarouselView tops_carousel, bottoms_carousel, shoes_carousel;

    int[] tops_images = {R.drawable.t083263large, R.drawable.t3539717726366large, R.drawable.t5397177446928large, R.drawable.t35397121456317large, R.drawable.t35397175921984large};
    int[] bottoms_images = {R.drawable.b35397177259627large, R.drawable.b5397177424742large, R.drawable.b3539717742438large};
    int[] shoes_images = {R.drawable.s3539717635451large, R.drawable.s35397172277367large, R.drawable.s35397176100838large, R.drawable.s35397176103853large, R.drawable.s35397176433110large};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_outfit);
        getSupportActionBar().setTitle("Create an Outfit");

        // Dealing with the tops
        TextView tops_text = findViewById(R.id.tops_text);
        tops_carousel = (CarouselView) findViewById(R.id.tops_carousel);
        tops_carousel.setPageCount(tops_images.length);
        tops_carousel.setImageListener(tops_Listener);

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
    //https://www.youtube.com/watch?v=kknBxoCOYXI
    @Override
            public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.outfits_menu, menu);
        return true;
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
