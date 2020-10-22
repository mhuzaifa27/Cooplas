package com.example.cooplas.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cooplas.R;
import com.google.android.material.snackbar.Snackbar;

public class ShowMenu {
    static PopupWindow mypopupWindow = null;

    public static void showPostMenu(Activity activity, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.post_menu, viewGroup,false);

        TextView tv_unfollow = v.findViewById(R.id.tv_unfollow);
        TextView tv_report = v.findViewById(R.id.tv_report);

        mypopupWindow = new PopupWindow(v,300, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        if(v.getParent() != null) {
            ((ViewGroup)v.getParent()).removeView(v); // <- fix
        }
        mypopupWindow.showAsDropDown(view, 0, 0);

        mypopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypopupWindow.dismiss();
            }
        });
    }

    public boolean isShowingMenu() {
        if (mypopupWindow.isShowing()) return true;
        else return false;
    }
}
