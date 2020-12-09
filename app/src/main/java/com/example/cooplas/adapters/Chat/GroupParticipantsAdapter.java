package com.example.cooplas.adapters.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.UserService;
import com.example.cooplas.R;
import com.example.cooplas.activities.Chat.SingleMessagingActivity;
import com.example.cooplas.utils.Constants;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupParticipantsAdapter extends RecyclerView.Adapter<GroupParticipantsAdapter.TravelBuddyViewHolder> {
    private List<String> data;
    private Context context;
    private boolean isLoadingAdded;

    public GroupParticipantsAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(String mc) {
        data.add(0, mc);
        if (data.size() > 1)
            notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public void addAll(List<String> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(String
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
        add(new String());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        String item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public String
    getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user_image, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        String path = data.get(position);

        UserService userService = new UserService();
        userService.setOnChangedListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
               User user = userService.getUserById(path);
                Glide
                        .with(context)
                        .load(user.getImage())
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_dummy_user)
                        .into(holder.img_user);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
       CircleImageView img_user;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            img_user=itemView.findViewById(R.id.img_user);
        }
    }
}
