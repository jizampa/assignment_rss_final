package com.example.joao.assignment_rss;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayNews extends AppCompatActivity {

    TextView newsTitle;
    TextView newsSubtitle;
    Intent intent;
    Button btnTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_news);

        newsTitle = findViewById(R.id.tv_disp_news_title);
        newsSubtitle = findViewById(R.id.tv_dis_news_subtitle);
        intent = getIntent();

        newsTitle.setText(intent.getStringExtra("title"));
        newsSubtitle.setText(intent.getStringExtra("subtitle"));

        btnTest= findViewById(R.id.button);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(DisplayNews.this, WebView.class);
                intent.putExtra("link", getIntent().getStringExtra("link"));
                startActivity(intent);
            }
        });

    }
}
