package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;
import com.example.cooplas.adapters.Food.RestaurantMenuAdapter;
import com.example.cooplas.adapters.Food.RestaurantReviewsAdapter;
import com.example.cooplas.models.Food.Callbacks.CallbackGetFoodForYou;
import com.example.cooplas.models.Food.Callbacks.CallbackGetRestaurantDetail;
import com.example.cooplas.models.Food.ForYou;
import com.example.cooplas.models.Food.Menu;
import com.example.cooplas.models.Food.Review;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.jobesk.gong.utils.FunctionsKt;
import com.joooonho.SelectableRoundedImageView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RestaurantDetailActivit";
    private Context context = RestaurantDetailActivity.this;
    private Activity activity = RestaurantDetailActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;

    private RecyclerView rv_menu, rv_reviews;
    private List<Menu> listMenu = new ArrayList<>();
    private List<Review> listReviews = new ArrayList<>();
    private LinearLayoutManager menuLayoutManager, reviewsLayoutManager;

    private RestaurantMenuAdapter restaurantMenuAdapter;
    private RestaurantReviewsAdapter restaurantReviewsAdapter;

    private TextView tv_about_us, tv_about_us_text, tv_reviews;
    private CircleImageView img_user;

    private ImageView img_cart,img_back;
    private SelectableRoundedImageView img_restaurant;
    private TextView tv_restaurant_name, tv_location, tv_reviews_count, tv_rating;

    private TextView tv_no_menu, tv_no_reviews, tv_no_about_us;

    private KProgressHUD progressHUD;
    private String restaurantID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        initComponents();
        getIntentData();
        getRestaurantDetail();

        tv_reviews.setOnClickListener(this);
        tv_about_us.setOnClickListener(this);
        img_cart.setOnClickListener(this);
    }

    private void getIntentData() {
        restaurantID = getIntent().getStringExtra(Constants.ID);
    }

    private void getRestaurantDetail() {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackGetRestaurantDetail> call = apiInterface.getRestaurantDetail("Bearer " + accessToken, restaurantID);
        call.enqueue(new Callback<CallbackGetRestaurantDetail>() {
            @Override
            public void onResponse(Call<CallbackGetRestaurantDetail> call, Response<CallbackGetRestaurantDetail> response) {
                CallbackGetRestaurantDetail responseGetRestaurantDetail = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetRestaurantDetail != null) {
                    setData(responseGetRestaurantDetail);
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackGetRestaurantDetail> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void setData(CallbackGetRestaurantDetail response) {
        if (response.getCoverPic() != null) {
            Glide
                    .with(context)
                    .load(response.getCoverPic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(img_restaurant);
        }
        if (response.getName() != null)
            tv_restaurant_name.setText(response.getName());
        if (response.getReviewsCount() != null)
            tv_reviews_count.setText("(" + response.getReviewsCount() + ")");
        if (response.getStars() != null)
            tv_rating.setText(response.getStars());
        if (response.getLocationName() != null)
            tv_location.setText(response.getLocationName());
        if (response.getAboutUs() != null)
            tv_about_us_text.setText(response.getAboutUs());
        else {
            tv_about_us_text.setVisibility(View.GONE);
            tv_no_about_us.setVisibility(View.VISIBLE);
        }

        if (response.getMenu() != null) {
            if (response.getMenu().size() > 0) {
                if (response.getReviews().size() > 0) {
                    tv_no_menu.setVisibility(View.GONE);
                    rv_menu.setVisibility(View.VISIBLE);
                    listMenu = response.getMenu();
                    restaurantMenuAdapter.addAll(response.getMenu());
                } else {
                    tv_no_menu.setVisibility(View.VISIBLE);
                    rv_menu.setVisibility(View.GONE);
                }
            }
        } else {
            tv_no_menu.setVisibility(View.VISIBLE);
            rv_menu.setVisibility(View.GONE);
        }
        if (response.getReviews() != null) {
            if (response.getReviews().size() > 0) {
                tv_no_reviews.setVisibility(View.GONE);
                listReviews = response.getReviews();
                restaurantReviewsAdapter.addAll(response.getReviews());
            } else {
                tv_no_reviews.setVisibility(View.VISIBLE);
                rv_reviews.setVisibility(View.GONE);
            }
        } else {
            tv_no_reviews.setVisibility(View.VISIBLE);
            rv_reviews.setVisibility(View.GONE);
        }
        /***Item Clicks***/
        restaurantMenuAdapter.OnClickListener(new RestaurantMenuAdapter.ICLicks() {
            @Override
            public void onFavouriteClick(View view, Menu forYou, int position) {
                if (forYou.getIsFavourite().equalsIgnoreCase("1")) {
                    makeFoodUnFavourite(forYou.getId().toString(), position);
                } else {
                    makeFoodFavourite(forYou.getId().toString(), position);
                }
            }
        });


        setAboutUsView();
        progressHUD.dismiss();
    }

    private void makeFoodUnFavourite(String id, int position) {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.makeFoodUnFavourite("Bearer " + accessToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseGetFood = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetFood != null) {
                    progressHUD.dismiss();
                    Menu menu = listMenu.get(position);
                    menu.setIsFavourite("0");
                    listMenu.set(position, menu);
                    restaurantMenuAdapter.addAll(listMenu);
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void makeFoodFavourite(String id, int position) {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.makeFoodFavourite("Bearer " + accessToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseGetFood = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetFood != null) {
                    progressHUD.dismiss();
                    Menu menu = listMenu.get(position);
                    menu.setIsFavourite("1");
                    listMenu.set(position, menu);
                    restaurantMenuAdapter.addAll(listMenu);
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void initComponents() {
        progressHUD = KProgressHUD.create(activity);

        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        rv_menu = findViewById(R.id.rv_menu);
        rv_reviews = findViewById(R.id.rv_reviews);

        menuLayoutManager = new LinearLayoutManager(context);
        reviewsLayoutManager = new LinearLayoutManager(context);

        restaurantMenuAdapter = new RestaurantMenuAdapter(listMenu, context);
        restaurantReviewsAdapter = new RestaurantReviewsAdapter(listReviews, context);

        rv_menu.setLayoutManager(menuLayoutManager);
        rv_reviews.setLayoutManager(reviewsLayoutManager);

        rv_menu.setAdapter(restaurantMenuAdapter);
        rv_reviews.setAdapter(restaurantReviewsAdapter);

        tv_about_us = findViewById(R.id.tv_about_us);
        tv_about_us_text = findViewById(R.id.tv_about_us_text);
        tv_reviews = findViewById(R.id.tv_reviews);
        tv_restaurant_name = findViewById(R.id.tv_restaurant_name);
        tv_rating = findViewById(R.id.tv_rating);
        tv_reviews_count = findViewById(R.id.tv_reviews_count);
        tv_location = findViewById(R.id.tv_location);

        tv_no_menu = findViewById(R.id.tv_no_menu);
        tv_no_reviews = findViewById(R.id.tv_no_reviews);
        tv_no_about_us = findViewById(R.id.tv_no_about_us);

        img_user = findViewById(R.id.img_user);
        img_user.setVisibility(View.GONE);
        img_restaurant = findViewById(R.id.img_restaurant);
        img_cart = findViewById(R.id.img_cart);
         img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_about_us:
                setAboutUsView();
                break;
            case R.id.tv_reviews:
                setReviewView();
                break;
            case R.id.img_cart:
                startActivity(new Intent(context, FoodCartActivity.class));
                break;
        }
    }

    private void setAboutUsView() {
        if (tv_no_about_us.getText().toString() != null) {
            rv_reviews.setVisibility(View.GONE);
            tv_about_us_text.setVisibility(View.VISIBLE);
            tv_no_about_us.setVisibility(View.GONE);
            tv_no_reviews.setVisibility(View.GONE);
        } else {
            rv_reviews.setVisibility(View.GONE);
            tv_about_us_text.setVisibility(View.GONE);
            tv_no_about_us.setVisibility(View.VISIBLE);
            tv_no_reviews.setVisibility(View.GONE);
        }
        tv_about_us.setTextColor(getResources().getColor(R.color.colorSecond));
        tv_reviews.setTextColor(getResources().getColor(R.color.grey));
    }

    private void setReviewView() {
        if (listReviews.size() > 0) {
            rv_reviews.setVisibility(View.VISIBLE);
            tv_about_us_text.setVisibility(View.GONE);
            tv_no_about_us.setVisibility(View.GONE);
            tv_no_reviews.setVisibility(View.GONE);
        } else {
            rv_reviews.setVisibility(View.GONE);
            tv_about_us_text.setVisibility(View.GONE);
            tv_no_about_us.setVisibility(View.GONE);
            tv_no_reviews.setVisibility(View.VISIBLE);
        }
        tv_about_us.setTextColor(getResources().getColor(R.color.grey));
        tv_reviews.setTextColor(getResources().getColor(R.color.colorSecond));
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