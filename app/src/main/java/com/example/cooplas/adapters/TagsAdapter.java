package com.example.cooplas.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.events.videos.VideosTagEvent;
import com.example.cooplas.models.home.singlePost.Comment;
import com.example.cooplas.models.videos.TagsModel;
import com.example.cooplas.utils.CircleTransform;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.android.gms.wearable.MessageEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private ArrayList<TagsModel> data;
    private Context context;
    private Activity activity;

    public TagsAdapter(ArrayList<TagsModel> data, Activity activity) {

        this.data = data;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_video_tag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TagsModel model = data.get(position);

        holder.title_tv.setText(model.getTitle());

        if (model.getIsSelected().equalsIgnoreCase("1")) {

            holder.title_tv.setBackgroundResource(R.drawable.round_orange);
            holder.title_tv.setTextColor(activity.getResources().getColor(R.color.white));
        } else {

            holder.title_tv.setBackgroundResource(R.drawable.round_grey);
            holder.title_tv.setTextColor(activity.getResources().getColor(R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String titleName = model.getTitle();

                if (titleName.equalsIgnoreCase("Recent")) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setIsSelected("0");
                    }
                    data.get(position).setIsSelected("1");
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new VideosTagEvent(titleName));
                }

                if (titleName.equalsIgnoreCase("Trending")) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setIsSelected("0");
                    }
                    data.get(position).setIsSelected("1");
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new VideosTagEvent(titleName));
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);

        }
    }

}
