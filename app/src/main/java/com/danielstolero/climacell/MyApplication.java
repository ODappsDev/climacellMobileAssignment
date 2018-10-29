package com.danielstolero.climacell;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.danielstolero.climacell.data.DataRepository;
import com.danielstolero.climacell.data.local.db.AppDatabase;
import com.danielstolero.climacell.data.remote.ApiHelper;

/**
 * Created by Daniel Stolero on 02/05/2016.
 */
public class MyApplication extends Application {

    private final String TAG = MyApplication.class.getSimpleName();

    private static Context mContext;
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        mAppExecutors = new AppExecutors();

        Log.i(TAG, "onCreate() has been called");
    }

    public static Context getContext() {
        return mContext;
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public ApiHelper getApiHelper() {
        return ApiHelper.getInstance();
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(this, getDatabase(), getApiHelper());
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }
}
