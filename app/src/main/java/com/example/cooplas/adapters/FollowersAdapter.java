package com.example.cooplas.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.cooplas.R;
import com.example.cooplas.models.home.homeLikesModels.Like;
import com.example.cooplas.models.profile.Followers.FollowersModel;
import com.example.cooplas.models.profile.Following.Following;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersAdapter extends Adapter<FollowersAdapter.ViewHolder> {
    private List<Following> data;
    private Activity activity;

    public FollowersAdapter(Activity activity, List<Following> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_follow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Following model = data.get(position);


        String userID = String.valueOf(model.getId());
        String currentID = FunctionsKt.getUserID(activity);


        String followVal = model.getIsFollowing();
        if (followVal.equalsIgnoreCase("1")) {
            holder.follow_tv.setText(activity.getResources().getString(R.string.unfollow));
            holder.follow_tv.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            holder.follow_tv.setBackground(activity.getResources().getDrawable(R.drawable.bg_unfollow));
        } else {
            holder.follow_tv.setText(activity.getResources().getString(R.string.follow));
            holder.follow_tv.setTextColor(activity.getResources().getColor(R.color.white));
            holder.follow_tv.setBackground(activity.getResources().getDrawable(R.drawable.bf_follow));
        }
//
        holder.follow_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String followVal = model.getIsFollowing();
                if (followVal.equalsIgnoreCase("1")) {
                    //make it unfollow

                    String id = String.valueOf(model.getId());
                    model.setIsFollowing("0");
                    unfollow(id);
                } else {
                    //make it Follow
                    String id = String.valueOf(model.getId());
                    model.setIsFollowing("1");
                    follow(id);
                }


                notifyItemChanged(position);
            }
        });


        String userName = model.getFirstName() + " " + model.getLastName();
        String userImage = model.getProfilePic();
        holder.tv_name.setText(userName);
        Picasso.get().load(userImage).fit().centerCrop().placeholder(R.drawable.image_placeholder).into(holder.iv_profile);


        if (userID.equalsIgnoreCase(currentID)) {
            holder.follow_tv.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, follow_tv;
        private ImageView iv_profile;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);

            tv_name = itemView.findViewById(R.id.tv_name);
            follow_tv = itemView.findViewById(R.id.follow_tv);


        }
    }

    private void follow(String userID) {
        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();

        String accessToken = FunctionsKt.getAccessToken(activity);
        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.followUserHome("Bearer " + accessToken, String.valueOf(userID));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("follow", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    private void unfollow(String userID) {
        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();

        String accessToken = FunctionsKt.getAccessToken(activity);
        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.UnFollowUserHome("Bearer " + accessToken, String.valueOf(userID));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("follow", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

}
