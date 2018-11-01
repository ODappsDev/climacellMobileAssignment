package com.danielstolero.climacell.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.util.SparseArray;

import com.danielstolero.climacell.data.local.db.AppDatabase;
import com.danielstolero.climacell.data.model.Country;
import com.danielstolero.climacell.data.model.Forecast;
import com.danielstolero.climacell.data.remote.ApiHelper;

import java.util.List;

/**
 * Repository handling the work with data.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final Context mContext;
    private final AppDatabase mDatabase;
    private final ApiHelper mApiHelper;

    private MediatorLiveData<List<Country>> mObservableCountries;
    private MediatorLiveData<SparseArray<List<Forecast>>> mObservableForecast;

    private DataRepository(final Context context, final AppDatabase database, ApiHelper api) {
        mContext = context;
        mDatabase = database;
        mApiHelper = api;

        mObservableCountries = new MediatorLiveData<>();
        mObservableCountries.addSource(mDatabase.countryDao().loadAllCountries(),
                countries -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableCountries.postValue(countries);
                    }
                });

        mObservableForecast = new MediatorLiveData<>();
        mObservableForecast.setValue(new SparseArray<>());
    }

    public static DataRepository getInstance(final Context context, final AppDatabase database, ApiHelper api) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(context, database, api);
                }
            }
        }
        return sInstance;
    }

    public void getCountiesByApi() {
        mApiHelper.getCountries(this);
    }

    public void getForecastByApi(Country country) {
        mApiHelper.getForecast(this, country);
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<Country>> getCounties() {
        return mObservableCountries;
    }

    public void setCountries(List<Country> data) {
        mDatabase.countryDao().insertAll(data);
    }

    public LiveData<SparseArray<List<Forecast>>> getObservableForecast() {
        return mObservableForecast;
    }

    public void setForecast(Country country, List<Forecast> data) {
        SparseArray<List<Forecast>> forecasts = mObservableForecast.getValue();
        if(forecasts != null) {
            forecasts.put(country.getId(), data);
        }
        mObservableForecast.postValue(forecasts);
    }
}
