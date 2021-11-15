package com.ksusha.everneticaapi.base;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ksusha.everneticaapi.R;

import java.io.IOException;

public class SetWallpaperActivity extends AppCompatActivity {
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 0.1f;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);
        imageView = findViewById(R.id.zoom);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Button set = findViewById(R.id.set);
        Intent intent = getIntent();
        String url = intent.getStringExtra("image");

        //int position = intent.getIntExtra("image", -1);





        Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);


        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 1.1f));

        set.setOnClickListener(view -> {
            Toast.makeText(SetWallpaperActivity.this, "DONE", Toast.LENGTH_SHORT).show();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            try {

                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }



    // this redirects all touch events in the activity to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }

private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    // when a scale gesture is detected, use it to resize the image
    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector){
        mScaleFactor *= scaleGestureDetector.getScaleFactor();
        imageView.setScaleX(mScaleFactor);
        imageView.setScaleY(mScaleFactor);
        return true;
    }
}

}