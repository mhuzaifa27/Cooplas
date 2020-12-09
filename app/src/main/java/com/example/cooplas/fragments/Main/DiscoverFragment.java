package com.example.cooplas.fragments.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.activities.Food.FoodActivity;

import com.example.cooplas.activities.MainActivity;
import com.example.cooplas.activities.Music.MusicActivity;
import com.example.cooplas.activities.Music.NowPlayingActivity;
import com.example.cooplas.activities.Music.PlaylistDetailActivity;
import com.example.cooplas.activities.SettingsActivity;
import com.example.cooplas.activities.SigninSignupScreen;

import com.example.cooplas.activities.StoryActivity;
import com.example.cooplas.activities.Travel.Customer.MainCustomerActivity;
import com.example.cooplas.activities.Travel.Customer.SupportActivity;
import com.example.cooplas.activities.Wallet.WalletActivity;
import com.example.cooplas.activities.profile.ProfileActivity;
import com.example.cooplas.models.home.homeFragmentModel.HomeModel;
import com.example.cooplas.models.home.homeFragmentModel.Post;
import com.example.cooplas.models.home.homeFragmentModel.UserStory;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "DiscoverFragment";
    private Context context;
    private Activity activity;
    private CardView card_wallet, card_food, profileContainer, logoutCard, card_travel;
    private CardView card_music,card_settings,card_support,card_coopay,card_nearby,card_notifications;
    private CardView storiesCon;

    private CircleImageView img_user;
    private TextView tv_user_name,tv_user_name_pet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        initComponents(view);
        setData();

        storiesCon.setOnClickListener(this);
        card_music.setOnClickListener(this);
        card_wallet.setOnClickListener(this);
        card_food.setOnClickListener(this);
        card_travel.setOnClickListener(this);
        card_settings.setOnClickListener(this);
        card_support.setOnClickListener(this);
        card_coopay.setOnClickListener(this);
        card_nearby.setOnClickListener(this);
        card_notifications.setOnClickListener(this);

        return view;
    }

    private void setData() {
        tv_user_name.setText(FunctionsKt.getFirstName(context)+" "+FunctionsKt.getLastName(context));
        tv_user_name_pet.setText("@"+FunctionsKt.getUserName(context));

            Glide
                    .with(context)
                    .load(FunctionsKt.getImage(context))
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_dummy_user)
                    .into(img_user);
    }

    private void initComponents(View view) {
        context = getContext();
        activity = getActivity();

        card_music = view.findViewById(R.id.card_music);
        card_wallet = view.findViewById(R.id.card_wallet);
        card_food = view.findViewById(R.id.card_food);
        card_travel = view.findViewById(R.id.card_travel);
        logoutCard = view.findViewById(R.id.logoutCard);
        profileContainer = view.findViewById(R.id.profileContainer);
        card_settings = view.findViewById(R.id.card_settings);
        card_support = view.findViewById(R.id.card_support);
        storiesCon = view.findViewById(R.id.storiesCon);
        card_settings=view.findViewById(R.id.card_settings);
        card_support=view.findViewById(R.id.card_support);
        card_coopay=view.findViewById(R.id.card_coopay);
        card_nearby=view.findViewById(R.id.card_nearby);
        card_notifications=view.findViewById(R.id.card_notifications);

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                FunctionsKt.clearSharedPreference(getActivity());
                MainActivity.navItemIndex=0;
                Intent intent = new Intent(getActivity(), SigninSignupScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);

                startActivity(intent);

            }
        });

        img_user=view.findViewById(R.id.img_user);
        tv_user_name=view.findViewById(R.id.tv_user_name);
        tv_user_name_pet=view.findViewById(R.id.tv_user_name_pet);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.card_music:
                startActivity(new Intent(context, MusicActivity.class));
                break;
            case R.id.card_wallet:
                startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.card_food:
                startActivity(new Intent(context, FoodActivity.class));
                break;
            case R.id.card_travel:
                startActivity(new Intent(context, MainCustomerActivity.class));
                break;
            case R.id.card_settings:
                startActivity(new Intent(context, SettingsActivity.class));
                break;
            case R.id.card_support:
                startActivity(new Intent(context, SupportActivity.class));
                break;
            case R.id.storiesCon:
                getPosts();
                break;
            case R.id.card_coopay:
            case R.id.card_nearby:
            case R.id.card_notifications:
                Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void getPosts() {
        int offsetValue = 0;

        KProgressHUD progressHUD = KProgressHUD.create(getActivity());
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(getContext());
        APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        Call<HomeModel> call = apiInterface.getHomeFeed("Bearer " + accessToken, String.valueOf(offsetValue));
        call.enqueue(new Callback<HomeModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                Log.d("getCommunity", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();

                if (response.isSuccessful()) {
                    HomeModel model = response.body();
                    List<UserStory> listOFStories = new ArrayList<>();
                    listOFStories.addAll(model.getUserStories());

                    if (listOFStories.size()>0){

                        Intent intent = new Intent(activity, StoryActivity.class);
                        intent.putExtra("userStories", listOFStories.get(0));
                        activity.startActivity(intent);
                    }else {

                        Toast.makeText(activity, "you have not uploaded any story yet!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRefresh() {

    }
}
