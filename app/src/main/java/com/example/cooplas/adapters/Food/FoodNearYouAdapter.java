package com.example.cooplas.adapters.Food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.models.Food.NearYou;

import java.util.List;

public class FoodNearYouAdapter extends RecyclerView.Adapter<FoodNearYouAdapter.TravelBuddyViewHolder> {
    private List<NearYou> data;
    Context context;
    private boolean isLoadingAdded;

    public FoodNearYouAdapter(List<NearYou> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(NearYou mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<NearYou> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(NearYou city) {
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
        add(new NearYou());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        NearYou item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public NearYou getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food_near_you, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        NearYou foodNearYou = data.get(position);
        holder.tv_food_name.setText(foodNearYou.getName());
        holder.tv_distance.setText(foodNearYou.getDistance());
        Glide
                .with(context)
                .load(foodNearYou.getCoverPic())
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_place_holder_image)
                .into(holder.img_food);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_food_name, tv_distance;
        ImageView img_food;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_food_name=itemView.findViewById(R.id.tv_food_name);
            tv_distance=itemView.findViewById(R.id.tv_distance);

            img_food=itemView.findViewById(R.id.img_food);
        }
    }
}
