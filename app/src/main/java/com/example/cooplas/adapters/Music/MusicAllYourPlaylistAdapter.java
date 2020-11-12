package com.example.cooplas.adapters.Music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;

import java.util.List;

public class MusicAllYourPlaylistAdapter extends RecyclerView.Adapter<MusicAllYourPlaylistAdapter.TravelBuddyViewHolder> {
    private List<String> data;
    Context context;
    private boolean isLoadingAdded;

    public MusicAllYourPlaylistAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(String mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<String> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(String city) {
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
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_music_your_all_playlists, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
       /* Category category = data.get(position);

        holder.tv_category_name.setText(category.getName());
        Glide
                .with(context)
                .load(category.getCoverPic())
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_place_holder_image)
                .into(holder.img_category);*/
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
