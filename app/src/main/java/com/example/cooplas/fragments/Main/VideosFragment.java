package com.example.cooplas.fragments.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Travel.VideoAdapter;
import com.example.cooplas.events.videos.VideoLikeEvent;
import com.example.cooplas.events.videos.VideoRefreshFeedEvent;
import com.example.cooplas.events.videos.VideosTagEvent;
import com.example.cooplas.models.videos.Video;
import com.example.cooplas.models.videos.VideosFeedModel;
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

public class VideosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "VideosFragment";
    private Context context;
    private Activity activity;
    private boolean swipeRefreshCheck = false;
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private KProgressHUD progressHUD;
    private int offsetValue = 0;
    private int headerPosts = 0;
    private int adapterCheck = 0;
    private Video post;
    private ArrayList<Video> arrayList = new ArrayList<>();
    private VideoAdapter adapter;
    private LinearLayoutManager postLayoutManager;
    private RecyclerView recyclerViewPost;
    private int previousTotal = 0;
    private boolean isLoading = true;
    private boolean isLastPage = false;
    private int visibleThreshold = 5;
    private String categpryType = "recent";
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        context = getContext();
        activity = getActivity();
        EventBus.getDefault().register(this);
        init();
        resetFrag();

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

        return rootView;
    }

    private void resetFrag() {
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideosTagEvent event) {

        String title = event.getTitle();

        Log.d(TAG, "onMessageEvent: " + title);

        if (title.equalsIgnoreCase("Recent")) {
            categpryType = "recent";
        }

        if (title.equalsIgnoreCase("Trending")) {
            categpryType = "trending";
        }

        initialCall();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideoRefreshFeedEvent event) {
        initialCall();
    }

    private void init() {
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerViewPost = rootView.findViewById(R.id.rv_post_videos);
    }

    private void initialCall() {

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
    public void onRefresh() {
        categpryType = "recent";
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
        progressHUD = KProgressHUD.create(getActivity());
        if (swipeRefreshCheck == true) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressHUD.show();
        }
        String accessToken = FunctionsKt.getAccessToken(getContext());
        APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        Call<VideosFeedModel> call = apiInterface.getVideosFeed("Bearer " + accessToken, categpryType, String.valueOf(offsetValue));
        call.enqueue(new Callback<VideosFeedModel>() {

            @Override
            public void onResponse(Call<VideosFeedModel> call, Response<VideosFeedModel> response) {
                Log.d("getCommunity", "" + new Gson().toJson(response.body()));
                if (swipeRefreshCheck == true) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshCheck = false;
                } else {
                    progressHUD.dismiss();
                }
                if (response.isSuccessful()) {
                    VideosFeedModel model = response.body();
                    offsetValue = model.getNextOffset();
                    List<Video> videosList = model.getVideos();
                    if (headerPosts == 0) {
                        post = new Video();
                        post.setType(Video.TYPE_TAGS);
                        post.setId(0);
                        post.setBody(categpryType);
                        arrayList.add(post);
                        headerPosts = 1;
                    }
                    arrangeResponse(videosList);
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
            public void onFailure(Call<VideosFeedModel> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    private void populateFeedAdapter() {


        postLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new VideoAdapter(activity, arrayList);
        recyclerViewPost.setLayoutManager(postLayoutManager);
        recyclerViewPost.setAdapter(adapter);


    }

    private void arrangeResponse(List<Video> listOfPosts) {

        if (listOfPosts.size() > 0) {
        } else {
            isLastPage = true;
            return;
        }
        arrayList.addAll(listOfPosts);

        for (int i = 0; i < arrayList.size(); i++) {


            arrayList.get(i).setIsFollowing("1");

            int id = arrayList.get(i).getId();

            if (id == 0) {
                Log.d(TAG, "arrangeResponse: ");
            } else {
                arrayList.get(i).setType(Video.TYPE_VIDEO);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideoLikeEvent event) {

        int id = event.getId();
        int liked = event.getIsLiked();
        int position = event.getPosition();

        Video postModel = arrayList.get(position);
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
        KProgressHUD progressHUD = KProgressHUD.create(getActivity());
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getContext());

        APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
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
        KProgressHUD progressHUD = KProgressHUD.create(getActivity());
        progressHUD.show();

        String accessToken = FunctionsKt.getAccessToken(getContext());

        APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
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


    private void shareText(int postID) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareBody = "http://gong/community?post_id=" + postID;
        intent.setType("text/plain");
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(intent, "Share Using:"));
    }


}
