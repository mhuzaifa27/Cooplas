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
import com.example.cooplas.models.Food.Category;

import java.util.List;

public class FoodCategoriesAdapter extends RecyclerView.Adapter<FoodCategoriesAdapter.TravelBuddyViewHolder> {
    private List<Category> data;
    Context context;
    private boolean isLoadingAdded;

    public FoodCategoriesAdapter(List<Category> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(Category mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<Category> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(Category city) {
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
        add(new Category());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        Category item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public Category getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food_category, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        Category category = data.get(position);

        holder.tv_category_name.setText(category.getName());
        Glide
                .with(context)
                .load(category.getCoverPic())
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_place_holder_image)
                .into(holder.img_category);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_category_name;
        ImageView img_category;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_category_name=itemView.findViewById(R.id.tv_category_name);

            img_category=itemView.findViewById(R.id.img_category);
        }
    }
}
