package com.ksusha.everneticaapi.base;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ksusha.everneticaapi.R;

import java.io.IOException;

public class SetWallpaperActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);
        ImageView imageView = findViewById(R.id.zoomage);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Button set = findViewById(R.id.set);
        Intent intent = getIntent();
        String url = intent.getStringExtra("image");
        Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);
        set.setOnClickListener(view -> {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Toast.makeText(SetWallpaperActivity.this, "DONE", Toast.LENGTH_SHORT).show();
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}