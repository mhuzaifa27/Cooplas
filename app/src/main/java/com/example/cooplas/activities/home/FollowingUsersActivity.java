package com.example.cooplas.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.cooplas.R;
import com.example.cooplas.adapters.FollowersAdapter;
import com.example.cooplas.models.home.homeLikesModels.Like;
import com.example.cooplas.models.profile.Followers.FollowersModel;
import com.example.cooplas.models.profile.Following.Following;
import com.example.cooplas.models.profile.Following.FollowingModel;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KProgressHUD progressHUD;
    private boolean isLoading = true;
    private boolean isLastPage = false;
    private int previousTotal = 0;
    int offsetValue = 0;
    private int visibleThreshold = 3;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int adapterCheck = 0;
    private LinearLayoutManager postLayoutManager;
    private FollowersAdapter adapter;
    private List<Following> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_users);


        RelativeLayout rl_back = findViewById(R.id.rl_back);
        recyclerView = findViewById(R.id.recyclerView);
        progressHUD = KProgressHUD.create(FollowingUsersActivity.this);


        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apiCall();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        apiCall();
                    }
                    isLoading = true;
                }
            }
        });
    }

    private void populateRecyclerView(List<Following> arrayList) {


        postLayoutManager = new LinearLayoutManager(FollowingUsersActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new FollowersAdapter(FollowingUsersActivity.this, arrayList);
        recyclerView.setLayoutManager(postLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    private void apiCall() {

        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<FollowingModel> call = apiInterface.userFollowing("Bearer " + accessToken, String.valueOf(offsetValue));
        call.enqueue(new Callback<FollowingModel>() {
            @Override
            public void onResponse(Call<FollowingModel> call, Response<FollowingModel> response) {
                Log.d("Following", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {


                    FollowingModel followingModel = response.body();

                    List<Following> followingList = followingModel.getFollowing();

                    if (followingList.size() > 0) {

                    } else {
                        isLastPage = true;
                        return;
                    }
                    offsetValue = followingModel.getNextOffset();
                    for (int i = 0; i < followingList.size(); i++) {
                        followingList.get(i).setIsFollowing("1");
                    }

                    arrayList.addAll(followingList);
                    if (adapterCheck == 0) {
                        adapterCheck = 1;
                        populateRecyclerView(arrayList);
                    } else {
                        Log.d("arraySize", arrayList.size() + "");
                        adapter.notifyDataSetChanged();
                    }


                }
            }

            @Override
            public void onFailure(Call<FollowingModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }
}
