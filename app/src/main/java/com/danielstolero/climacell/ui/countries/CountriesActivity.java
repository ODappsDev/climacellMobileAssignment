package com.danielstolero.climacell.ui.countries;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.danielstolero.climacell.R;
import com.danielstolero.climacell.base.BaseActivity;

public class CountriesActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private CountriesAdapter mAdapter;
    private CountriesViewModel mViewModel;

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
    }

    private void setupSearchView() {

        mSearchView.setActivated(true);
        mSearchView.setQueryHint("Type your keyword here");
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
    }

    private void setupRecycleView() {
        mAdapter = new CountriesAdapter(this);
        mViewModel = ViewModelProviders.of(this).get(CountriesViewModel.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void subscribeUi() {
        // Update the list when the data changes
        mViewModel.getObservableCountries().observe(this, data -> mAdapter.setList(data));
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
}
