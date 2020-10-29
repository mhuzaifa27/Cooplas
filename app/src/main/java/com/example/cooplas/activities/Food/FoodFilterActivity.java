package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.cooplas.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

public class FoodFilterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FoodFilterActivity";

    private Context context = FoodFilterActivity.this;
    private Activity activity = FoodFilterActivity.this;

    private ChipGroup chip_types, chip_kitchen, chip_offers;
    private List<String> typesList = new ArrayList<>();
    private List<String> kitchenList = new ArrayList<>();
    private List<String> offersList = new ArrayList<>();

    private ScaleRatingBar srb_1, srb_2, srb_3, srb_4, srb_5;
    private TextView tv_dollar_1,tv_dollar_2,tv_dollar_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filter);

        initComponents();


        typesList.add("All");
        typesList.add("Sushi");
        typesList.add("Seafood");
        typesList.add("Pizza");
        typesList.add("Cake");
        typesList.add("Fast Food");
        typesList.add("Snacks");
        typesList.add("Deserts");

        kitchenList.add("All");
        kitchenList.add("American");
        kitchenList.add("Arabic");
        kitchenList.add("Mexican");
        kitchenList.add("Asian");
        kitchenList.add("European");
        kitchenList.add("Chinese");
        kitchenList.add("Indian");
        kitchenList.add("Italian");
        kitchenList.add("Turkish");
        kitchenList.add("Thai");
        kitchenList.add("Japanese");

        offersList.add("Free Home Delivery");

        setTypesChips(typesList);
        setKitchenChips(kitchenList);
        setOffersChips(offersList);

        srb_1.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                setOneStar();
            }
        });
        srb_2.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                setTwoStar();
            }
        });
        srb_3.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                setThreeStar();
            }
        });
        srb_4.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                setFourStar();
            }
        });
        srb_5.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                setFiveStar();
            }
        });
        /*srb_1.setOnClickListener(this);
        srb_2.setOnClickListener(this);
        srb_3.setOnClickListener(this);
        srb_4.setOnClickListener(this);
        srb_5.setOnClickListener(this);*/

        tv_dollar_1.setOnClickListener(this);
        tv_dollar_2.setOnClickListener(this);
        tv_dollar_3.setOnClickListener(this);

    }

    private void setTypesChips(List<String> typesList) {
        for (String category : typesList) {
            final Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip, null, false);
            mChip.setText(category);
            mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String tag = compoundButton.getText().toString();
                    Log.d(TAG, "onCheckedChanged: " + tag);
                }
            });
            chip_types.addView(mChip);
        }
    }

    private void setKitchenChips(List<String> typesList) {
        for (String category : typesList) {
            final Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip, null, false);
            mChip.setText(category);
            mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String tag = compoundButton.getText().toString();
                    Log.d(TAG, "onCheckedChanged: " + tag);
                }
            });
            chip_kitchen.addView(mChip);
        }
    }

    private void setOffersChips(List<String> typesList) {
        for (String category : typesList) {
            final Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip, null, false);
            mChip.setText(category);
            mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String tag = compoundButton.getText().toString();
                    Log.d(TAG, "onCheckedChanged: " + tag);
                }
            });
            chip_offers.addView(mChip);
        }
    }


    private void initComponents() {
        chip_types = findViewById(R.id.chip_types);
        chip_kitchen = findViewById(R.id.chip_kitchen);
        chip_offers = findViewById(R.id.chip_offers);

        srb_1 = findViewById(R.id.srb_1);
        srb_2 = findViewById(R.id.srb_2);
        srb_3 = findViewById(R.id.srb_3);
        srb_4 = findViewById(R.id.srb_4);
        srb_5 = findViewById(R.id.srb_5);

        tv_dollar_1=findViewById(R.id.tv_dollar_1);
        tv_dollar_2=findViewById(R.id.tv_dollar_2);
        tv_dollar_3=findViewById(R.id.tv_dollar_3);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.srb_1:
                setOneStar();
                break;
            case R.id.srb_2:
                setTwoStar();
                break;
            case R.id.srb_3:
                setThreeStar();
                break;
            case R.id.srb_4:
                setFourStar();
                break;
            case R.id.srb_5:
                setFiveStar();
                break;*/
            case R.id.tv_dollar_1:
                selectDollar1();
                break;
            case R.id.tv_dollar_2:
                selectDollar2();
                break;
            case R.id.tv_dollar_3:
                selectDollar3();
                break;
        }
    }

    private void selectDollar3() {
        tv_dollar_1.setTextColor(getResources().getColor(R.color.light_purple));
        tv_dollar_1.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        tv_dollar_1.setAlpha(Float.parseFloat("0.40"));
        tv_dollar_2.setTextColor(getResources().getColor(R.color.light_purple));
        tv_dollar_2.setAlpha(Float.parseFloat("0.40"));
        tv_dollar_2.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        tv_dollar_3.setTextColor(getResources().getColor(R.color.white));
        tv_dollar_3.setAlpha(Float.parseFloat("1"));
        tv_dollar_3.setBackground(getResources().getDrawable(R.drawable.round_orange));
    }

    private void selectDollar2() {
        tv_dollar_1.setTextColor(getResources().getColor(R.color.light_purple));
        tv_dollar_1.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        tv_dollar_1.setAlpha(Float.parseFloat("0.40"));
        tv_dollar_2.setTextColor(getResources().getColor(R.color.white));
        tv_dollar_2.setAlpha(Float.parseFloat("1"));
        tv_dollar_2.setBackground(getResources().getDrawable(R.drawable.round_orange));
        tv_dollar_3.setTextColor(getResources().getColor(R.color.light_purple));
        tv_dollar_3.setAlpha(Float.parseFloat("0.40"));
        tv_dollar_3.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
    }

    private void selectDollar1() {
        tv_dollar_1.setTextColor(getResources().getColor(R.color.white));
        tv_dollar_1.setBackground(getResources().getDrawable(R.drawable.round_orange));
        tv_dollar_1.setAlpha(Float.parseFloat("1"));
        tv_dollar_2.setTextColor(getResources().getColor(R.color.light_purple));
        tv_dollar_2.setAlpha(Float.parseFloat("0.40"));
        tv_dollar_2.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        tv_dollar_3.setTextColor(getResources().getColor(R.color.light_purple));
        tv_dollar_3.setAlpha(Float.parseFloat("0.40"));
        tv_dollar_3.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
    }

    private void setFiveStar() {
        srb_1.setNumStars(1);
        srb_1.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_2.setNumStars(2);
        srb_2.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_3.setNumStars(3);
        srb_3.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_4.setNumStars(4);
        srb_4.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_5.setMinimumStars(5);
        srb_5.setBackground(getResources().getDrawable(R.drawable.round_orange));
    }

    private void setFourStar() {
        srb_1.setNumStars(1);
        srb_1.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_2.setNumStars(2);
        srb_2.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_3.setNumStars(3);
        srb_3.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_4.setMinimumStars(4);
        srb_4.setBackground(getResources().getDrawable(R.drawable.round_orange));
        srb_5.setNumStars(5);
        srb_5.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
    }

    private void setThreeStar() {
        srb_1.setNumStars(1);
        srb_1.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_2.setNumStars(2);
        srb_2.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_3.setMinimumStars(3);
        srb_3.setBackground(getResources().getDrawable(R.drawable.round_orange));
        srb_4.setNumStars(4);
        srb_4.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_5.setNumStars(5);
        srb_5.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
    }

    private void setTwoStar() {
        srb_1.setNumStars(1);
        srb_1.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_2.setMinimumStars(2);
        srb_2.setBackground(getResources().getDrawable(R.drawable.round_orange));
        srb_3.setNumStars(3);
        srb_3.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_4.setNumStars(4);
        srb_4.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_5.setNumStars(5);
        srb_5.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
    }

    private void setOneStar() {
        srb_1.setMinimumStars(1);
        srb_1.setBackground(getResources().getDrawable(R.drawable.round_orange));
        srb_2.setNumStars(2);
        srb_2.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_3.setNumStars(3);
        srb_3.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_4.setNumStars(4);
        srb_4.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
        srb_5.setNumStars(5);
        srb_5.setBackground(getResources().getDrawable(R.drawable.round_white_black_border));
    }
}