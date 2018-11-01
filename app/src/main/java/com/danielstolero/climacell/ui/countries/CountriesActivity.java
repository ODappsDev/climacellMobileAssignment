package com.danielstolero.climacell.ui.countries;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.danielstolero.climacell.R;
import com.danielstolero.climacell.base.BaseActivity;
import com.danielstolero.climacell.data.model.Country;
import com.google.android.gms.common.api.GoogleApiClient;

public class CountriesActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private CountriesAdapter mAdapter;
    private CountriesViewModel mViewModel;

    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

        mSearchView = findViewById(R.id.search);
        mRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void initListeners() {
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    protected void initData() {

        setupSearchView();
        setupRecycleView();
        subscribeUi();

        mViewModel.getCountries();
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setupSearchView() {
        mSearchView.setActivated(true);
        mSearchView.setQueryHint("Type your keyword here");
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
    }

    private void setupRecycleView() {
        mViewModel = ViewModelProviders.of(this).get(CountriesViewModel.class);
        mAdapter = new CountriesAdapter(this, mViewModel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void subscribeUi() {
        // Update the list when the data changes
        mViewModel.getObservableCountries().observe(this, data -> mAdapter.setList(data, progressBar));
        mViewModel.getObservableForecast().observe(this, data -> mAdapter.updateForecast(data));

        //((MyApplication) getApplication()).getRepository().getObservableForecast().observe(this, data -> mAdapter.updateForecast(data));

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(mAdapter != null) {
            mAdapter.getFilter().filter(query);
        }
        return false;
    }

    void initForecast(Country country) {
        mViewModel.loadForecast(country);
    }

    void showForecast() {
    }
}
