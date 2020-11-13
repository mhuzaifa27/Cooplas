package com.example.cooplas.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;

import com.example.cooplas.adapters.SingleFeedPostAdapter;
import com.example.cooplas.events.home.PostLikeHome;
import com.example.cooplas.events.home.PostLikeHomeSInglePost;
import com.example.cooplas.models.home.commentModels.CommentMainModel;
import com.example.cooplas.models.home.singlePost.Comment;
import com.example.cooplas.models.home.singlePost.Medium;
import com.example.cooplas.models.home.singlePost.Post;
import com.example.cooplas.models.home.singlePost.SinglePostMainModel;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCommentActivity extends AppCompatActivity {

    private KProgressHUD progressHUD;
    private LinearLayoutManager postLayoutManager;
    private SingleFeedPostAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Post> arrayList = new ArrayList<>();

    private String postID;
    private ImageView iv_write_comment, iv_send;
    private EditText et_comment;
    private String commentValue = "";

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);


        Intent intent = getIntent();
        postID = intent.getStringExtra("postID");

        progressHUD = KProgressHUD.create(PostCommentActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        RelativeLayout rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_write_comment = findViewById(R.id.iv_write_comment);
        et_comment = findViewById(R.id.et_comment);
        iv_send = findViewById(R.id.iv_send);

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentValue = et_comment.getText().toString();
                if (commentValue.isEmpty()) {
                    Toast.makeText(PostCommentActivity.this, "Please enter comment!", Toast.LENGTH_SHORT).show();
                    return;
                }

                et_comment.setText("");

                postComment();
            }
        });


        getPosts();
    }

    private void getPosts() {

        progressHUD.show();

        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<SinglePostMainModel> call = apiInterface.getSinglePost("Bearer " + accessToken, postID);
        call.enqueue(new Callback<SinglePostMainModel>() {
            @Override
            public void onResponse(Call<SinglePostMainModel> call, Response<SinglePostMainModel> response) {
                Log.d("getSinglePost", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    SinglePostMainModel model = response.body();


                    Post singlePost = model.getPost();
                    arrangeResponse(singlePost);
                    populateFeedAdapter();

                }
            }

            @Override
            public void onFailure(Call<SinglePostMainModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    private void postComment() {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<CommentMainModel> call = apiInterface.postComment("Bearer " + accessToken, postID, commentValue);
        call.enqueue(new Callback<CommentMainModel>() {
            @Override
            public void onResponse(Call<CommentMainModel> call, Response<CommentMainModel> response) {
                Log.d("getSinglePost", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    CommentMainModel commentMainModel = response.body();
                    Comment comment = commentMainModel.getComment();


                    getPosts();


//                    List<Comment> lsitOfCOmmet = new ArrayList<>();
//
//                    lsitOfCOmmet.add(comment);

//                    arrayList.get(0).getComments().add(comment);
//                    adapter.notifyDataSetChanged();


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

    private void populateFeedAdapter() {
        postLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new SingleFeedPostAdapter(PostCommentActivity.this, arrayList);
        recyclerView.setLayoutManager(postLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void arrangeResponse(Post singlePost) {
//            int id = listOfPosts.get(i).getId();
//            int types = listOfPosts.get(i).getType();
//            Log.d("postID", id + " " + types);

        if (arrayList.size() > 0) {
            arrayList.clear();
        }


        List<Medium> listMedia = singlePost.getMedia();
        if (listMedia.size() > 0) {
            String type = listMedia.get(0).getType();
            if (type.contains("image")) {
                int sizeList = listMedia.size();
                switch (sizeList) {
                    case 1:
                        singlePost.setType(Post.TYPE_IMAGES_SINGLE);
                        arrayList.add(singlePost);
                        break;
                    case 2:
                        singlePost.setType(Post.TYPE_IMAGES_DOUBLE);
                        arrayList.add(singlePost);
                        break;
                    case 3:
                        singlePost.setType(Post.TYPE_IMAGES_TRIPPLE);
                        arrayList.add(singlePost);
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
                        singlePost.setType(Post.TYPE_IMAGES_MULTIPLE);
                        arrayList.add(singlePost);
                        break;
                }
            } else if (type.contains("video")) {
                singlePost.setType(com.example.cooplas.models.home.homeFragmentModel.Post.TYPE_VIDEO);
                arrayList.add(singlePost);
            }
        } else {
            singlePost.setType(Post.TYPE_TEXT);
            arrayList.add(singlePost);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PostLikeHomeSInglePost event) {

        int id = event.getId();
        int liked = event.getIsLiked();
        int position = event.getPosition();

        Post postModel = arrayList.get(position);
        if (liked == 1) {
            // make it UnLike
            postModel.setIsLiked("0");
            int likeCount = Integer.parseInt(postModel.getLikesCount()) - 1;
            postModel.setLikesCount(String.valueOf(likeCount));
            adapter.notifyItemChanged(position);
            postUnLike(id);
        } else {
            // make it like
            postModel.setIsLiked("1");
            int likeCount = Integer.parseInt(postModel.getLikesCount()) + 1;
            postModel.setLikesCount(String.valueOf(likeCount));
            adapter.notifyItemChanged(position);
            postLike(id);
        }
    }

    private void postLike(int postID) {
        KProgressHUD progressHUD = KProgressHUD.create(PostCommentActivity.this);
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
        KProgressHUD progressHUD = KProgressHUD.create(PostCommentActivity.this);
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
