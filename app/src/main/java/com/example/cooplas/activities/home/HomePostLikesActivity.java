package com.example.cooplas.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.cooplas.R;
import com.example.cooplas.adapters.HomeLikesAdapter;
import com.example.cooplas.models.home.homeLikesModels.Like;
import com.example.cooplas.models.home.homeLikesModels.LikeMainModel;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePostLikesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_post_likes);


        RelativeLayout rl_back = findViewById(R.id.rl_back);
        recyclerView = findViewById(R.id.rcv_likes);
        progressHUD = KProgressHUD.create(HomePostLikesActivity.this);

        Intent intent = getIntent();
        if (intent != null) {
            String postID = intent.getStringExtra("postID");
            getPostLikes(postID);
        }

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void populateRecyclerView(List<Like> likesList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        HomeLikesAdapter adapter = new HomeLikesAdapter(HomePostLikesActivity.this, likesList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void getPostLikes(String postID) {

        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<LikeMainModel> call = apiInterface.getPostLikes("Bearer " + accessToken, String.valueOf(postID));
        call.enqueue(new Callback<LikeMainModel>() {
            @Override
            public void onResponse(Call<LikeMainModel> call, Response<LikeMainModel> response) {
                Log.d("postUnLike", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    List<Like> likesList = response.body().getLikes();
                    if (likesList.size() > 0) {
                        populateRecyclerView(likesList);
                    }
                }
            }
            @Override
            public void onFailure(Call<LikeMainModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }


}