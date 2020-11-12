package com.example.cooplas.adapters.Food;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.activities.Food.RestaurantDetailActivity;
import com.example.cooplas.models.Food.PopularRestaurant;
import com.example.cooplas.utils.AppConstants;
import com.example.cooplas.utils.Constants;

import java.util.List;

public class FoodPopularRestaurantAdapter extends RecyclerView.Adapter<FoodPopularRestaurantAdapter.TravelBuddyViewHolder> {
    private List<PopularRestaurant> data;
    private Context context;
    private boolean isLoadingAdded;

    public FoodPopularRestaurantAdapter(List<PopularRestaurant> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(PopularRestaurant mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<PopularRestaurant> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(PopularRestaurant city) {
        int position = data.indexOf(city);
        if (position > -1) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PopularRestaurant());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        PopularRestaurant item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PopularRestaurant getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food_popular_restaurants, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        PopularRestaurant popularRestaurant = data.get(position);

        holder.tv_restaurant_name.setText(popularRestaurant.getName());
        holder.tv_restaurant_address.setText(popularRestaurant.getLocationName());
        holder.tv_reviews_count.setText("("+popularRestaurant.getReviewCount()+")");
        holder.tv_rating.setText(popularRestaurant.getStars());
        Glide
                .with(context)
                .load(popularRestaurant.getCoverPic())
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_place_holder_image)
                .into(holder.img_restaurant);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,RestaurantDetailActivity.class);
                intent.putExtra(Constants.ID,popularRestaurant.getId().toString());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_restaurant_name, tv_restaurant_address, tv_reviews_count,tv_rating;
        ImageView img_restaurant;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_restaurant_name=itemView.findViewById(R.id.tv_restaurant_name);
            tv_restaurant_address=itemView.findViewById(R.id.tv_restaurant_address);
            tv_reviews_count=itemView.findViewById(R.id.tv_reviews_count);
            tv_rating=itemView.findViewById(R.id.tv_rating);

            img_restaurant=itemView.findViewById(R.id.img_restaurant);
        }
    }
}
