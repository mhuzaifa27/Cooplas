package com.example.cooplas.activities.Music;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.activities.ContactUsActivity;
import com.example.cooplas.activities.Food.AddressesActivity;
import com.example.cooplas.activities.Food.AllCategoriesActivity;
import com.example.cooplas.activities.Food.AllForYouActivity;
import com.example.cooplas.activities.Food.AllNearYouActivity;
import com.example.cooplas.activities.Food.AllPopularRestaurantActivity;
import com.example.cooplas.activities.Food.FoodFilterActivity;
import com.example.cooplas.activities.Food.FoodOrderDetailActivity;
import com.example.cooplas.activities.Food.FoodOrderHistoryActivity;
import com.example.cooplas.activities.Food.FoodTrackOrderActivity;
import com.example.cooplas.activities.Travel.Customer.NotificationActivity;
import com.example.cooplas.activities.Wallet.WalletActivity;
import com.example.cooplas.adapters.Music.MusicFeaturedAlbumAdapter;
import com.example.cooplas.adapters.Music.MusicFeaturedArtistAdapter;
import com.example.cooplas.adapters.Music.MusicRecentPlayedAdapter;
import com.example.cooplas.adapters.Music.MusicTrendingAdapter;
import com.example.cooplas.adapters.Music.MusicYourPlaylistAdapter;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.ShowDialogues;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MusicActivity";
    private Context context = MusicActivity.this;
    private Activity activity = MusicActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;

    private DrawerLayout drawer_food;

    private RecyclerView rv_recent_played, rv_trending, rv_your_playlist, rv_featured_artist,rv_featured_album;
    private LinearLayoutManager layoutManagerRecentPlayed, layoutManagerTrending, layoutManagerYourPlaylist, layoutManagerFeatureArtist,layoutManagerFeatureAlbum;

    private List<String> recentPlayedList = new ArrayList<>();
    private List<String> trendingList = new ArrayList<>();
    private List<String> yourPlayList = new ArrayList<>();
    private List<String> featureArtistList = new ArrayList<>();
    private List<String> featureAlbumList = new ArrayList<>();

    private MusicRecentPlayedAdapter musicRecentPlayedAdapter;
    private MusicTrendingAdapter musicTrendingAdapter;
    private MusicYourPlaylistAdapter musicYourPlaylistAdapter;
    private MusicFeaturedArtistAdapter musicFeaturedArtistAdapter;
    private MusicFeaturedAlbumAdapter musicFeaturedAlbumAdapter;

    private TextView tv_see_your_playlist, tv_see_trending, tv_see_near_you, tv_see_popular_restaurant;
    private ImageView img_filters, img_cart, img_discover, img_back;
    private CircleImageView img_user;

    private KProgressHUD progressHUD;

    private TextView tv_my_wallet,tv_order_history,tv_track_order,tv_addresses,tv_notifications,tv_invite_friends,tv_help_and_support,tv_log_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initComponents();
        getMusicData();

        tv_see_trending.setOnClickListener(this);
        img_discover.setOnClickListener(this);
        tv_see_your_playlist.setOnClickListener(this);
        //tv_see_near_you.setOnClickListener(this);
/*        img_filters.setOnClickListener(this);
        img_cart.setOnClickListener(this);
        img_user.setOnClickListener(this);

        tv_my_wallet.setOnClickListener(this);
        tv_order_history.setOnClickListener(this);
        tv_track_order.setOnClickListener(this);
        tv_addresses.setOnClickListener(this);
        tv_notifications.setOnClickListener(this);
        tv_invite_friends.setOnClickListener(this);
        tv_help_and_support.setOnClickListener(this);
        tv_log_out.setOnClickListener(this);*/
    }

    private void getMusicData() {

        recentPlayedList.add("d");
        recentPlayedList.add("d");
        recentPlayedList.add("d");
        recentPlayedList.add("d");

        trendingList.add("d");
        trendingList.add("d");
        trendingList.add("d");
        trendingList.add("d");

        yourPlayList.add("s");
        yourPlayList.add("s");
        yourPlayList.add("s");
        yourPlayList.add("s");

        featureArtistList.add("k");
        featureArtistList.add("k");
        featureArtistList.add("k");
        featureArtistList.add("k");
        featureArtistList.add("k");
        featureArtistList.add("k");

        featureAlbumList.add("k");
        featureAlbumList.add("k");
        featureAlbumList.add("k");
        featureAlbumList.add("k");
        featureAlbumList.add("k");
        featureAlbumList.add("k");

        setData();
        /*if (swipeRefreshCheck == true) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressHUD.show();
        }*/
        /*progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackGetFood> call = apiInterface.getFood("Bearer " + accessToken);
        call.enqueue(new Callback<CallbackGetFood>() {
            @Override
            public void onResponse(Call<CallbackGetFood> call, Response<CallbackGetFood> response) {
                CallbackGetFood responseGetFood = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetFood != null) {
                    if (responseGetFood.getSuccess()) {
                        if (responseGetFood.getCategories().size() > 0)
                            categoriesList = responseGetFood.getCategories();
                        if (responseGetFood.getForYou().size() > 0)
                            forYouList = responseGetFood.getForYou();
                        if (responseGetFood.getNearYou().size() > 0)
                            nearYouList = responseGetFood.getNearYou();
                        if (responseGetFood.getPopularRestaurants().size() > 0)
                            popularRestaurantList = responseGetFood.getPopularRestaurants();
                        setData();
                    } else {
                        progressHUD.dismiss();
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackGetFood> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });*/
    }

    private void setData() {
        musicRecentPlayedAdapter.addAll(recentPlayedList);
        musicTrendingAdapter.addAll(trendingList);
        musicYourPlaylistAdapter.addAll(yourPlayList);
        musicFeaturedArtistAdapter.addAll(featureArtistList);
        musicFeaturedAlbumAdapter.addAll(featureAlbumList);

        /*
        *//***Item Clicks***//*
        final boolean[] isFavourite = {false};
        musicTrendingAdapter.OnClickListener(new FoodForYouAdapter.ICLicks() {
            @Override
            public void onFavouriteClick(View view, ForYou forYou, ImageView img_favourite) {
                if (isFavourite[0]){
                    isFavourite[0] =false;
                    img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_heart));
                }
                else{
                    isFavourite[0] =true;
                    img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_fill_heart));
                }
            }
        });

        progressHUD.dismiss();*/
    }

    private void initComponents() {
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        progressHUD = KProgressHUD.create(activity);
        img_discover = findViewById(R.id.img_discover);
        img_discover.setVisibility(View.VISIBLE);
        img_cart = findViewById(R.id.img_cart);
        img_cart.setVisibility(View.GONE);

       /* img_filters = findViewById(R.id.img_filters);
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.GONE);
        img_user = findViewById(R.id.img_user);*/

        tv_see_trending = findViewById(R.id.tv_see_trending);
        tv_see_your_playlist=findViewById(R.id.tv_see_your_playlist);
        /*tv_see_near_you = findViewById(R.id.tv_see_near_you);
        tv_see_popular_restaurant = findViewById(R.id.tv_see_popular_restaurant);

        tv_my_wallet = findViewById(R.id.tv_my_wallet);
        tv_order_history = findViewById(R.id.tv_order_history);
        tv_track_order = findViewById(R.id.tv_track_order);
        tv_addresses = findViewById(R.id.tv_addresses);
        tv_notifications = findViewById(R.id.tv_notifications);
        tv_invite_friends = findViewById(R.id.tv_invite_friends);
        tv_help_and_support = findViewById(R.id.tv_help_and_support);
        tv_log_out = findViewById(R.id.tv_log_out);*/

        rv_recent_played = findViewById(R.id.rv_recent_played);
        rv_trending = findViewById(R.id.rv_trending);
        rv_your_playlist = findViewById(R.id.rv_your_playlist);
        rv_featured_artist = findViewById(R.id.rv_featured_artist);
        rv_featured_album=findViewById(R.id.rv_featured_album);
        
        layoutManagerRecentPlayed = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_recent_played.setLayoutManager(layoutManagerRecentPlayed);
        layoutManagerTrending = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rv_trending.setLayoutManager(layoutManagerTrending);
        layoutManagerYourPlaylist = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_your_playlist.setLayoutManager(layoutManagerYourPlaylist);
        layoutManagerFeatureArtist = new GridLayoutManager(context, 3);
        rv_featured_artist.setLayoutManager(layoutManagerFeatureArtist);
        layoutManagerFeatureAlbum = new GridLayoutManager(context,3);
        rv_featured_album.setLayoutManager(layoutManagerFeatureAlbum);

        musicRecentPlayedAdapter = new MusicRecentPlayedAdapter(recentPlayedList, context);
        rv_recent_played.setAdapter(musicRecentPlayedAdapter);
        musicTrendingAdapter = new MusicTrendingAdapter(trendingList, context);
        rv_trending.setAdapter(musicTrendingAdapter);
        musicYourPlaylistAdapter = new MusicYourPlaylistAdapter(yourPlayList, context);
        rv_your_playlist.setAdapter(musicYourPlaylistAdapter);
        musicFeaturedArtistAdapter = new MusicFeaturedArtistAdapter(featureArtistList, context);
        rv_featured_artist.setAdapter(musicFeaturedArtistAdapter);
        musicFeaturedAlbumAdapter = new MusicFeaturedAlbumAdapter(featureAlbumList, context);
        rv_featured_album.setAdapter(musicFeaturedAlbumAdapter);

        drawer_food = findViewById(R.id.drawer_food);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_see_all_categories:
                startActivity(new Intent(context, AllCategoriesActivity.class));
                break;
            case R.id.tv_see_trending:
                startActivity(new Intent(context, AllTrendingActivity.class));
                break;
            case R.id.tv_see_near_you:
                startActivity(new Intent(context, AllNearYouActivity.class));
                break;
            case R.id.tv_see_popular_restaurant:
                startActivity(new Intent(context, AllPopularRestaurantActivity.class));
                break;
            case R.id.img_filters:
                startActivity(new Intent(context, FoodFilterActivity.class));
                break;
            case R.id.img_cart:
                startActivity(new Intent(context, FoodOrderDetailActivity.class));
                break;
            case R.id.img_user:
                drawer_food.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_my_wallet:
                drawer_food.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.tv_order_history:
                drawer_food.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, FoodOrderHistoryActivity.class));
                break;
            case R.id.tv_track_order:
                drawer_food.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, FoodTrackOrderActivity.class));
                break;
            case R.id.tv_addresses:
                drawer_food.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, AddressesActivity.class));
                break;
            case R.id.tv_notifications:
                drawer_food.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, NotificationActivity.class));
                break;
            case R.id.tv_invite_friends:
                drawer_food.closeDrawer(Gravity.LEFT);
                //startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.tv_help_and_support:
                drawer_food.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, ContactUsActivity.class));
                break;
            case R.id.tv_log_out:
                drawer_food.closeDrawer(Gravity.LEFT);
                break;
            case R.id.img_discover:
                onBackPressed();
                break;
            case R.id.tv_see_your_playlist:
                startActivity(new Intent(context,AllYourPlaylistActivity.class));
                break;
        }
    }
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
        unregisterNetworkChanges();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CheckInternetEvent event) {
        Log.d("SsS", "checkInternetAvailability: called");
        if (event.isIS_INTERNET_AVAILABLE()) {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_internet_available));

        } else {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_check_internet));
        }
    }
}