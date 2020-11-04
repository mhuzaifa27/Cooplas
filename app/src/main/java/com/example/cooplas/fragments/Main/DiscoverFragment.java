package com.example.cooplas.fragments.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cooplas.R;
import com.example.cooplas.activities.Food.FoodActivity;
import com.example.cooplas.activities.LikesScreen;
import com.example.cooplas.activities.SigninSignupScreen;

import com.example.cooplas.activities.Travel.Customer.MainCustomerActivity;
import com.example.cooplas.activities.Wallet.WalletActivity;
import com.jobesk.gong.utils.FunctionsKt;

public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "DiscoverFragment";
    private Context context;
    private Activity activity;
    private CardView card_wallet, card_food, logoutCard, card_travel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        initComponents(view);

        card_wallet.setOnClickListener(this);
        card_food.setOnClickListener(this);
        card_travel.setOnClickListener(this);

        return view;
    }

    private void initComponents(View view) {
        context = getContext();
        activity = getActivity();

        card_wallet = view.findViewById(R.id.card_wallet);
        card_food = view.findViewById(R.id.card_food);
        card_travel = view.findViewById(R.id.card_travel);
        logoutCard = view.findViewById(R.id.logoutCard);

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionsKt.clearSharedPreference(getActivity());
                Intent intent = new Intent(getActivity(), SigninSignupScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_wallet:
                startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.card_food:
                startActivity(new Intent(context, FoodActivity.class));
                break;
            case R.id.card_travel:
                startActivity(new Intent(context, MainCustomerActivity.class));
                break;
        }
    }
}
