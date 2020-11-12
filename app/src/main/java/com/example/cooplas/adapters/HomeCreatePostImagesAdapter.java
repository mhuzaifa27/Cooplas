package com.example.cooplas.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cooplas.R;
import com.example.cooplas.activities.home.EditPostActivity;
import com.example.cooplas.models.home.MediaTypeModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class HomeCreatePostImagesAdapter extends RecyclerView.Adapter<HomeCreatePostImagesAdapter.MyViewHolder> {

    private ArrayList<MediaTypeModel> dataArrayList;
    private Activity activity;
    private int isCommunity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView user_image, cancel_image;

        public MyViewHolder(View view) {

            super(view);
            user_image = view.findViewById(R.id.user_image);
            cancel_image = view.findViewById(R.id.cancel_image);


        }
    }

    public HomeCreatePostImagesAdapter(Activity activity, ArrayList<MediaTypeModel> dataArrayList, int isCommunity) {
        this.dataArrayList = dataArrayList;
        this.activity = activity;
        this.isCommunity = isCommunity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image_selection, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MediaTypeModel model = dataArrayList.get(position);
        Log.d("imageLink", model + "");


        if (model.getPath().contains("mp4")) {


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.isMemoryCacheable();
            String videopath = model.getPath();
            Glide.with(activity).setDefaultRequestOptions(requestOptions).load(videopath).into(holder.user_image);

        } else {

            String imagePath = model.getPath();
            if (imagePath.contains("http")) {

                Picasso.get().load(imagePath).fit().centerCrop().into(holder.user_image);

            } else {

                File imgFile = new File(model.getPath());
                if (imgFile.exists()) {

                    Picasso.get().load(imgFile).fit().centerCrop().into(holder.user_image);
                }

            }


        }
//        holder.addCon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new CommunityImagePickEvent());
//            }
//        });
        holder.cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imagePath = model.getPath();
                if (imagePath.contains("http")) {

                    EditPostActivity.stringBuilder.append(model.getId() + ",");
                    dataArrayList.remove(position);
                    notifyDataSetChanged();
                } else {

                    dataArrayList.remove(position);
                    notifyDataSetChanged();

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }
}
