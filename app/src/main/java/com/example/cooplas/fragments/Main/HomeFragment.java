package com.example.cooplas.fragments.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.activities.home.HomeCreatePostActivity;
import com.example.cooplas.activities.home.SearchUserActivity;
import com.example.cooplas.adapters.HomeFeedAdapter;
import com.example.cooplas.events.AddStoryEvent;
import com.example.cooplas.events.home.HomeRefreshFeedEvent;
import com.example.cooplas.events.home.PostLikeHome;
import com.example.cooplas.models.home.homeFragmentModel.HomeModel;
import com.example.cooplas.models.home.homeFragmentModel.Medium;
import com.example.cooplas.models.home.homeFragmentModel.Post;
import com.example.cooplas.models.home.homeFragmentModel.UserStory;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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
    private List<UserStory> listStories;

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        EventBus.getDefault().register(this);

        initComponents();

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
    public void onMessageEvent(AddStoryEvent event) {

        new ImagePicker.Builder(getActivity())
                .mode(ImagePicker.Mode.GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.JPG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            String pathImage = mPaths.get(0);
            Log.d(TAG, "onActivityResult: ");
            uploadStory(pathImage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeRefreshFeedEvent event) {
        onRefresh();
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

    private void getPosts() {

        if (swipeRefreshCheck == true) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressHUD.show();
        }
        String accessToken = FunctionsKt.getAccessToken(getContext());
        APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        Call<HomeModel> call = apiInterface.getHomeFeed("Bearer " + accessToken, String.valueOf(offsetValue));
        call.enqueue(new Callback<HomeModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                Log.d("getCommunity", "" + new Gson().toJson(response.body()));
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
                    List<Post> listOfPosts = model.getPosts();
                    List<UserStory> listOFStories = new ArrayList<>();

                    UserStory story = new UserStory();
                    listOFStories.add(story);
                    listOFStories.addAll(model.getUserStories());


                    if (headerPosts == 0) {
                        if (listOFStories.size() > 0) {
                            listStories = listOFStories;
                        }
                        post = new Post();
                        post.setType(Post.TYPE_HORIZONTAL_LIST);
                        post.setStories(listStories);
                        arrayList.add(post);
                        headerPosts = 1;
                    }
//
//                    if (listOfPosts.size() > 0) {
//
//                    } else {
//
//                        return;
//                    }
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
                            listOfPosts.get(i).setIsFollowing("1");
                            arrayList.add(listOfPosts.get(i));
                            break;
                        case 2:
                            listOfPosts.get(i).setType(Post.TYPE_IMAGES_DOUBLE);
                            listOfPosts.get(i).setIsFollowing("1");
                            arrayList.add(listOfPosts.get(i));
                            break;
                        case 3:
                            listOfPosts.get(i).setType(Post.TYPE_IMAGES_TRIPPLE);
                            listOfPosts.get(i).setIsFollowing("1");
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
                            listOfPosts.get(i).setIsFollowing("1");
                            arrayList.add(listOfPosts.get(i));
                            break;
                    }

                } else if (type.contains("video")) {
                    listOfPosts.get(i).setType(Post.TYPE_VIDEO);
                    listOfPosts.get(i).setIsFollowing("1");
                    arrayList.add(listOfPosts.get(i));
                }

            } else {

                listOfPosts.get(i).setType(Post.TYPE_TEXT);
                listOfPosts.get(i).setIsFollowing("1");
                arrayList.add(listOfPosts.get(i));

            }
        }

    }

    private void populateFeedAdapter() {


        postLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new HomeFeedAdapter(activity, arrayList);
        recyclerViewPost.setLayoutManager(postLayoutManager);
        recyclerViewPost.setAdapter(adapter);


    }

    private void initComponents() {

        context = getContext();
        activity = getActivity();
        progressHUD = KProgressHUD.create(getActivity());

        recyclerViewPost = rootView.findViewById(R.id.rv_post);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        parentLayout = rootView.findViewById(android.R.id.content);
        ImageView create_post_img = rootView.findViewById(R.id.create_post_img);
        create_post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(activity)
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            Intent intent = new Intent(getActivity(), HomeCreatePostActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();


                    }
                }).check();


            }
        });

        ImageView search_img = rootView.findViewById(R.id.search_img);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchUserActivity.class);
                startActivity(intent);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PostLikeHome event) {

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

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void uploadStory(String mediaPath) {

        KProgressHUD progressHUD = KProgressHUD.create(getActivity()).show();
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getActivity());
        APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        builder.addFormDataPart("body", "");

//        for (int i = 0; i < listMedia.size(); i++) {

//            if (listMedia.get(i).getType().equals("vid")) {
//                File file = new File(listMedia.get(i).getPath());
//                String mimeType = getMimeType(listMedia.get(i).getPath());
//                RequestBody requestImage = RequestBody.create(MediaType.parse(mimeType), file);
//                builder.addFormDataPart("media[]", file.getName(), RequestBody.create(MediaType.parse(mimeType), file));
//            } else {
        String mimeType = getMimeType(mediaPath);
        File file = new File(mediaPath);
        RequestBody requestImage = RequestBody.create(MediaType.parse(mimeType), file);
//            builder.addFormDataPart("files[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        builder.addFormDataPart("media", file.getName(), RequestBody.create(MediaType.parse(mimeType), file));
//            }
//        }
        MultipartBody requestBody = builder.build();
        Call<JsonObject> call = apiInterface.createStoryPost("Bearer " + accessToken, requestBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postUpload", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Story Added successfully", Toast.LENGTH_SHORT).show();
                    // send noti
                    EventBus.getDefault().post(new HomeRefreshFeedEvent());
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
