package com.example.android.sunshine.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.sunshine.data.SunshineRepository;
import com.example.android.sunshine.data.database.ListWeatherEntry;
import com.example.android.sunshine.data.database.WeatherEntry;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final LiveData<List<ListWeatherEntry>> mWeatherList;
    private final SunshineRepository mRepository;

    MainActivityViewModel(SunshineRepository repository) {
        mRepository = repository;
        mWeatherList = mRepository.getFutureWeatherListByDate();
    }

    public LiveData<List<ListWeatherEntry>> getWeatherList() {
        return mWeatherList;
    }
}
