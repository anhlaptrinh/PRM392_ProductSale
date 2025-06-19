package com.example.productsaleprm.activity;

import android.os.Bundle;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;

public class CarouselActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_carousel);
        ImageView imageView = findViewById(R.id.image_carousel);
        Glide.with(CarouselActivity.this).load(getIntent().getStringExtra("image")).into(imageView);
    }
}
