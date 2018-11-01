package com.danielstolero.climacell.ui.countries;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.danielstolero.climacell.R;
import com.danielstolero.climacell.base.BaseActivity;
import com.danielstolero.climacell.data.model.Country;
import com.danielstolero.climacell.data.model.Forecast;
import com.danielstolero.climacell.data.model.Location;
import com.danielstolero.climacell.data.model.Value;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Created by Daniel Stolero on 30/10/2018.
 */
public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> implements Filterable {

    private final String TAG = CountriesAdapter.class.getSimpleName();

    private Context mContext;
    private CountriesViewModel mViewModel;
    private List<Country> mList;
    private List<Country> mListFiltered;
    private SparseArray<List<Forecast>> forecast;
    private ExpandableLayout mExpandableLayout;

    private ViewHolder mViewHolder;
    private Country mCountry;

    public CountriesAdapter(Context context, CountriesViewModel viewModel) {
        mContext = context;
        mViewModel = viewModel;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one mView per item, and
    // you provide access to all the views for a data item in a mView holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView txt;
        private ImageView img;
        private View container, progressBar;
        private ExpandableLayout expandableLayout;
        private TextView day1, day2, day3, day4, day5;
        private MapView map;

        public ViewHolder(View view) {
            super(view);

            container = view.findViewById(R.id.container);
            txt = view.findViewById(R.id.textView);
            img = view.findViewById(R.id.imageView);
            progressBar = view.findViewById(R.id.progressBar);
            expandableLayout = view.findViewById(R.id.expandable_layout);
            map = view.findViewById(R.id.map);
            map.onCreate(null);

            initDays(view);
        }

        private void initDays(View view) {
            day1 = view.findViewById(R.id.day1);
            day2 = view.findViewById(R.id.day2);
            day3 = view.findViewById(R.id.day3);
            day4 = view.findViewById(R.id.day4);
            day5 = view.findViewById(R.id.day5);
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CountriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new mView
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        // set the mView's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a mView (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Country country = mListFiltered.get(position);
        holder.txt.setText(String.format("%S, (%S)", country.getCapital(), country.getName()));

        SvgLoader.pluck()
                .with((BaseActivity) mContext)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(country.getFlag(), holder.img);

        holder.container.setOnClickListener(v -> {
            if (mListFiltered != null && holder.getAdapterPosition() < mListFiltered.size() && holder.getAdapterPosition() > -1) {
                updateExpandableLayout(holder);
                setupForecast(country, holder);
                ((CountriesActivity) mContext).initForecast(mListFiltered.get(holder.getAdapterPosition()));
                holder.map.onResume();
                holder.map.getMapAsync(googleMap -> {

                    if(googleMap != null) {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        googleMap.getUiSettings().setZoomControlsEnabled(false);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                        Location location = country.getLocation();
                        if(location != null) {
                            LatLng newPos = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(newPos));

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, 4));
                        }
                    }
                });
                mCountry = country;
                mViewHolder = holder;
            }
        });

        setupDays(holder);
    }

    private void setupForecast(Country country, ViewHolder holder) {
        holder.progressBar.setVisibility(View.VISIBLE);
        mViewModel.loadForecast(country);
    }

    private void setupDays(ViewHolder holder) {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd.MM", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance();
        holder.day1.setText(simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        holder.day2.setText(simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        holder.day3.setText(simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        holder.day4.setText(simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        holder.day5.setText(simpleDateFormat.format(calendar.getTime()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mListFiltered != null ? mListFiltered.size() : 0;
    }

    public void setList(@Nullable final List<Country> data, View view) {
        if (data != null) {
            view.setVisibility(View.GONE);
            if (mList == null) {
                mList = data;
                mListFiltered = data;
                Collections.sort(mList);
                notifyItemRangeInserted(0, data.size());
            } else {
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return mList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return data.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return data.get(oldItemPosition).getId() == data.get(newItemPosition).getId();
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        Country newItem = data.get(newItemPosition);
                        Country oldItem = mList.get(oldItemPosition);
                        return newItem.getId() == oldItem.getId();
                    }
                });
                mList = data;
                mListFiltered = data;
                Collections.sort(mList);
                result.dispatchUpdatesTo(this);
            }
        } else {
            Log.d(TAG, "Empty list.");
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mListFiltered = mList;
                } else {
                    List<Country> filteredList = new ArrayList<>();
                    for (Country row : mList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCapital().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListFiltered = (ArrayList<Country>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void updateExpandableLayout(ViewHolder holder) {
        if (mExpandableLayout != null && mExpandableLayout != holder.expandableLayout) {
            mExpandableLayout.collapse();
        }
        mExpandableLayout = holder.expandableLayout;
        mExpandableLayout.toggle();
    }

    public void updateForecast(SparseArray<List<Forecast>> data) {
        if (mViewHolder != null && mViewHolder.progressBar != null) {
            mViewHolder.progressBar.setVisibility(View.GONE);

            List<Forecast> forecasts = data.get(mCountry.getId());
            if (forecasts != null) {
                for (int i = 0; i < forecasts.size(); i++) {
                    Forecast forecast = forecasts.get(i);

                    // Min temp
                    int minTempId = mContext.getResources().getIdentifier("minTemp" + (i + 1), "id", mContext.getPackageName());
                    Value minTemp = Objects.requireNonNull(forecast.getTemp().get("min")).getValue();
                    ((TextView) mViewHolder.itemView.findViewById(minTempId)).setText(String.format(Locale.US, "%.1f %s", minTemp.getValue(), minTemp.getUnits()));

                    // Max temp
                    int maxTempId = mContext.getResources().getIdentifier("maxTemp" + (i + 1), "id", mContext.getPackageName());
                    Value maxTemp = Objects.requireNonNull(forecast.getTemp().get("max")).getValue();
                    ((TextView) mViewHolder.itemView.findViewById(maxTempId)).setText(String.format(Locale.US, "%.1f %s", maxTemp.getValue(), maxTemp.getUnits()));

                    // Max precipitation
                    int precipitationId = mContext.getResources().getIdentifier("precipitation" + (i + 1), "id", mContext.getPackageName());
                    Value precipitation = Objects.requireNonNull(forecast.getPrecipitation().get("max")).getValue();
                    ((TextView) mViewHolder.itemView.findViewById(precipitationId)).setText(String.format(Locale.US, "%.1f\n%s", precipitation.getValue(), precipitation.getUnits()));
                }
            }
        }
    }
}
