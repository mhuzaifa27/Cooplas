package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooplas.R;
import com.example.cooplas.activities.ContactUsActivity;
import com.example.cooplas.activities.Travel.Customer.NotificationActivity;
import com.example.cooplas.activities.Wallet.WalletActivity;
import com.example.cooplas.adapters.Food.FoodCategoriesAdapter;
import com.example.cooplas.adapters.Food.FoodForYouAdapter;
import com.example.cooplas.adapters.Food.FoodNearYouAdapter;
import com.example.cooplas.adapters.Food.FoodPopularRestaurantAdapter;
import com.example.cooplas.models.Food.Callbacks.CallbackGetFood;
import com.example.cooplas.models.Food.Category;
import com.example.cooplas.models.Food.ForYou;
import com.example.cooplas.models.Food.NearYou;
import com.example.cooplas.models.Food.PopularRestaurant;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.jobesk.gong.utils.FunctionsKt;
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

public class FoodActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FoodActivity";
    private Context context = FoodActivity.this;
    private Activity activity = FoodActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;

    private DrawerLayout drawer_food;

    private RecyclerView rv_category, rv_for_you, rv_near_you, rv_popular_restaurants;
    private LinearLayoutManager layoutManagerCategory, layoutManagerForYou, layoutManagerNearYou, layoutManagerPopularRestaurant;

    private List<Category> categoriesList = new ArrayList<>();
    private List<ForYou> forYouList = new ArrayList<>();
    private List<NearYou> nearYouList = new ArrayList<>();
    private List<PopularRestaurant> popularRestaurantList = new ArrayList<>();

    private FoodCategoriesAdapter foodCategoriesAdapter;
    private FoodForYouAdapter foodForYouAdapter;
    private FoodNearYouAdapter foodNearYouAdapter;
    private FoodPopularRestaurantAdapter foodPopularRestaurantAdapter;

    private TextView tv_see_all_categories, tv_see_for_you, tv_see_near_you, tv_see_popular_restaurant;
    private ImageView img_filters, img_cart, img_discover, img_back;
    private CircleImageView img_user;

    private KProgressHUD progressHUD;

    private TextView tv_my_wallet, tv_order_history, tv_track_order, tv_addresses, tv_notifications, tv_invite_friends, tv_help_and_support, tv_log_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        initComponents();
        getFoodData();

        tv_see_all_categories.setOnClickListener(this);
        tv_see_for_you.setOnClickListener(this);
        tv_see_near_you.setOnClickListener(this);
        tv_see_popular_restaurant.setOnClickListener(this);
        img_filters.setOnClickListener(this);
        img_cart.setOnClickListener(this);
        img_user.setOnClickListener(this);

        tv_my_wallet.setOnClickListener(this);
        tv_order_history.setOnClickListener(this);
        tv_track_order.setOnClickListener(this);
        tv_addresses.setOnClickListener(this);
        tv_notifications.setOnClickListener(this);
        tv_invite_friends.setOnClickListener(this);
        tv_help_and_support.setOnClickListener(this);
        tv_log_out.setOnClickListener(this);
        img_discover.setOnClickListener(this);
    }

    private void getFoodData() {
        /*if (swipeRefreshCheck == true) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressHUD.show();
        }*/
        progressHUD.show();
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
        });
    }

    private void setData() {
        foodCategoriesAdapter.addAll(categoriesList);
        foodForYouAdapter.addAll(forYouList);
        foodNearYouAdapter.addAll(nearYouList);
        foodPopularRestaurantAdapter.addAll(popularRestaurantList);

        /***Item Clicks***/
        foodForYouAdapter.OnClickListener(new FoodForYouAdapter.ICLicks() {
            @Override
            public void onFavouriteClick(View view, ForYou forYou, int position) {
                if (forYou.getIsFavourite().equalsIgnoreCase("1")) {
                    makeFoodUnFavourite(forYou.getId().toString(), position);
                } else {
                    makeFoodFavourite(forYou.getId().toString(), position);
                }
            }
        });

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
                    ForYou forYou = forYouList.get(position);
                    forYou.setIsFavourite("0");
                    forYouList.set(position, forYou);
                    foodForYouAdapter.addAll(forYouList);
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
                    ForYou forYou = forYouList.get(position);
                    forYou.setIsFavourite("1");
                    forYouList.set(position, forYou);
                    foodForYouAdapter.addAll(forYouList);
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
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        progressHUD = KProgressHUD.create(activity);

        img_filters = findViewById(R.id.img_filters);
        img_cart = findViewById(R.id.img_cart);
        img_discover = findViewById(R.id.img_discover);
        img_discover.setVisibility(View.VISIBLE);
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.GONE);
        img_user = findViewById(R.id.img_user);

        tv_see_all_categories = findViewById(R.id.tv_see_all_categories);
        tv_see_for_you = findViewById(R.id.tv_see_for_you);
        tv_see_near_you = findViewById(R.id.tv_see_near_you);
        tv_see_popular_restaurant = findViewById(R.id.tv_see_popular_restaurant);

        tv_my_wallet = findViewById(R.id.tv_my_wallet);
        tv_order_history = findViewById(R.id.tv_order_history);
        tv_track_order = findViewById(R.id.tv_track_order);
        tv_addresses = findViewById(R.id.tv_addresses);
        tv_notifications = findViewById(R.id.tv_notifications);
        tv_invite_friends = findViewById(R.id.tv_invite_friends);
        tv_help_and_support = findViewById(R.id.tv_help_and_support);
        tv_log_out = findViewById(R.id.tv_log_out);

        rv_category = findViewById(R.id.rv_category);
        rv_for_you = findViewById(R.id.rv_for_you);
        rv_near_you = findViewById(R.id.rv_near_you);
        rv_popular_restaurants = findViewById(R.id.rv_popular_restaurants);

        layoutManagerCategory = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_category.setLayoutManager(layoutManagerCategory);
        layoutManagerForYou = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rv_for_you.setLayoutManager(layoutManagerForYou);
        layoutManagerNearYou = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rv_near_you.setLayoutManager(layoutManagerNearYou);
        layoutManagerPopularRestaurant = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rv_popular_restaurants.setLayoutManager(layoutManagerPopularRestaurant);

        foodCategoriesAdapter = new FoodCategoriesAdapter(categoriesList, context);
        rv_category.setAdapter(foodCategoriesAdapter);
        foodForYouAdapter = new FoodForYouAdapter(forYouList, context);
        rv_for_you.setAdapter(foodForYouAdapter);
        foodNearYouAdapter = new FoodNearYouAdapter(nearYouList, context);
        rv_near_you.setAdapter(foodNearYouAdapter);
        foodPopularRestaurantAdapter = new FoodPopularRestaurantAdapter(popularRestaurantList, context);
        rv_popular_restaurants.setAdapter(foodPopularRestaurantAdapter);

        drawer_food = findViewById(R.id.drawer_food);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_see_all_categories:
                startActivity(new Intent(context, AllCategoriesActivity.class));
                break;
            case R.id.tv_see_for_you:
                startActivity(new Intent(context, AllForYouActivity.class));
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
                startActivity(new Intent(context, FoodCartActivity.class));
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
                finish();
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