package com.example.cooplas.adapters.Food;

import android.content.Context;
import android.media.Rating;
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
import com.example.cooplas.models.Food.Menu;
import com.example.cooplas.models.Food.Menu;
import com.example.cooplas.utils.ShowDialogues;

import java.util.List;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.TravelBuddyViewHolder> {
    private List<Menu> data;
    private Context context;
    private ICLicks icLicks;
    private boolean isLoadingAdded;

    public interface ICLicks{
        void onFavouriteClick(View view, Menu forYou, int img_favourite);
    }
    public void OnClickListener(ICLicks icLicks){
        this.icLicks=icLicks;
    }

    public void add(Menu mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<Menu> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(Menu city) {
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
        add(new Menu());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        Menu item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Menu getItem(int position) {
        return data.get(position);
    }

    public RestaurantMenuAdapter(List<Menu> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_resataurant_menu, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        Menu menu = data.get(position);

        if (menu.getName() != null)
            holder.tv_food_name.setText(menu.getName());
        if (menu.getPrice() != null)
            holder.tv_price.setText("$" + menu.getPrice());
        if (menu.getStars() != null)
            holder.rb_rating.setRating(Float.parseFloat(menu.getStars()));

        if(menu.getIsFavourite().equalsIgnoreCase("1"))
            holder.img_favourite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fill_heart));
        else
            holder.img_favourite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_empty_heart));

        if (menu.getCoverPic() != null) {
            Glide
                    .with(context)
                    .load(menu.getCoverPic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(holder.img_food);
        }
        holder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(icLicks!=null){
                    icLicks.onFavouriteClick(view,menu,position);
                }
            }
        });
        holder.img_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogues.SHOW_ADD_TO_CART_DIALOG(menu,context);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_food_name, tv_price;
        RatingBar rb_rating;
        ImageView img_favourite, img_food;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_price = itemView.findViewById(R.id.tv_price);

            rb_rating = itemView.findViewById(R.id.rb_rating);

            img_favourite = itemView.findViewById(R.id.img_favourite);
            img_food = itemView.findViewById(R.id.img_food);

        }
    }
}
