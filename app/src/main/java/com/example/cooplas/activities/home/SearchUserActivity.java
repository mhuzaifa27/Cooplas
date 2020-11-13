package com.example.cooplas.activities.home;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.FollowingAdapter;
import com.example.cooplas.adapters.SearchUserAdapter;
import com.example.cooplas.models.home.searchUser.Result;
import com.example.cooplas.models.home.searchUser.SearchUserModel;
import com.example.cooplas.models.profile.Followers.Follower;
import com.example.cooplas.models.profile.Followers.FollowersModel;
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

public class SearchUserActivity extends AppCompatActivity {

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
    private SearchUserAdapter adapter;
    private List<Result> arrayList = new ArrayList<>();

    private EditText keyword_et;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);


        RelativeLayout rl_back = findViewById(R.id.rl_back);
        recyclerView = findViewById(R.id.recyclerView);
        keyword_et = findViewById(R.id.keyword_et);
        progressHUD = KProgressHUD.create(SearchUserActivity.this);


        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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

        keyword_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    keyword = keyword_et.getText().toString();
                    if (keyword.isEmpty()) {

                        Toast.makeText(SearchUserActivity.this, "Please enter user name to search", Toast.LENGTH_SHORT).show();

                    } else {

                        isLoading = true;
                        isLastPage = false;
                        previousTotal = 0;
                        offsetValue = 0;
                        visibleThreshold = 3;
                        pastVisiblesItems = 0;
                        visibleItemCount = 0;
                        totalItemCount = 0;
                        adapterCheck = 0;
                        if (arrayList.size() > 0) {
                            arrayList.clear();
                        }

                        apiCall();
                    }


                    return true;
                }
                return false;
            }
        });

    }

    private void populateRecyclerView(List<Result> arrayList) {


        postLayoutManager = new LinearLayoutManager(SearchUserActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new SearchUserAdapter(SearchUserActivity.this, arrayList);
        recyclerView.setLayoutManager(postLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    private void apiCall() {

        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getApplicationContext());
        APIInterface apiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        Call<SearchUserModel> call = apiInterface.searchUsers("Bearer " + accessToken, keyword, String.valueOf(offsetValue));
        call.enqueue(new Callback<SearchUserModel>() {
            @Override
            public void onResponse(Call<SearchUserModel> call, Response<SearchUserModel> response) {
                Log.d("Following", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {


                    SearchUserModel followingModel = response.body();

                    List<Result> usersLIst = followingModel.getResults();

                    if (usersLIst.size() > 0) {

                    } else {

                        if (offsetValue == 0) {
                            Toast.makeText(SearchUserActivity.this, "No user found with this name!", Toast.LENGTH_SHORT).show();
                        }


                        isLastPage = true;
                        return;
                    }
                    offsetValue = followingModel.getNextOffset();

                    arrayList.addAll(usersLIst);
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
            public void onFailure(Call<SearchUserModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }
}
