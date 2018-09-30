package com.example.joao.assignment_rss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class WebView extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        android.webkit.WebView wv;
        wv = findViewById(R.id.WVwebview);
        intent = getIntent();

        String test = intent.getStringExtra("link");

        wv.loadUrl(intent.getStringExtra("link"));
    }
}
