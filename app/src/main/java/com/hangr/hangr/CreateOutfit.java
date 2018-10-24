package com.hangr.hangr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.w3c.dom.Text;

public class CreateOutfit extends AppCompatActivity{

    CarouselView tops_carousel;

    int[] tops_images = {R.drawable.ic_launcher_background};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_outfit);

        TextView tops_text = findViewById(R.id.tops_text);
        tops_carousel = (CarouselView) findViewById(R.id.tops_carousel);
        tops_carousel.setPageCount(tops_images.length);
        tops_carousel.setImageListener(tops_Listener);
    }

    ImageListener tops_Listener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(tops_images[position]);
        }
    };
}
