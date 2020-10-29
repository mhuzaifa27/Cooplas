package com.example.cooplas.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;

import com.example.cooplas.R;

public class CustomSwitchButton extends Switch {

    public CustomSwitchButton(Context context) {
        super(context);
    }

    public CustomSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        changeColor(checked);
    }

    private void changeColor(boolean isChecked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int thumbColor;
            int trackColor;

            if (isChecked) {
                thumbColor = getResources().getColor(R.color.colorSecond);
                //trackColor = thumbColor;
            } else {
                thumbColor = getResources().getColor(R.color.black);
                //trackColor = thumbColor;
            }

            try {
                getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
                //getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.MULTIPLY);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
