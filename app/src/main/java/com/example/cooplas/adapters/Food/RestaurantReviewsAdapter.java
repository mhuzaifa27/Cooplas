package com.example.cooplas.adapters.Food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;

import java.util.List;

public class RestaurantReviewsAdapter extends RecyclerView.Adapter<RestaurantReviewsAdapter.TravelBuddyViewHolder> {
    private List<String> data;
    Context context;

    public RestaurantReviewsAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_restaurant_review, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        String s = data.get(position);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_subject, tv_body, tv_date;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);

        }
    }
}
