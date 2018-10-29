package com.danielstolero.climacell.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danielstolero.climacell.MyApplication;
import com.danielstolero.climacell.data.model.Country;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Country>> mObservableCountries;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mObservableCountries = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableCountries.setValue(null);

        LiveData<List<Country>> countries = ((MyApplication) application).getRepository().getCounties();

        // observe the changes of the taxis from the database and forward them
        mObservableCountries.addSource(countries, mObservableCountries::setValue);
    }

    public void doApiCallForCountries() {
        MyApplication application = getApplication();
        application.getRepository().getCountiesByApi();
    }
}
