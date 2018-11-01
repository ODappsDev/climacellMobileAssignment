package com.danielstolero.climacell.ui.countries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.danielstolero.climacell.MyApplication;
import com.danielstolero.climacell.data.model.Country;
import com.danielstolero.climacell.data.model.Forecast;

import java.util.List;

public class CountriesViewModel extends AndroidViewModel {

    private MediatorLiveData<List<Country>> mObservableCountries;
    private MediatorLiveData<SparseArray<List<Forecast>>> mObservableForecast;

    public CountriesViewModel(@NonNull Application application) {
        super(application);

        mObservableCountries = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableCountries.setValue(null);

        LiveData<List<Country>> countries = ((MyApplication) application).getRepository().getCounties();

        // observe the changes of the taxis from the database and forward them
        mObservableCountries.addSource(countries, mObservableCountries::setValue);

        mObservableForecast = new MediatorLiveData<>();
        mObservableForecast.setValue(new SparseArray<>());

        LiveData<SparseArray<List<Forecast>>> forecasts = ((MyApplication) application).getRepository().getObservableForecast();
        mObservableForecast.addSource(forecasts, mObservableForecast::setValue);
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

    public MediatorLiveData<SparseArray<List<Forecast>>> getObservableForecast() {
        return mObservableForecast;
    }

    public void loadForecast(Country country) {
        // TODO
        MyApplication application = getApplication();

        // TODO - Need check last update.
        if (mObservableForecast != null &&
                mObservableForecast.getValue() != null &&
                mObservableForecast.getValue().get(country.getId()) != null) {
            mObservableForecast.postValue(mObservableForecast.getValue());
        } else {
            application.getRepository().getForecastByApi(country);
        }
    }
}
