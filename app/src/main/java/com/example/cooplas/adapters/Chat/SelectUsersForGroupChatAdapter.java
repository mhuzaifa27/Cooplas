package com.example.cooplas.adapters.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectUsersForGroupChatAdapter extends RecyclerView.Adapter<SelectUsersForGroupChatAdapter.TravelBuddyViewHolder> {
    private List<User> data;
    private Context context;
    private boolean isLoadingAdded;
    private IClicks iClicks;

    public interface IClicks{
        void onCheckChangeListener(boolean value,User user);
    }
    public void setOnCheckedChangeListener(IClicks iClicks){
        this.iClicks=iClicks;
    }

    public SelectUsersForGroupChatAdapter(List<User> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(User mc) {
        data.add(0, mc);
        if (data.size() > 1)
            notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public void addAll(List<User> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(User
                               city) {
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
        add(new User());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        User item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public User
    getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user_for_group_chat, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        User user = data.get(position);

        holder.tv_user_name.setText(user.getUserName());
        Glide
                .with(context)
                .load(user.getImage())
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_dummy_user)
                .into(holder.img_user);

        holder.cb_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(iClicks!=null){
                    iClicks.onCheckChangeListener(b,user);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name;
        CircleImageView img_user;
        CheckBox cb_user;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_user_name=itemView.findViewById(R.id.tv_user_name);
            img_user=itemView.findViewById(R.id.img_user);
            cb_user=itemView.findViewById(R.id.cb_user);
        }
    }
}
