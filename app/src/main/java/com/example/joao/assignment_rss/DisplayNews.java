package com.example.joao.assignment_rss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayNews extends AppCompatActivity {

    TextView newsTitle;
    TextView newsSubtitle;
    Intent intent;
    Button btnTest;
    SharedPreferences mainActivitySharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_news);

        mainActivitySharedPref = getSharedPreferences("general_shared_pref",MODE_PRIVATE);

        newsTitle = findViewById(R.id.tv_disp_news_title);
        newsSubtitle = findViewById(R.id.tv_dis_news_subtitle);
        intent = getIntent();

        newsTitle.setText(Html.fromHtml(intent.getStringExtra("title"),Html.FROM_HTML_MODE_LEGACY));
        newsSubtitle.setText(Html.fromHtml(intent.getStringExtra("subtitle"),Html.FROM_HTML_MODE_LEGACY));

        btnTest= findViewById(R.id.btn_view_full_art);
        if (mainActivitySharedPref.getBoolean("isEnableRedirection",false)){
            btnTest.setVisibility(View.VISIBLE);
        }
        else{
            btnTest.setVisibility(View.GONE);
        }

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
