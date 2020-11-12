package com.example.cooplas.adapters.Food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.models.Food.ForYou;
import com.example.cooplas.utils.ShowDialogues;

import java.util.List;

public class FoodForYouAdapter extends RecyclerView.Adapter<FoodForYouAdapter.TravelBuddyViewHolder> {
    private List<ForYou> data;
    private Context context;
    private ICLicks icLicks;
    private boolean isLoadingAdded;

    public interface ICLicks{
        void onFavouriteClick(View view, ForYou forYou, int img_favourite);
    }
    public void OnClickListener(ICLicks icLicks){
        this.icLicks=icLicks;
    }

    public FoodForYouAdapter(List<ForYou> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(ForYou mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<ForYou> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(ForYou city) {
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
        add(new ForYou());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        ForYou item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ForYou getItem(int position) {
        return data.get(position);
    }


    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_food_for_you, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        ForYou foodForYou = data.get(position);

        holder.tv_food_name.setText(foodForYou.getName());
        holder.tv_restaurant_name.setText(foodForYou.getRestaurantName());
        holder.tv_price.setText("$"+foodForYou.getPrice());
        holder.rb_rating.setRating(Float.parseFloat(foodForYou.getStars()));
        Glide
                .with(context)
                .load(foodForYou.getCoverPic())
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_place_holder_image)
                .into(holder.img_food);

        if(foodForYou.getIsFavourite().equalsIgnoreCase("1"))
            holder.img_favourite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fill_heart));
        else
            holder.img_favourite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_empty_heart));

        holder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(icLicks!=null){
                    icLicks.onFavouriteClick(view,foodForYou,position);
                }
            }
        });
        holder.img_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogues.SHOW_ADD_TO_CART_DIALOG(foodForYou,context);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_food_name, tv_restaurant_name, tv_price;
        ImageView img_food,img_favourite;
        RatingBar rb_rating;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_food_name=itemView.findViewById(R.id.tv_food_name);
            tv_restaurant_name=itemView.findViewById(R.id.tv_restaurant_name);
            tv_price=itemView.findViewById(R.id.tv_price);

            img_favourite=itemView.findViewById(R.id.img_favourite);
            img_food=itemView.findViewById(R.id.img_food);

            rb_rating=itemView.findViewById(R.id.rb_rating);
        }
    }
}
