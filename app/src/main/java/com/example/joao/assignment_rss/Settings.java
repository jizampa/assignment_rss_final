package com.example.joao.assignment_rss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Switch upperCaseSwitch;
    private Switch isBtVisible;
    private CheckBox enableRedirection;
    private TextView number_of_news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("general_shared_pref",MODE_PRIVATE);

        upperCaseSwitch = findViewById(R.id.switch_Upper);
        upperCaseSwitch.setChecked(sharedPreferences.getBoolean("toUpper",false));

        isBtVisible = findViewById(R.id.switch_description);
        isBtVisible.setChecked(sharedPreferences.getBoolean("isBtVisible", false));

        enableRedirection = findViewById(R.id.checkBox_btn_redirection);
        enableRedirection.setChecked(sharedPreferences.getBoolean("isEnableRedirection",false));

        number_of_news =findViewById(R.id.txt_number_of_news);
       // number_of_news.setText(sharedPreferences.getInt("numberOfNews","20"));





    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("toUpper", upperCaseSwitch.isChecked());
        editor.putBoolean("isBtVisible", isBtVisible.isChecked());
        editor.putBoolean("isEnableRedirection", enableRedirection.isChecked());
        try {
            if (Integer.parseInt(number_of_news.getText().toString()) > 0) {
                editor.putInt("numberOfNews", Integer.parseInt(number_of_news.getText().toString()));
            }

        }catch (NumberFormatException e){}

        editor.apply();


    }

}
