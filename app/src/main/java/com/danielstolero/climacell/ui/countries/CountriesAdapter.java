package com.danielstolero.climacell.ui.countries;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel Stolero on 30/10/2018.
 */
public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> implements View.OnClickListener, Filterable {

    private final String TAG = CountriesAdapter.class.getSimpleName();

    private Context mContext;
    private List<Country> mList;
    private List<Country> mListFiltered;

    public CountriesAdapter(Context context) {
        mContext = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one mView per item, and
    // you provide access to all the views for a data item in a mView holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView txt;
        //TextView btnConnect;
        private ImageView img;

        public ViewHolder(View view) {
            super(view);

            txt = view.findViewById(R.id.textView);
            img = view.findViewById(R.id.imageView);
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
        Country country = mListFiltered.get(position);
        holder.txt.setText(String.format("%S, (%S)", country.getCapital(), country.getName()));

        SvgLoader.pluck()
                .with((BaseActivity)mContext)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(country.getFlag(), holder.img);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mListFiltered != null ? mListFiltered.size() : 0;
    }

    @Override
    public void onClick(View v) {

    }

    public void setList(@Nullable final List<Country> data) {
        if(data != null) {
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
}
