package com.example.cooplas.fragments.Main;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.adapters.HomeFeedAdapter;

import com.example.cooplas.models.homeFragmentModel.HomeModel;
import com.example.cooplas.models.homeFragmentModel.Medium;
import com.example.cooplas.models.homeFragmentModel.Post;
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

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private Context context;
    private Activity activity;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerViewPost;
    private LinearLayoutManager postLayoutManager;
    private LinearLayoutManager storiesLayoutManager;

    private HomeFeedAdapter adapter;
    private ViewGroup parentLayout;

    private View rootView;
    private HomeModel model;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents();


        swipeRefreshLayout.setOnRefreshListener(this);
//        populateFeedAdapter();


        getPosts();

        return rootView;
    }

    private void getPosts() {
        if (swipeRefreshCheck == true) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressHUD.show();
        }

        String accessToken = FunctionsKt.getAccessToken(getContext());
        APIInterface apiInterface = APIClient.getClient(getContext()).create(APIInterface.class);
        Call<HomeModel> call = apiInterface.getHomeFeed("Bearer " + accessToken, String.valueOf(offsetValue));
        call.enqueue(new Callback<HomeModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                Log.e("getCommunity", "" + new Gson().toJson(response.body()));
                if (swipeRefreshCheck == true) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshCheck = false;
                } else {
                    progressHUD.dismiss();
                }
                if (response.isSuccessful()) {
                    HomeModel model = response.body();
                    offsetValue = model.getPostsNextOffset();
                    Log.d("offset", "onResponse: " + offsetValue);
                    List<Post> listOfPosts = model.getWall().getPosts();

                    if (listOfPosts.size() > 0) {
                    } else {
                        return;
                    }

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
            public void onFailure(Call<HomeModel> call, Throwable t) {
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
            return;
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

        recyclerViewPost = rootView.findViewById(R.id.rv_post);
        postLayoutManager = new LinearLayoutManager(context);
        adapter = new HomeFeedAdapter(activity, arrayList);
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
        context = getContext();
        activity = getActivity();
        progressHUD = KProgressHUD.create(getActivity());


        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        parentLayout = rootView.findViewById(android.R.id.content);
    }

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

    @Override
    public void onClick(View view) {

    }
}
