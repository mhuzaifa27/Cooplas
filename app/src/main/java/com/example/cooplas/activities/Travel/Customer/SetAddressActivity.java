package com.example.cooplas.activities.Travel.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arsy.maps_library.MapRipple;
import com.example.cooplas.R;
import com.example.cooplas.adapters.Travel.PreviousAddressesAdapter;

import java.util.ArrayList;
import java.util.List;

public class SetAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SetAddressActivity";
    private Context context=SetAddressActivity.this;
    private Activity activity= SetAddressActivity.this;

    private RecyclerView rv_previous_addresses;
    private LinearLayoutManager layoutManager;
    private List<String> previousAddressesList =new ArrayList<>();
    private PreviousAddressesAdapter previousAddressesAdapter;

    private TextView tv_title;
    private RelativeLayout rl_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

        initComponents();
        setData();

        rl_done.setOnClickListener(this::onClick);
    }

    private void setData() {
        previousAddressesList.add("s");
        previousAddressesList.add("s");
        previousAddressesList.add("s");
        previousAddressesList.add("s");
        previousAddressesList.add("s");
    }

    private void initComponents() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);

        rv_previous_addresses =findViewById(R.id.rv_prevois_addresses);
        layoutManager=new LinearLayoutManager(context);
        previousAddressesAdapter=new PreviousAddressesAdapter(previousAddressesList,context);
        rv_previous_addresses.setLayoutManager(layoutManager);
        rv_previous_addresses.setAdapter(previousAddressesAdapter);

        rl_done=findViewById(R.id.rl_done);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_done:
                startActivity(new Intent(context,ReachedAtPickUpActivity.class));
                break;
        }
    }
}