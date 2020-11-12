package com.example.cooplas.adapters;

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
import com.example.cooplas.activities.home.ImageViewActivity;
import com.example.cooplas.activities.home.PostCommentActivity;
import com.example.cooplas.activities.home.VideoViewActivity;

import com.example.cooplas.events.home.PostLikeHome;
import com.example.cooplas.models.home.homeFragmentModel.Medium;
import com.example.cooplas.models.home.homeFragmentModel.Post;
import com.example.cooplas.utils.CircleTransform;
import com.example.cooplas.utils.RoundedCornersTransformation;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
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

public class HomeFeedAdapter extends RecyclerView.Adapter {

    private ArrayList<Post> dataList;
    private Activity activity;


    public HomeFeedAdapter(Activity activity, ArrayList<Post> dataList) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataList.get(position).getType()) {
            case 1:
                return Post.TYPE_HORIZONTAL_LIST;
            case 2:
                return Post.TYPE_TEXT;
            case 3:
                return Post.TYPE_IMAGES_SINGLE;
            case 4:
                return Post.TYPE_IMAGES_DOUBLE;
            case 5:
                return Post.TYPE_IMAGES_TRIPPLE;
            case 6:
                return Post.TYPE_IMAGES_MULTIPLE;
            case 7:
                return Post.TYPE_VIDEO;
            default:
                return -1;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Post.TYPE_HORIZONTAL_LIST:
                View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_horizontal_list, parent, false);
                return new ViewHolderHorizontalList(layoutTwo);
            case Post.TYPE_TEXT:
                View layoutThree = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_text, parent, false);
                return new ViewHolderText(layoutThree);
            case Post.TYPE_IMAGES_SINGLE:
                View layoutFour = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_single, parent, false);
                return new ViewHolderImageSingle(layoutFour);
            case Post.TYPE_IMAGES_DOUBLE:
                View layoutFive = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_double, parent, false);
                return new ViewHolderImageDouble(layoutFive);
            case Post.TYPE_IMAGES_TRIPPLE:
                View layoutSix = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_tripple, parent, false);
                return new ViewHolderImageTripple(layoutSix);
            case Post.TYPE_IMAGES_MULTIPLE:
                View layoutSeven = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_multiple, parent, false);
                return new ViewHolderImageMultiple(layoutSeven);
            case Post.TYPE_VIDEO:
                View layoutEight = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_video, parent, false);
                return new ViewHolderVideo(layoutEight);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("onBindViewHolder", "onBindViewHolder: " + position);
        Post postModel = dataList.get(position);
        switch (dataList.get(position).getType()) {
            case Post.TYPE_HORIZONTAL_LIST:
//
                HomeStoriesAdapter adapter = new HomeStoriesAdapter(postModel.getStories(), activity);
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                ((ViewHolderHorizontalList) holder).rv_stories.setLayoutManager(layoutManager);
                ((ViewHolderHorizontalList) holder).rv_stories.setAdapter(adapter);


                break;
            case Post.TYPE_TEXT:
                if (postModel.getIsLiked().equalsIgnoreCase("1")) {
                    ((ViewHolderText) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                } else {
                    ((ViewHolderText) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                }
                ((ViewHolderText) holder).iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueLiked = postModel.getIsLiked();

                        if (valueLiked.equalsIgnoreCase("1")) {
                            //Make it unLike
                            ((ViewHolderText) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                        } else {
                            //Make it Like
                            ((ViewHolderText) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                        }
                        EventBus.getDefault().post(new PostLikeHome(postModel.getId(), position, Integer.parseInt(valueLiked)));

                    }
                });


                ((ViewHolderText) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("itemPosition", "onClick: Positon" + postModel.getId() + "");

                        Intent intent = new Intent(activity, PostCommentActivity.class);
                        intent.putExtra("postID", String.valueOf(postModel.getId()));
                        activity.startActivity(intent);


                    }
                });


                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderText) holder).iv_profile);

                ((ViewHolderText) holder).tv_name.setText(postModel.getUser().getFirstName() + " " + postModel.getUser().getLastName());
                ((ViewHolderText) holder).tv_time.setText(postModel.getCreatedAt());
                ((ViewHolderText) holder).tv_description.setText(postModel.getBody());

                ((ViewHolderText) holder).tv_like_count.setText(String.valueOf(postModel.getLikesCount()));
                ((ViewHolderText) holder).tv_comment_count.setText(String.valueOf(postModel.getCommentsCount()));
                ((ViewHolderText) holder).tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                ((ViewHolderText) holder).iv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolderText) holder).tv_share.callOnClick();
                    }
                });
                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderText) holder).iv_write_comment);

                ((ViewHolderText) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HomePostLikesActivity.class);
                        int postID = postModel.getId();
                        intent.putExtra("postID", String.valueOf(postID));
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderText) holder).iv_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int postUserID = postModel.getUser().getId();
                        String currentUserID = FunctionsKt.getUserID(activity);
                        if (currentUserID.equalsIgnoreCase(String.valueOf(postUserID))) {
                            // current user
                            showPostMenuMine(activity, view, ((ViewHolderText) holder).parentLayout, postModel.getId(), position);
                        } else {
                            //other User
                            showPostMenuOthers(activity, view, ((ViewHolderText) holder).parentLayout, postModel.getId(), position, postModel.getIsFollowing(), postModel.getUser().getId());
                        }
                    }
                });


                break;
            case Post.TYPE_IMAGES_SINGLE:

                if (postModel.getIsLiked().equalsIgnoreCase("1")) {
                    ((ViewHolderImageSingle) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                } else {
                    ((ViewHolderImageSingle) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                }
                ((ViewHolderImageSingle) holder).iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueLiked = postModel.getIsLiked();

                        if (valueLiked.equalsIgnoreCase("1")) {
                            //Make it unLike
                            ((ViewHolderImageSingle) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                        } else {
                            //Make it Like
                            ((ViewHolderImageSingle) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                        }
                        EventBus.getDefault().post(new PostLikeHome(postModel.getId(), position, Integer.parseInt(valueLiked)));

                    }
                });


                ((ViewHolderImageSingle) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("itemPosition", "onClick: Positon" + postModel.getId() + "");
                        Intent intent = new Intent(activity, PostCommentActivity.class);
                        intent.putExtra("postID", String.valueOf(postModel.getId()));
                        activity.startActivity(intent);
                    }
                });


                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageSingle) holder).iv_profile);

                ((ViewHolderImageSingle) holder).tv_name.setText(postModel.getUser().getFirstName() + " " + postModel.getUser().getLastName());
                ((ViewHolderImageSingle) holder).tv_time.setText(postModel.getCreatedAt());
                ((ViewHolderImageSingle) holder).tv_description.setText(postModel.getBody());

                Picasso.get().load(postModel.getMedia().get(0).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageSingle) holder).first_image_view);


                ((ViewHolderImageSingle) holder).tv_like_count.setText(String.valueOf(postModel.getLikesCount()));
                ((ViewHolderImageSingle) holder).tv_comment_count.setText(String.valueOf(postModel.getCommentsCount()));
                ((ViewHolderImageSingle) holder).tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                ((ViewHolderImageSingle) holder).iv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolderImageSingle) holder).tv_share.callOnClick();
                    }
                });
                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageSingle) holder).iv_write_comment);


                ((ViewHolderImageSingle) holder).first_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "0");
                        activity.startActivity(intent);
                    }
                });

                ((ViewHolderImageSingle) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                ((ViewHolderImageSingle) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HomePostLikesActivity.class);
                        int postID = postModel.getId();
                        intent.putExtra("postID", String.valueOf(postID));
                        activity.startActivity(intent);
                    }
                });

                ((ViewHolderImageSingle) holder).iv_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int postUserID = postModel.getUser().getId();
                        String currentUserID = FunctionsKt.getUserID(activity);
                        if (currentUserID.equalsIgnoreCase(String.valueOf(postUserID))) {
                            // current user
                            showPostMenuMine(activity, view, ((ViewHolderImageSingle) holder).parentLayout, postModel.getId(), position);
                        } else {
                            //other User
                            showPostMenuOthers(activity, view, ((ViewHolderImageSingle) holder).parentLayout, postModel.getId(), position, postModel.getIsFollowing(), postModel.getUser().getId());
                        }
                    }
                });


                break;
            case Post.TYPE_IMAGES_DOUBLE:

                if (postModel.getIsLiked().equalsIgnoreCase("1")) {
                    ((ViewHolderImageDouble) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                } else {
                    ((ViewHolderImageDouble) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                }
                ((ViewHolderImageDouble) holder).iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueLiked = postModel.getIsLiked();

                        if (valueLiked.equalsIgnoreCase("1")) {
                            //Make it unLike
                            ((ViewHolderImageDouble) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                        } else {
                            //Make it Like
                            ((ViewHolderImageDouble) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                        }
                        EventBus.getDefault().post(new PostLikeHome(postModel.getId(), position, Integer.parseInt(valueLiked)));

                    }
                });


                ((ViewHolderImageDouble) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("itemPosition", "onClick: Positon" + postModel.getId() + "");
                        Intent intent = new Intent(activity, PostCommentActivity.class);
                        intent.putExtra("postID", String.valueOf(postModel.getId()));
                        activity.startActivity(intent);
                    }
                });


                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageDouble) holder).iv_profile);

                ((ViewHolderImageDouble) holder).tv_name.setText(postModel.getUser().getFirstName() + " " + postModel.getUser().getLastName());
                ((ViewHolderImageDouble) holder).tv_time.setText(postModel.getCreatedAt());
                ((ViewHolderImageDouble) holder).tv_description.setText(postModel.getBody());

                Picasso.get().load(postModel.getMedia().get(0).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageDouble) holder).first_image_view);
                Picasso.get().load(postModel.getMedia().get(1).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageDouble) holder).second_image_view);

                ((ViewHolderImageDouble) holder).tv_like_count.setText(String.valueOf(postModel.getLikesCount()));
                ((ViewHolderImageDouble) holder).tv_comment_count.setText(String.valueOf(postModel.getCommentsCount()));
                ((ViewHolderImageDouble) holder).tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                ((ViewHolderImageDouble) holder).iv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolderImageDouble) holder).tv_share.callOnClick();
                    }
                });
                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageDouble) holder).iv_write_comment);

                ((ViewHolderImageDouble) holder).first_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "0");
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageDouble) holder).second_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "1");
                        activity.startActivity(intent);
                    }
                });

                ((ViewHolderImageDouble) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HomePostLikesActivity.class);
                        int postID = postModel.getId();
                        intent.putExtra("postID", String.valueOf(postID));
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageDouble) holder).iv_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int postUserID = postModel.getUser().getId();
                        String currentUserID = FunctionsKt.getUserID(activity);
                        if (currentUserID.equalsIgnoreCase(String.valueOf(postUserID))) {
                            // current user
                            showPostMenuMine(activity, view, ((ViewHolderImageDouble) holder).parentLayout, postModel.getId(), position);
                        } else {
                            //other User
                            showPostMenuOthers(activity, view, ((ViewHolderImageDouble) holder).parentLayout, postModel.getId(), position, postModel.getIsFollowing(), postModel.getUser().getId());
                        }
                    }
                });

                break;
            case Post.TYPE_IMAGES_TRIPPLE:


                if (postModel.getIsLiked().equalsIgnoreCase("1")) {
                    ((ViewHolderImageTripple) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                } else {
                    ((ViewHolderImageTripple) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                }
                ((ViewHolderImageTripple) holder).iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueLiked = postModel.getIsLiked();

                        if (valueLiked.equalsIgnoreCase("1")) {
                            //Make it unLike
                            ((ViewHolderImageTripple) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                        } else {
                            //Make it Like
                            ((ViewHolderImageTripple) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                        }
                        EventBus.getDefault().post(new PostLikeHome(postModel.getId(), position, Integer.parseInt(valueLiked)));

                    }
                });


                ((ViewHolderImageTripple) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("itemPosition", "onClick: Positon" + postModel.getId() + "");
                        Intent intent = new Intent(activity, PostCommentActivity.class);
                        intent.putExtra("postID", String.valueOf(postModel.getId()));
                        activity.startActivity(intent);
                    }
                });


                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageTripple) holder).iv_profile);

                ((ViewHolderImageTripple) holder).tv_name.setText(postModel.getUser().getFirstName() + " " + postModel.getUser().getLastName());
                ((ViewHolderImageTripple) holder).tv_time.setText(postModel.getCreatedAt());
                ((ViewHolderImageTripple) holder).tv_description.setText(postModel.getBody());

                Picasso.get().load(postModel.getMedia().get(0).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageTripple) holder).first_image_view);
                Picasso.get().load(postModel.getMedia().get(1).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageTripple) holder).second_image_view);
                Picasso.get().load(postModel.getMedia().get(2).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageTripple) holder).third_image_view);

                ((ViewHolderImageTripple) holder).tv_like_count.setText(String.valueOf(postModel.getLikesCount()));
                ((ViewHolderImageTripple) holder).tv_comment_count.setText(String.valueOf(postModel.getCommentsCount()));
                ((ViewHolderImageTripple) holder).tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                ((ViewHolderImageTripple) holder).iv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolderImageTripple) holder).tv_share.callOnClick();
                    }
                });
                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageTripple) holder).iv_write_comment);


                ((ViewHolderImageTripple) holder).first_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "0");
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageTripple) holder).second_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "1");
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageTripple) holder).third_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "2");
                        activity.startActivity(intent);
                    }
                });

                ((ViewHolderImageTripple) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HomePostLikesActivity.class);
                        int postID = postModel.getId();
                        intent.putExtra("postID", String.valueOf(postID));
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageTripple) holder).iv_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int postUserID = postModel.getUser().getId();
                        String currentUserID = FunctionsKt.getUserID(activity);
                        if (currentUserID.equalsIgnoreCase(String.valueOf(postUserID))) {
                            // current user
                            showPostMenuMine(activity, view, ((ViewHolderImageTripple) holder).parentLayout, postModel.getId(), position);
                        } else {
                            //other User
                            showPostMenuOthers(activity, view, ((ViewHolderImageTripple) holder).parentLayout, postModel.getId(), position, postModel.getIsFollowing(), postModel.getUser().getId());
                        }
                    }
                });


                break;
            case Post.TYPE_IMAGES_MULTIPLE:

                if (postModel.getIsLiked().equalsIgnoreCase("1")) {
                    ((ViewHolderImageMultiple) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                } else {
                    ((ViewHolderImageMultiple) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                }
                ((ViewHolderImageMultiple) holder).iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueLiked = postModel.getIsLiked();

                        if (valueLiked.equalsIgnoreCase("1")) {
                            //Make it unLike
                            ((ViewHolderImageMultiple) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                        } else {
                            //Make it Like
                            ((ViewHolderImageMultiple) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                        }
                        EventBus.getDefault().post(new PostLikeHome(postModel.getId(), position, Integer.parseInt(valueLiked)));

                    }
                });


                ((ViewHolderImageMultiple) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("itemPosition", "onClick: Positon" + postModel.getId() + "");
                        Intent intent = new Intent(activity, PostCommentActivity.class);
                        intent.putExtra("postID", String.valueOf(postModel.getId()));
                        activity.startActivity(intent);
                    }
                });
                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageMultiple) holder).iv_profile);

                ((ViewHolderImageMultiple) holder).tv_name.setText(postModel.getUser().getFirstName() + " " + postModel.getUser().getLastName());
                ((ViewHolderImageMultiple) holder).tv_time.setText(postModel.getCreatedAt());
                ((ViewHolderImageMultiple) holder).tv_description.setText(postModel.getBody());

                Picasso.get().load(postModel.getMedia().get(0).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageMultiple) holder).first_image_view);
                Picasso.get().load(postModel.getMedia().get(1).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageMultiple) holder).second_image_view);
                Picasso.get().load(postModel.getMedia().get(2).getPath())
                        .transform(new RoundedCornersTransformation(10, 5))
                        .fit().centerCrop()
                        .into(((ViewHolderImageMultiple) holder).third_image_view);


                int totalSize = postModel.getMedia().size();
                int remainingImagesSize = totalSize - 3;
                ((ViewHolderImageMultiple) holder).tv_image_count.setText("+" + String.valueOf(remainingImagesSize));


                ((ViewHolderImageMultiple) holder).tv_like_count.setText(String.valueOf(postModel.getLikesCount()));
                ((ViewHolderImageMultiple) holder).tv_comment_count.setText(String.valueOf(postModel.getCommentsCount()));
                ((ViewHolderImageMultiple) holder).tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                ((ViewHolderImageMultiple) holder).iv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewHolderImageMultiple) holder).tv_share.callOnClick();
                    }
                });
                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderImageMultiple) holder).iv_write_comment);


                ((ViewHolderImageMultiple) holder).first_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "0");
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageMultiple) holder).second_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "1");
                        activity.startActivity(intent);
                    }
                });

                ((ViewHolderImageMultiple) holder).third_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> array = new ArrayList<>();
                        List<Medium> listImages = postModel.getMedia();
                        for (int i = 0; i < listImages.size(); i++) {
                            String path = listImages.get(i).getPath().toString();
                            array.add(path);
                        }
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "2");
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageMultiple) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HomePostLikesActivity.class);
                        int postID = postModel.getId();
                        intent.putExtra("postID", String.valueOf(postID));
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderImageMultiple) holder).iv_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int postUserID = postModel.getUser().getId();
                        String currentUserID = FunctionsKt.getUserID(activity);
                        if (currentUserID.equalsIgnoreCase(String.valueOf(postUserID))) {
                            // current user
                            showPostMenuMine(activity, view, ((ViewHolderImageMultiple) holder).parentLayout, postModel.getId(), position);
                        } else {
                            //other User
                            showPostMenuOthers(activity, view, ((ViewHolderImageMultiple) holder).parentLayout, postModel.getId(), position, postModel.getIsFollowing(), postModel.getUser().getId());
                        }
                    }
                });


                break;
            case Post.TYPE_VIDEO:

                if (postModel.getIsLiked().equalsIgnoreCase("1")) {
                    ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                } else {
                    ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                }
                ((ViewHolderVideo) holder).iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String valueLiked = postModel.getIsLiked();

                        if (valueLiked.equalsIgnoreCase("1")) {
                            //Make it unLike
                            ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.ic_unfav);
                        } else {
                            //Make it Like
                            ((ViewHolderVideo) holder).iv_fav.setBackgroundResource(R.drawable.fav);
                        }
                        EventBus.getDefault().post(new PostLikeHome(postModel.getId(), position, Integer.parseInt(valueLiked)));

                    }
                });


                ((ViewHolderVideo) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("itemPosition", "onClick: Positon" + postModel.getId() + "");
                        Intent intent = new Intent(activity, PostCommentActivity.class);
                        intent.putExtra("postID", String.valueOf(postModel.getId()));
                        activity.startActivity(intent);
                    }
                });

                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderVideo) holder).iv_profile);

                ((ViewHolderVideo) holder).tv_name.setText(postModel.getUser().getFirstName() + " " + postModel.getUser().getLastName());
                ((ViewHolderVideo) holder).tv_time.setText(postModel.getCreatedAt());
                ((ViewHolderVideo) holder).tv_description.setText(postModel.getBody());


                RequestOptions requestOptions = new RequestOptions();
                requestOptions.isMemoryCacheable();
                String videopath = postModel.getMedia().get(0).getPath();
                Glide.with(activity).setDefaultRequestOptions(requestOptions).load(videopath).into(((ViewHolderVideo) holder).first_image_view);


                ((ViewHolderVideo) holder).tv_like_count.setText(String.valueOf(postModel.getLikesCount()));
                ((ViewHolderVideo) holder).tv_comment_count.setText(String.valueOf(postModel.getCommentsCount()));
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
                Picasso.get().load(postModel.getUser()
                        .getProfilePic())
                        .fit().centerCrop()
                        .transform(new CircleTransform())
                        .into(((ViewHolderVideo) holder).iv_write_comment);
                ((ViewHolderVideo) holder).first_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, VideoViewActivity.class);
                        intent.putExtra("videoLink", postModel.getMedia().get(0).getPath());
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderVideo) holder).tv_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, HomePostLikesActivity.class);
                        int postID = postModel.getId();
                        intent.putExtra("postID", String.valueOf(postID));
                        activity.startActivity(intent);
                    }
                });
                ((ViewHolderVideo) holder).iv_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int postUserID = postModel.getUser().getId();
                        String currentUserID = FunctionsKt.getUserID(activity);
                        if (currentUserID.equalsIgnoreCase(String.valueOf(postUserID))) {
                            // current user
                            showPostMenuMine(activity, view, ((ViewHolderVideo) holder).parentLayout, postModel.getId(), position);
                        } else {
                            //other User
                            showPostMenuOthers(activity, view, ((ViewHolderVideo) holder).parentLayout, postModel.getId(), position, postModel.getIsFollowing(), postModel.getUser().getId());
                        }
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


    static class ViewHolderHorizontalList extends RecyclerView.ViewHolder {
        private RecyclerView rv_stories;

        public ViewHolderHorizontalList(@NonNull View itemView) {
            super(itemView);
            rv_stories = itemView.findViewById(R.id.rv_stories);
        }
    }

    static class ViewHolderText extends RecyclerView.ViewHolder {
        private ImageView iv_profile, iv_fav, iv_comment, iv_share, iv_write_comment, iv_send, iv_option;
        private TextView tv_name, tv_time, tv_description, tv_like_count, tv_comment_count, tv_share;
        private EditText et_comment;
        private RelativeLayout parentLayout;

        public ViewHolderText(@NonNull View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_description = itemView.findViewById(R.id.tv_description);
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


    static class ViewHolderImageSingle extends RecyclerView.ViewHolder {

        private ImageView iv_profile, first_image_view, iv_fav, iv_comment, iv_share, iv_write_comment, iv_send, iv_option;

        private TextView tv_name, tv_time, tv_description, tv_like_count, tv_comment_count, tv_share;
        private EditText et_comment;
        private RelativeLayout parentLayout;

        public ViewHolderImageSingle(@NonNull View itemView) {
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

    static class ViewHolderImageDouble extends RecyclerView.ViewHolder {

        private ImageView iv_profile, first_image_view, second_image_view, iv_fav, iv_comment, iv_share, iv_write_comment, iv_send, iv_option;

        private TextView tv_name, tv_time, tv_description, tv_like_count, tv_comment_count, tv_share;
        private EditText et_comment;
        private RelativeLayout parentLayout;

        public ViewHolderImageDouble(@NonNull View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_description = itemView.findViewById(R.id.tv_description);
            first_image_view = itemView.findViewById(R.id.firt_image_view);
            second_image_view = itemView.findViewById(R.id.second_image_view);
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

    static class ViewHolderImageTripple extends RecyclerView.ViewHolder {

        private ImageView iv_profile, first_image_view, second_image_view, third_image_view, iv_fav, iv_comment, iv_share, iv_write_comment, iv_send, iv_option;

        private TextView tv_name, tv_time, tv_description, tv_like_count, tv_comment_count, tv_share;
        private EditText et_comment;
        private RelativeLayout parentLayout;

        public ViewHolderImageTripple(@NonNull View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_description = itemView.findViewById(R.id.tv_description);
            first_image_view = itemView.findViewById(R.id.firt_image_view);
            second_image_view = itemView.findViewById(R.id.second_image_view);
            third_image_view = itemView.findViewById(R.id.third_image_view);
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

    static class ViewHolderImageMultiple extends RecyclerView.ViewHolder {

        private ImageView iv_profile, first_image_view, second_image_view, third_image_view_shadow, third_image_view, iv_fav, iv_comment, iv_share, iv_write_comment, iv_send, iv_option;
        private TextView tv_name, tv_time, tv_description, tv_like_count, tv_comment_count, tv_share, tv_image_count;
        private EditText et_comment;
        private RelativeLayout parentLayout;

        public ViewHolderImageMultiple(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_description = itemView.findViewById(R.id.tv_description);
            first_image_view = itemView.findViewById(R.id.firt_image_view);
            second_image_view = itemView.findViewById(R.id.second_image_view);
            third_image_view = itemView.findViewById(R.id.third_image_view);
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
            third_image_view_shadow = itemView.findViewById(R.id.third_image_view_shadow);
            tv_image_count = itemView.findViewById(R.id.tv_image_count);
            parentLayout = itemView.findViewById(R.id.parentLayout);
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

    private void shareText(int postID) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareBody = "http://gong/community?post_id=" + postID;
        intent.setType("text/plain");
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(intent, "Share Using:"));
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
}
