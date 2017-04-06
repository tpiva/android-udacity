package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private TextView mShowWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mShowWeatherTextView = (TextView) findViewById(R.id.tv_weather_data_shared);

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
        Intent weatherIntent = getIntent();
        if(weatherIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String sharedWeatherText = weatherIntent.getStringExtra(Intent.EXTRA_TEXT);
            mShowWeatherTextView.setText(sharedWeatherText);
        }
    }
}