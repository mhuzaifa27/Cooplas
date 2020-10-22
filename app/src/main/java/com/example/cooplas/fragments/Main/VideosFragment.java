package com.example.cooplas.fragments.Main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.adapters.HomePostAdapter;
import com.example.cooplas.adapters.HomeStoriesAdapter;
import com.example.cooplas.adapters.PostVideosAdapter;
import com.example.cooplas.utils.ShowMenu;

import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "VideosFragment";
    private Context context;
    private Activity activity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_no;
    private RecyclerView rv_stories, rv_post_videos;
    private LinearLayoutManager storiesLayoutManager,postLayoutManager;
    private HomeStoriesAdapter storiesAdapter;
    private PostVideosAdapter postVideosAdapter;

    private List<String> storiesList = new ArrayList<>();
    private List<String> postVideosList = new ArrayList<>();
    private ViewGroup parentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        initComponents(view);

        storiesList.add("s");
        storiesList.add("s");
        storiesList.add("s");
        storiesList.add("s");
        storiesList.add("s");
        storiesList.add("s");

        postVideosList.add("s");
        postVideosList.add("s");
        postVideosList.add("s");
        postVideosList.add("s");
        postVideosList.add("s");
        postVideosList.add("s");
        postVideosList.add("s");

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void initComponents(View view) {
        context = getContext();
        activity = getActivity();

        rv_stories = view.findViewById(R.id.rv_stories);
        rv_post_videos = view.findViewById(R.id.rv_post_videos);

        parentLayout = view.findViewById(android.R.id.content);

        storiesLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        postLayoutManager = new LinearLayoutManager(context);

        storiesAdapter = new HomeStoriesAdapter(storiesList, context);
        postVideosAdapter =new PostVideosAdapter(postVideosList,context,activity);
        postVideosAdapter.setOnMenuClick(new PostVideosAdapter.IClicks() {
            @Override
            public void OnMenuClick(View view) {
                ShowMenu.showPostMenu(activity,view,parentLayout);
            }
        });
        rv_stories.setLayoutManager(storiesLayoutManager);
        rv_stories.setAdapter(storiesAdapter);

        rv_post_videos.setLayoutManager(postLayoutManager);
        rv_post_videos.setAdapter(postVideosAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        //tv_no=view.findViewById(R.id.tv_no);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View view) {

    }
}
