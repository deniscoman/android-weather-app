package com.example.android.sunshine.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.sunshine.data.SunshineRepository;
import com.example.android.sunshine.data.database.WeatherEntry;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final LiveData<List<WeatherEntry>> mWeatherList;
    private final SunshineRepository mRepository;

    MainActivityViewModel(SunshineRepository repository) {
        mRepository = repository;
        mWeatherList = mRepository.getFutureWeatherListByDate();
    }

    public LiveData<List<WeatherEntry>> getWeatherList() {
        return mWeatherList;
    }
}
