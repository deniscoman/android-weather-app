/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sunshine.R;
import com.example.android.sunshine.data.database.ListWeatherEntry;
import com.example.android.sunshine.data.database.WeatherEntry;
import com.example.android.sunshine.databinding.ActivityDetailBinding;
import com.example.android.sunshine.utilities.InjectorUtils;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import java.util.Date;

/**
 * Displays single day's forecast
 */
public class DetailActivity extends AppCompatActivity {

    private DetailActivityViewModel mViewModel;

    public EntityPresenter mEntityPresenter;

    public static final String WEATHER_ID_EXTRA = "WEATHER_ID_EXTRA";
    public static final String LIST_POSITION_EXTRA = "LIST_POSITION_EXTRA";

    /*
     * This field is used for data binding. Normally, we would have to call findViewById many
     * times to get references to the Views in this Activity. With data binding however, we only
     * need to call DataBindingUtil.setContentView and pass in a Context and a layout, as we do
     * in onCreate of this class. Then, we can access all of the Views in our layout
     * programmatically without cluttering up the code with findViewById.
     */
    private ActivityDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEntityPresenter = new EntityPresenter(getApplicationContext());
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        long timestamp = getIntent().getLongExtra(WEATHER_ID_EXTRA, -1);

        Date date = new Date(timestamp);

        DetailViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext(), date);

        mViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);


        mViewModel.getWeather().observe(this, weatherEntry -> {
            if (weatherEntry != null) {
                mEntityPresenter.buildFromWeatherEntry(weatherEntry);
                mDetailBinding.primaryInfo.setWeatherPresenter(mEntityPresenter);
                mDetailBinding.extraDetails.setWeatherPresenter(mEntityPresenter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        int position = getIntent().getIntExtra(LIST_POSITION_EXTRA, -1);
        intent.putExtra(LIST_POSITION_EXTRA, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class EntityPresenter {
        private Context context;
        private ObservableField<String> highTemperature = new ObservableField<>();
        private ObservableField<String> lowTemperature = new ObservableField<>();
        private ObservableField<String> description = new ObservableField<>();
        private ObservableField<Integer> imageId = new ObservableField<>();
        private ObservableField<String> date = new ObservableField<>();
        private ObservableField<String> humidity = new ObservableField<>();
        private ObservableField<String> wind = new ObservableField<>();
        private ObservableField<String> pressure = new ObservableField<>();

        public EntityPresenter(Context context) {
            this.context = context;
        }

        public void buildFromWeatherEntry(WeatherEntry entry) {

            int weatherId = entry.getWeatherIconId();

            String maxTemperature = SunshineWeatherUtils.formatTemperature(context, entry.getMax());
            highTemperature.set(maxTemperature);

            String weatherDescription = SunshineWeatherUtils.getStringForWeatherCondition(context, weatherId);
            description.set(weatherDescription);

            Integer weatherImageId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);
            imageId.set(weatherImageId);

            String dateText = SunshineDateUtils.getFriendlyDateString(context, entry.getDate().getTime(), true);
            date.set(dateText);

            String minTemperature = SunshineWeatherUtils.formatTemperature(context, entry.getMin());
            lowTemperature.set(minTemperature);

            humidity.set(String.valueOf(entry.getHumidity()));

            double windSpeed = entry.getWind();
            double windDirection = entry.getDegrees();
            String weatherWind = SunshineWeatherUtils.getFormattedWind(DetailActivity.this, windSpeed, windDirection);
            wind.set(weatherWind);

            pressure.set(getString(R.string.format_pressure, entry.getPressure()));
        }

        public ObservableField<String> getHighTemperature() { return highTemperature; }

        public ObservableField<String> getDate() {
            return date;
        }

        public ObservableField<String> getDescription() {
            return description;
        }

        public ObservableField<String> getHumidity() {
            return humidity;
        }

        public ObservableField<Integer> getImageId() {
            return imageId;
        }

        public ObservableField<String> getLowTemperature() {
            return lowTemperature;
        }

        public ObservableField<String> getPressure() {
            return pressure;
        }

        public ObservableField<String> getWind() {
            return wind;
        }
    }
}