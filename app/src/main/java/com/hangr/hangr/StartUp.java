package com.hangr.hangr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class StartUp extends AppCompatActivity {
    private Button button;
    private Button outfitButton;
//    private Button aalasaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_up);

        hideNavigationBar();
        button = findViewById(R.id.newItemButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewItemActivity();
            }
        });

//        aalasaButton = findViewById(R.id.aalasaButton);
//        aalasaButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCreateOutfitActivity();
//            }
//        });

        outfitButton = findViewById(R.id.outfitButton);
        outfitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateOutfitActivity();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        hideNavigationBar();

    }

    private void hideNavigationBar(){
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    public void openNewItemActivity() {
        Intent intent = new Intent(this, NewItem.class);
        startActivity(intent);
    }

    public void openCreateOutfitActivity() {
        Intent intent = new Intent(this, CreateOutfit.class);
        startActivity(intent);
    }
}
