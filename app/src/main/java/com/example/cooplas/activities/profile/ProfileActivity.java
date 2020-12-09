package com.example.cooplas.activities.profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;

import com.example.cooplas.adapters.ProfileFeedAdapter;

import com.example.cooplas.events.profile.PostLikeProfile;
import com.example.cooplas.models.profile.Medium;
import com.example.cooplas.models.profile.Post;
import com.example.cooplas.models.profile.ProfileModel;
import com.example.cooplas.models.profile.Wall;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewPost;
    private LinearLayoutManager postLayoutManager;
    private ProfileFeedAdapter adapter;
    private ViewGroup parentLayout;

    private boolean swipeRefreshCheck = false;
    private boolean isLoading = true;
    private boolean isLastPage = false;
    private int previousTotal = 0;
    int offsetValue = 0;
    private int visibleThreshold = 5;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private KProgressHUD progressHUD;
    private int headerPosts = 0;
    private int adapterCheck = 0;
    private Post post;
    private ArrayList<Post> arrayList = new ArrayList<>();
    private Wall wallModel;
    private TextView tv_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressHUD = KProgressHUD.create(ProfileActivity.this);
        initComponents();

        swipeRefreshLayout.setOnRefreshListener(this);
//        populateFeedAdapter();
        recyclerViewPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = postLayoutManager.getItemCount();
                pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (isLoading) {
                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount)
                        <= (pastVisiblesItems + visibleThreshold)) {

                    Log.i("Yaeye!", "end called");
                    if (isLastPage == false) {
                        getPosts();
                    }
                    isLoading = true;
                }
            }
        });

        getPosts();


    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(HomeRefreshFeedEvent event) {
//        onRefresh();
//    }

    @Override
    public void onRefresh() {
        swipeRefreshCheck = true;
        headerPosts = 0;
        adapterCheck = 0;
        isLoading = true;
        isLastPage = false;
        previousTotal = 0;
        offsetValue = 0;
        visibleThreshold = 5;
        if (arrayList.size() > 0) {
            arrayList.clear();
        }
        getPosts();
    }

    private void getPosts() {
        if (swipeRefreshCheck == true) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressHUD.show();
        }
        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<ProfileModel> call = apiInterface.getUserWall("Bearer " + accessToken, String.valueOf(offsetValue));
        call.enqueue(new Callback<ProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                Log.d("getCommunity", "" + new Gson().toJson(response.body()));
                if (swipeRefreshCheck == true) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshCheck = false;
                } else {
                    progressHUD.dismiss();
                }
                if (response.isSuccessful()) {
                    ProfileModel model = response.body();
                    offsetValue = model.getPostsNextOffset();
                    Log.d("offset", "onResponse: " + offsetValue);

                    wallModel = model.getWall();
                    tv_name.setText(wallModel.getFirstName() + " " + wallModel.getLastName());

                    List<Post> listOfPosts = model.getWall().getPosts();

//                    if (listOfPosts.size() > 0) {
//                    } else {
//                        return;
//                    }

                    if (headerPosts == 0) {
                        post = new Post();
                        post.setType(Post.TYPE_HORIZONTAL_LIST);
                        arrayList.add(post);
                        headerPosts = 1;
                    }
                    arrangeResponse(listOfPosts);
                    if (adapterCheck == 0) {

                        adapterCheck = 1;
                        populateFeedAdapter();

                    } else {

                        Log.d("arraySize", arrayList.size() + "");
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    private void arrangeResponse(List<Post> listOfPosts) {

        if (listOfPosts.size() > 0) {

        } else {
            isLastPage = true;
//            return;
        }
        for (int i = 0; i < listOfPosts.size(); i++) {
            int id = listOfPosts.get(i).getId();
            int types = listOfPosts.get(i).getType();
            Log.d("postID", id + " " + types);
            List<Medium> listMedia = listOfPosts.get(i).getMedia();
            if (listMedia.size() > 0) {
                String type = listMedia.get(0).getType();
                if (type.contains("image")) {

                    int sizeList = listMedia.size();
                    switch (sizeList) {
                        case 1:
                            listOfPosts.get(i).setType(Post.TYPE_IMAGES_SINGLE);
                            arrayList.add(listOfPosts.get(i));
                            break;
                        case 2:
                            listOfPosts.get(i).setType(Post.TYPE_IMAGES_DOUBLE);
                            arrayList.add(listOfPosts.get(i));
                            break;
                        case 3:
                            listOfPosts.get(i).setType(Post.TYPE_IMAGES_TRIPPLE);
                            arrayList.add(listOfPosts.get(i));
                            break;
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            listOfPosts.get(i).setType(Post.TYPE_IMAGES_MULTIPLE);
                            arrayList.add(listOfPosts.get(i));
                            break;
                    }

                } else if (type.contains("video")) {
                    listOfPosts.get(i).setType(Post.TYPE_VIDEO);
                    arrayList.add(listOfPosts.get(i));
                }

            } else {

                listOfPosts.get(i).setType(Post.TYPE_TEXT);
                arrayList.add(listOfPosts.get(i));

            }
        }

    }


    private void populateFeedAdapter() {
        postLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new ProfileFeedAdapter(ProfileActivity.this, arrayList, wallModel);
//        homePostAdapter.setOnMenuClick(new HomePostAdapter.IClicks() {
//            @Override
//            public void OnMenuClick(View view) {
//                ShowMenu.showPostMenu(activity, view, parentLayout);
//            }
//        });
        recyclerViewPost.setLayoutManager(postLayoutManager);
        recyclerViewPost.setAdapter(adapter);


    }

    private void initComponents() {

        RelativeLayout rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerViewPost = findViewById(R.id.rv_post);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(ProfileActivity.this);
        parentLayout = findViewById(android.R.id.content);
        tv_name = findViewById(R.id.tv_name);
        RelativeLayout rl_edit = findViewById(R.id.rl_edit);
        rl_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileScreen.class);
                startActivity(intent);
            }
        });


//        ((ProfileFeedAdapter.ViewHolderHorizontalList) holder).rl_edit
//
//
//        ImageView create_post_img = findViewById(R.id.create_post_img);
//        create_post_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dexter.withContext(getApplicationContext())
//                        .withPermissions(
//                                Manifest.permission.CAMERA,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        ).withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//                        if (report.areAllPermissionsGranted()) {
//
//                            Intent intent = new Intent(getApplicationContext(), HomeCreatePostActivity.class);
//                            startActivity(intent);
//                        }
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//                        token.continuePermissionRequest();
//
//
//                    }
//                }).check();
//
//
//            }
//        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PostLikeProfile event) {

        int id = event.getId();
        int liked = event.getIsLiked();
        int position = event.getPosition();

        Post postModel = arrayList.get(position);
        if (liked == 1) {
            // make it UnLike
            postModel.setIsLiked("0");
            int likeCount = postModel.getLikesCount() - 1;
            postModel.setLikesCount(likeCount);
            adapter.notifyItemChanged(position);
            postUnLike(id);

        } else {
            // make it like
            postModel.setIsLiked("1");
            int likeCount = postModel.getLikesCount() + 1;
            postModel.setLikesCount(likeCount);
            adapter.notifyItemChanged(position);
            postLike(id);

        }
    }

    private void postLike(int postID) {
        KProgressHUD progressHUD = KProgressHUD.create(getApplicationContext());
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());

        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.postLikeHome("Bearer " + accessToken, String.valueOf(postID));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("postLike", "" + new Gson().toJson(response.body()));

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

    private void postUnLike(int postID) {
        KProgressHUD progressHUD = KProgressHUD.create(getApplicationContext());
        progressHUD.show();

        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());

        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.poskUnLikeHome("Bearer " + accessToken, String.valueOf(postID));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("postUnLike", "" + new Gson().toJson(response.body()));

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
