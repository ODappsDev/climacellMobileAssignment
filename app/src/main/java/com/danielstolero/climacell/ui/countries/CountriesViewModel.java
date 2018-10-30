package com.danielstolero.climacell.ui.countries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.danielstolero.climacell.MyApplication;
import com.danielstolero.climacell.data.model.Country;

import java.util.List;

public class CountriesViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Country>> mObservableCountries;

    public CountriesViewModel(@NonNull Application application) {
        super(application);

        mObservableCountries = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableCountries.setValue(null);

        LiveData<List<Country>> countries = ((MyApplication) application).getRepository().getCounties();

        // observe the changes of the taxis from the database and forward them
        mObservableCountries.addSource(countries, mObservableCountries::setValue);
    }

    public void getCountries() {
        MyApplication application = getApplication();
        application.getAppExecutors().diskIO().execute(() -> {
            List<Country> list = application.getDatabase().countryDao().loadAll();

            // TODO - Need check last update.
            if (list != null && list.size() > 0) {
                mObservableCountries.postValue(list);
            } else {
                application.getRepository().getCountiesByApi();
            }
        });
    }

    public MediatorLiveData<List<Country>> getObservableCountries() {
        return mObservableCountries;
    }
}
