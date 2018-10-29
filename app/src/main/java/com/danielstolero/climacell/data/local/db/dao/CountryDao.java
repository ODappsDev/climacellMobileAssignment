package com.danielstolero.climacell.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.danielstolero.climacell.data.model.Country;

import java.util.List;

@Dao
public interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Country option);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Country> options);

    @Query("SELECT * FROM countries")
    List<Country> loadAll();

    @Query("SELECT * FROM countries")
    LiveData<List<Country>> loadAllCountries();
}
