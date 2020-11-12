package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Food.FoodCartAdapter;
import com.example.cooplas.models.Food.Callbacks.CallbackGetCart;
import com.example.cooplas.models.Food.CartItem;
import com.example.cooplas.models.Food.FoodItem;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodCartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FoodCartActivity";
    private Context context = FoodCartActivity.this;
    private Activity activity = FoodCartActivity.this;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;

    private RecyclerView rv_cart;
    private LinearLayoutManager layoutManager;
    private List<CartItem> cartList = new ArrayList<>();
    private FoodCartAdapter foodCartAdapter;

    private TextView tv_title, tv_subtotal, tv_total;
    private Resources resources;
    private RelativeLayout rl_checkout;
    private KProgressHUD progressHUD;
    private int total = 0, subtotal = 0,quantity=0;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart);

        initComponents();
        getCartItems();

        rl_checkout.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    private void getCartItems() {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackGetCart> call = apiInterface.getCart("Bearer " + accessToken);
        call.enqueue(new Callback<CallbackGetCart>() {
            @Override
            public void onResponse(Call<CallbackGetCart> call, Response<CallbackGetCart> response) {
                CallbackGetCart responseGetCart = response.body();
                Log.d(TAG, "onResponse: " + responseGetCart);
                if (responseGetCart != null) {
                    setData(responseGetCart);
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackGetCart> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void setData(CallbackGetCart response) {
        cartList = response.getCart().getCartItems();
        foodCartAdapter.addAll(cartList);
        for (int i = 0; i < cartList.size(); i++) {
            CartItem cartItem = cartList.get(i);
            FoodItem item = cartList.get(i).getFoodItem();
            int itemPrice = Integer.parseInt(item.getPrice());
            int quantity = Integer.parseInt(cartItem.getQuantity());
            subtotal = subtotal + itemPrice * quantity;
        }
        total = subtotal;

        tv_total.setText(total + " usd");
        tv_subtotal.setText(subtotal + " usd");

        progressHUD.dismiss();
    }

    private void initComponents() {
        progressHUD = KProgressHUD.create(context);
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        resources = getResources();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(resources.getString(R.string.cart));
        tv_subtotal = findViewById(R.id.tv_subtotal);
        tv_total = findViewById(R.id.tv_total);

        img_back=findViewById(R.id.img_back);

        rv_cart = findViewById(R.id.rv_cart);

        layoutManager = new LinearLayoutManager(context);
        rv_cart.setLayoutManager(layoutManager);

        foodCartAdapter = new FoodCartAdapter(cartList, context);
        rv_cart.setAdapter(foodCartAdapter);
        rv_cart.setFocusable(false);

        /**Food Adapter Clicks**/
        foodCartAdapter.SetOnIClickListener(new FoodCartAdapter.IClicks() {
            @Override
            public void onRemoveItemClick(View view, CartItem cartItem, int position) {
                removeCartItem(cartItem, position);
            }

            @Override
            public void onPlusQuantityClick(View view, CartItem cartItem, int position) {
                quantity=Integer.parseInt(cartItem.getQuantity());
                quantity=quantity+1;
                Map<String,String> params=new HashMap<>();
                params.put(Constants.QUANTITY,String.valueOf(quantity));
                updateCartItem(cartItem,params,position,1);
            }

            @Override
            public void onMinusQuantityClick(View view, CartItem cartItem, int position) {
                quantity=Integer.parseInt(cartItem.getQuantity());
                if(quantity>1){
                    quantity=quantity-1;
                    Map<String,String> params=new HashMap<>();
                    params.put(Constants.QUANTITY,String.valueOf(quantity));
                    updateCartItem(cartItem,params,position,0);
                }
            }
        });
        /****/

        rl_checkout = findViewById(R.id.rl_checkout);
    }

    private void updateCartItem(CartItem cartItem, Map<String, String> params, int position, int val) {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.updateCartItem("Bearer " + accessToken,params,String.valueOf(cartItem.getId()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseGetCart = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetCart != null) {
                    progressHUD.dismiss();
                    updateTotal(cartItem,val,position);
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

    private void updateTotal(CartItem cartItem, int val, int position) {
        if(val==0){
            subtotal=Integer.parseInt(tv_subtotal.getText().toString().split(" ")[0])-Integer.parseInt(cartItem.getFoodItem().getPrice());
            total=Integer.parseInt(tv_total.getText().toString().split(" ")[0])-Integer.parseInt(cartItem.getFoodItem().getPrice());
        }
        else{
            subtotal=Integer.parseInt(tv_subtotal.getText().toString().split(" ")[0])+Integer.parseInt(cartItem.getFoodItem().getPrice());
            total=Integer.parseInt(tv_total.getText().toString().split(" ")[0])+Integer.parseInt(cartItem.getFoodItem().getPrice());
        }
        cartItem.setQuantity(String.valueOf(quantity));
        cartList.set(position,cartItem);
        foodCartAdapter.addAll(cartList);
        quantity=0;
        tv_subtotal.setText(subtotal+" usd");
        tv_total.setText(total+" usd");
    }

    private void removeCartItem(CartItem cartItem, int position) {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.removeCartItem("Bearer " + accessToken,cartItem.getId().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseGetCart = response.body();
                Log.d(TAG, "onResponse: " + responseGetCart);
                if (responseGetCart != null) {
                    updateCartList(cartItem,position);
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

    private void updateCartList(CartItem cartItem, int position) {
        quantity=Integer.parseInt(cartItem.getQuantity());
        int price=Integer.parseInt(cartItem.getFoodItem().getPrice());
        int valueToMinus=price*quantity;

        subtotal=Integer.parseInt(tv_subtotal.getText().toString().split(" ")[0]);
        total=Integer.parseInt(tv_total.getText().toString().split(" ")[0]);

        if(subtotal>=valueToMinus && total>=valueToMinus){
            subtotal=subtotal-valueToMinus;
            total=total-valueToMinus;

            tv_subtotal.setText(subtotal+" usd");
            tv_total.setText(total+" usd");
        }
        foodCartAdapter.remove(cartList.get(position));
        //cartList.remove(position);
        quantity=0;
        progressHUD.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_checkout:
                startActivity(new Intent(context, CheckoutActivity.class));
                break;
            case R.id.img_back:
                onBackPressed();
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