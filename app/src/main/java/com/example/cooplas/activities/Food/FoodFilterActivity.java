package com.example.cooplas.activities.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.example.cooplas.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class FoodFilterActivity extends AppCompatActivity {

    private static final String TAG = "FoodFilterActivity";

    private Context context = FoodFilterActivity.this;
    private Activity activity = FoodFilterActivity.this;

    private ChipGroup chipsPrograms;
    private List<String> typesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_filter);

        //initComponents();

        typesList.add("All");
        typesList.add("Sushi");
        typesList.add("Seafood");
        typesList.add("Pizza");
        typesList.add("Cake");
        typesList.add("Fast Food");
        typesList.add("Snacks");
        typesList.add("Deserts");
        //setCategoryChips(typesList);
    }

    /*private void setCategoryChips(List<String> typesList) {
        for (String category : typesList) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip, null, false);
            mChip.setText(category);
            mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String tag = compoundButton.getText().toString();
                    compoundButton.setChecked(true);
                    compoundButton.setBackgroundColor(getResources().getColor(R.color.colorSecond));
                    compoundButton.setTextColor(getResources().getColor(R.color.white));
                }
            });
            chipsPrograms.addView(mChip);
        }
    }

    private void initComponents() {
        chipsPrograms = findViewById(R.id.chip_types);
    }*/
}