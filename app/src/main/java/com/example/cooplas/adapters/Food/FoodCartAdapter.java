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
import com.example.cooplas.models.Food.CartItem;
import com.example.cooplas.models.Food.FoodItem;

import java.util.List;

public class FoodCartAdapter extends RecyclerView.Adapter<FoodCartAdapter.TravelBuddyViewHolder> {
    private List<CartItem> data;
    private Context context;
    private boolean isLoadingAdded;
    private IClicks iClicks;

    public interface IClicks{
        void onRemoveItemClick(View view, CartItem cartItem, int tv_quantity);
        void onPlusQuantityClick(View view, CartItem cartItem, int tv_quantity);
        void onMinusQuantityClick(View view, CartItem cartItem, int tv_quantity);
    }
    public void SetOnIClickListener(IClicks iClicks){
        this.iClicks=iClicks;
    }

    public FoodCartAdapter(List<CartItem> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(CartItem mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<CartItem> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(CartItem city) {
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
        add(new CartItem());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        CartItem item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public CartItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        CartItem cartItem = data.get(position);
        FoodItem item = cartItem.getFoodItem();

        if (item.getName() != null)
            holder.tv_food_name.setText(item.getName());
        if (item.getPrice() != null)
            holder.tv_price.setText("$" + item.getPrice());

        holder.tv_quantity.setText(cartItem.getQuantity());

        if (item.getCoverPic() != null) {
            Glide
                    .with(context)
                    .load(item.getCoverPic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(holder.img_food);
        }

        holder.img_remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iClicks!=null){
                    iClicks.onRemoveItemClick(view,cartItem,position);
                }
            }
        });
        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iClicks!=null){
                    iClicks.onPlusQuantityClick(view,cartItem,position);
                }
            }
        });
        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iClicks!=null){
                    iClicks.onMinusQuantityClick(view,cartItem,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_food_name, tv_price,tv_quantity;
        ImageView img_remove_item,img_food,img_plus,img_minus;
        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_food_name=itemView.findViewById(R.id.tv_food_name);
            tv_price=itemView.findViewById(R.id.tv_price);
            tv_quantity=itemView.findViewById(R.id.tv_quantity);

            img_remove_item=itemView.findViewById(R.id.img_remove_item);
            img_food=itemView.findViewById(R.id.img_food);
            img_plus=itemView.findViewById(R.id.img_plus);
            img_minus=itemView.findViewById(R.id.img_minus);
        }
    }
}
