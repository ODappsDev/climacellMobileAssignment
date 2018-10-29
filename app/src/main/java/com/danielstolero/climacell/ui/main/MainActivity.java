package com.danielstolero.climacell.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.danielstolero.climacell.R;
import com.danielstolero.climacell.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mRecyclerView = findViewById(R.id.recyclerView);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

        //viewModel.doApiCallForCountries();
    }
}
