package com.example.cooplas.adapters.Travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cooplas.R;
import com.example.cooplas.activities.home.EditPostActivity;
import com.example.cooplas.activities.home.HomePostLikesActivity;
import com.example.cooplas.activities.home.PostCommentActivity;
import com.example.cooplas.activities.home.VideoViewActivity;
import com.example.cooplas.adapters.HomeFeedAdapter;
import com.example.cooplas.adapters.TagsAdapter;

import com.example.cooplas.events.videos.VideoLikeEvent;

import com.example.cooplas.models.home.commentModels.CommentMainModel;
import com.example.cooplas.models.home.singlePost.Comment;
import com.example.cooplas.models.videos.TagsModel;
import com.example.cooplas.models.videos.Video;
import com.example.cooplas.utils.CircleTransform;

import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoAdapter extends RecyclerView.Adapter {

    private ArrayList<Video> dataList;
    private Activity activity;


    public VideoAdapter(Activity activity, ArrayList<Video> dataList) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataList.get(position).getType()) {
            case 1:

                return Video.TYPE_TAGS;


            case 7:
                return Video.TYPE_VIDEO;
            default:
                return -1;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Video.TYPE_TAGS:
                View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video_tags, parent, false);
                return new ViewHolderTags(layoutTwo);
            case Video.TYPE_VIDEO:
                View layoutEight = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_video2, parent, false);
                return new ViewHolderVideo(layoutEight);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("onBindViewHolder", "onBindViewHolder: " + position);
        Video videoModel = dataList.get(position);
        switch (dataList.get(position).getType()) {
            case Video.TYPE_TAGS:


                String body = videoModel.getBody();

                ArrayList<TagsModel> arrayList = new ArrayList();

                if (body.equalsIgnoreCase("recent")) {
                    TagsModel model1 = new TagsModel();
                    model1.setIsSelected("1");
                    model1.setTitle("Recent");
                    arrayList.add(model1);

                } else {
                    TagsModel model1 = new TagsModel();
                    model1.setIsSelected("0");
                    model1.setTitle("Recent");
                    arrayList.add(model1);
                }

                if (body.equalsIgnoreCase("Trending")) {
                    TagsModel model2 = new TagsModel();
                    model2.setIsSelected("1");
                    model2.setTitle("Trending");
                    arrayList.add(model2);
                } else {
                    TagsModel model2 = new TagsModel();
                    model2.setIsSelected("0");
                    model2.setTitle("Trending");
                    arrayList.add(model2);
                }

                TagsAdapter adapter = new TagsAdapter(arrayList, activity);
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                ((ViewHolderTags) holder).recyclerView.setLayoutManager(layoutManager);
                ((ViewHolderTags) holder).recyclerView.setAdapter(adapter);


                break;


            case Video.TYPE_VIDEO:

                if (videoModel.getIsLiked().equalsIgnoreCase("1")) {
                    ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                } else {
                    ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                }
                ((ViewHolderVideo) holder).iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueLiked = videoModel.getIsLiked();

                        if (valueLiked.equalsIgnoreCase("1")) {
                            //Make it unLike
                            ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                        } else {
                            //Make it Like
                            ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                        }
                        EventBus.getDefault().post(new VideoLikeEvent(videoModel.getId(), position, Integer.parseInt(valueLiked)));

                    }
                });


                ((ViewHolderVideo) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("itemPosition", "onClick: Positon" + videoModel.getId() + "");
                        Intent intent = new Intent(activity, PostCommentActivity.class);
                        intent.putExtra("postID", String.valueOf(videoModel.getId()));
                        activity.startActivity(intent);
                    }
                });

                Picasso.get().load(videoModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderVideo) holder).iv_profile);

                ((ViewHolderVideo) holder).tv_name.setText(videoModel.getUser().getFirstName() + " " + videoModel.getUser().getLastName());
                ((ViewHolderVideo) holder).tv_time.setText(videoModel.getCreatedAt());
                ((ViewHolderVideo) holder).tv_description.setText(videoModel.getBody());
                ((ViewHolderVideo) holder).tv_description.setVisibility(View.GONE);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.isMemoryCacheable();
                String videopath = videoModel.getMedia().get(0).getPath();
                Glide.with(activity).setDefaultRequestOptions(requestOptions).load(videopath).into(((ViewHolderVideo) holder).first_image_view);


                ((ViewHolderVideo) holder).tv_like_count.setText(String.valueOf(videoModel.getLikesCount()));
                ((ViewHolderVideo) holder).tv_comment_count.setText(String.valueOf(videoModel.getCommentsCount()));
                ((ViewHolderVideo) holder).tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                ((ViewHolderVideo) holder).iv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolderVideo) holder).tv_share.callOnClick();
                    }
                });
                Picasso.get().load(videoModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderVideo) holder).iv_write_comment);
                ((ViewHolderVideo) holder).first_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, VideoViewActivity.class);
                        intent.putExtra("videoLink", videoModel.getMedia().get(0).getPath());
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderVideo) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HomePostLikesActivity.class);
                        int postID = videoModel.getId();
                        intent.putExtra("postID", String.valueOf(postID));
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderVideo) holder).iv_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int postUserID = videoModel.getUser().getId();
                        String currentUserID = FunctionsKt.getUserID(activity);
                        if (currentUserID.equalsIgnoreCase(String.valueOf(postUserID))) {
                            // current user
                            showPostMenuMine(activity, view, ((ViewHolderVideo) holder).parentLayout, videoModel.getId(), position);
                        } else {
                            //other User
                            showPostMenuOthers(activity, view, ((ViewHolderVideo) holder).parentLayout, videoModel.getId(), position, videoModel.getIsFollowing(), videoModel.getUser().getId());
                        }
                    }
                });
                ((ViewHolderVideo) holder).iv_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String commentVal = ((ViewHolderVideo) holder).et_comment.getText().toString();
                        if (commentVal.isEmpty()) {
                            Toast.makeText(activity, "Please enter comment to send!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ((ViewHolderVideo) holder).et_comment.setText("");
                        postComment(String.valueOf(videoModel.getId()), commentVal, position, Integer.valueOf(videoModel.getCommentsCount()));
                    }
                });


                break;
            default:
                return;
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolderTags extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        public ViewHolderTags(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    static class ViewHolderVideo extends RecyclerView.ViewHolder {

        private ImageView iv_profile, first_image_view, iv_fav, iv_comment, iv_share, iv_write_comment, iv_send, iv_option;

        private TextView tv_name, tv_time, tv_description, tv_like_count, tv_comment_count, tv_share;
        private EditText et_comment;
        private RelativeLayout parentLayout;

        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_description = itemView.findViewById(R.id.tv_description);
            first_image_view = itemView.findViewById(R.id.firt_image_view);
            iv_fav = itemView.findViewById(R.id.iv_fav);
            tv_like_count = itemView.findViewById(R.id.tv_like_count);
            iv_comment = itemView.findViewById(R.id.iv_comment);
            tv_comment_count = itemView.findViewById(R.id.tv_comment_count);
            iv_share = itemView.findViewById(R.id.iv_share);
            tv_share = itemView.findViewById(R.id.tv_share);
            iv_write_comment = itemView.findViewById(R.id.iv_write_comment);
            et_comment = itemView.findViewById(R.id.et_comment);
            iv_send = itemView.findViewById(R.id.iv_send);
            iv_option = itemView.findViewById(R.id.iv_option);
            parentLayout = itemView.findViewById(R.id.parentLayout);

        }

    }

    public void showPostMenuOthers(Activity activity, View view, ViewGroup viewGroup, int postID, int position, String isFollowing, int userID) {
        LayoutInflater inflater = (LayoutInflater)
                activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.post_menu, viewGroup, false);

        TextView tv_unfollow = v.findViewById(R.id.tv_unfollow);
        TextView tv_report = v.findViewById(R.id.tv_report);

        if (isFollowing.equalsIgnoreCase("1")) {
            tv_unfollow.setText("Un Follow");
        } else {
            tv_unfollow.setText("Follow");
        }

        PopupWindow mypopupWindow = new PopupWindow(v, 300, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        if (v.getParent() != null) {
            ((ViewGroup) v.getParent()).removeView(v); // <- fix
        }
        mypopupWindow.showAsDropDown(view, 0, 0);

        mypopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypopupWindow.dismiss();
            }
        });

        tv_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isFollowing.equalsIgnoreCase("1")) {
                    tv_unfollow.setText("Un Follow");

                    dataList.get(position).setIsFollowing("0");
                    UnFollowUser(userID);


                } else {

                    tv_unfollow.setText("Follow");
                    dataList.get(position).setIsFollowing("1");
                    FollowUser(userID);
                }
                notifyItemChanged(position);
                mypopupWindow.dismiss();
            }
        });
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reportPost(postID);
                mypopupWindow.dismiss();
            }
        });
    }

    public void showPostMenuMine(Activity activity, View view, ViewGroup viewGroup, int postID, int position) {
        LayoutInflater inflater = (LayoutInflater)
                activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.post_menu_mine, viewGroup, false);

        TextView tv_edit = v.findViewById(R.id.tv_edit);
        TextView tv_delete = v.findViewById(R.id.tv_delete);
        PopupWindow mypopupWindow = new PopupWindow(v, 300, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        if (v.getParent() != null) {
            ((ViewGroup) v.getParent()).removeView(v); // <- fix
        }
        mypopupWindow.showAsDropDown(view, 0, 0);

        mypopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypopupWindow.dismiss();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deletePost(postID, position);
                mypopupWindow.dismiss();
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, EditPostActivity.class);
                intent.putExtra("postID", String.valueOf(postID));
                intent.putExtra("from", "video");
                activity.startActivity(intent);
                mypopupWindow.dismiss();

            }
        });


    }

    private void deletePost(int postID, int position) {
        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(activity);

        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.deletePost("Bearer " + accessToken, String.valueOf(postID));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postDel", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    dataList.remove(position);
                    notifyItemChanged(position);
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

    private void FollowUser(int postID) {
        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(activity);

        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.followUser("Bearer " + accessToken, String.valueOf(postID));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postDel", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "User UnFollowed", Toast.LENGTH_SHORT).show();
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

    private void UnFollowUser(int postID) {
        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(activity);

        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.unFollowUser("Bearer " + accessToken, String.valueOf(postID));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postDel", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "User UnFollowed", Toast.LENGTH_SHORT).show();
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

    private void reportPost(int postID) {


        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(activity);

        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.repostPost("Bearer " + accessToken, String.valueOf(postID));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postDel", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Post Reported Successfully!", Toast.LENGTH_SHORT).show();
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

    private void postComment(String postID, String commentValue, int position, int commentCount) {

        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(activity);
        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<CommentMainModel> call = apiInterface.postComment("Bearer " + accessToken, postID, commentValue);
        call.enqueue(new Callback<CommentMainModel>() {
            @Override
            public void onResponse(Call<CommentMainModel> call, Response<CommentMainModel> response) {
                Log.d("getSinglePost", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    CommentMainModel commentMainModel = response.body();
                    Comment comment = commentMainModel.getComment();


                    int count = commentCount + 1;
                    dataList.get(position).setCommentsCount(String.valueOf(count));
                    notifyItemChanged(position);

                    Intent intent = new Intent(activity, PostCommentActivity.class);
                    intent.putExtra("postID", postID);
                    activity.startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<CommentMainModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }
}
