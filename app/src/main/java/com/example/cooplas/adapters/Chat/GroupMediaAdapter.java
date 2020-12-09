package com.example.cooplas.adapters.Chat;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.cooplas.Firebase.Models.MediaModel;
import com.example.cooplas.R;
import com.example.cooplas.activities.home.ImageViewActivity;
import com.example.cooplas.activities.home.VideoViewActivity;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MediaModel> data;
    private Context context;
    private boolean isLoadingAdded;

    public GroupMediaAdapter(List<MediaModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(MediaModel mc) {
        data.add(0, mc);
        if (data.size() > 1)
            notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public void addAll(List<MediaModel> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(MediaModel
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
        add(new MediaModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        MediaModel item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public MediaModel
    getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_IMAGE))
            return 1;
        else if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_VIDEO))
            return 2;
        else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 1:
                view = inflater.inflate(R.layout.item_image_media, parent, false);
                holder = new ImageMediaViewHolder(view);
                break;
            case 2:
                view = inflater.inflate(R.layout.item_video_media, parent, false);
                holder = new VideoMediaViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MediaModel media = data.get(position);
        switch (holder.getItemViewType()) {
            case 1:
                ImageMediaViewHolder imageMediaViewHolder = (ImageMediaViewHolder) holder;
                Glide
                        .with(context)
                        .load(media.getPath())
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_place_holder_image)
                        .into(imageMediaViewHolder.img_media_image);
                imageMediaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> array = new ArrayList<>();
                        array.add(media.getPath());
                        Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "0");
                        context.startActivity(intent);
                    }
                });
                break;
            case 2:
                VideoMediaViewHolder videoMediaViewHolder = (VideoMediaViewHolder) holder;
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.isMemoryCacheable();
                String videopath = media.getPath();
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(videopath).into(videoMediaViewHolder.img_media_image);
                videoMediaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("videoLink", "onClick: " + media.getPath());
                        Intent intent = new Intent(context, VideoViewActivity.class);
                        intent.putExtra("videoLink", media.getPath());
                        intent.putExtra("from", "firebase");
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ImageMediaViewHolder extends RecyclerView.ViewHolder {
        ImageView img_media_image;

        public ImageMediaViewHolder(View itemView) {
            super(itemView);
            img_media_image = itemView.findViewById(R.id.img_media_image);
        }
    }

    public class VideoMediaViewHolder extends RecyclerView.ViewHolder {
        ImageView img_media_image;
        RelativeLayout rl_play;

        public VideoMediaViewHolder(View itemView) {
            super(itemView);
            img_media_image = itemView.findViewById(R.id.img_media_image);

            rl_play = itemView.findViewById(R.id.rl_play);
        }
    }

}
