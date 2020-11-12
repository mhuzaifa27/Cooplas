package com.example.cooplas.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.models.Food.Callbacks.CallbackGetRestaurantDetail;
import com.example.cooplas.models.Food.ForYou;
import com.example.cooplas.models.Food.Menu;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.android.material.snackbar.Snackbar;
import com.jobesk.gong.utils.FunctionsKt;
import com.joooonho.SelectableRoundedImageView;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDialogues {

    private static final String TAG = "ShowDialogues";

    /*public static void SHOW_DATE_PICKER_DIALOG(Context context, final TextView tv, DialogInterface.OnDismissListener onDismissListener){
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialogue_date_picker);
        alertDialog.setOnDismissListener(onDismissListener);
        final DatePicker datePicker = alertDialog.findViewById(R.id.date_picker);
        Calendar c = Calendar.getInstance();
        datePicker.setMaxDate(c.getTimeInMillis());
        Button btn_ok=alertDialog.findViewById(R.id.btn_ok);
        Button btn_cancel=alertDialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year=datePicker.getYear();
                int monthOfYear=datePicker.getMonth();
                int dayOfMonth=datePicker.getDayOfMonth();
                String dateYouChoose = null;
                if(dayOfMonth<10 && monthOfYear<10)
                    dateYouChoose= "0"+dayOfMonth + "-" + "0"+(monthOfYear + 1) + "-" + year;
                else if(dayOfMonth<10)
                    dateYouChoose= "0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                else
                    dateYouChoose= dayOfMonth + "-" + "0"+(monthOfYear + 1) + "-" + year;
                tv.setText(dateYouChoose);
                alertDialog.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }*/

    public static void SHOW_NO_INTERNET_DIALOG(Context context) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialogue_no_internet);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /*alertDialog.findViewById(R.id.tv_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });*/
        alertDialog.show();
    }
    public static void SHOW_SERVER_ERROR_DIALOG(Context context) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_server_error);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*alertDialog.findViewById(R.id.tv_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });*/
        alertDialog.show();
    }
    public static void SHOW_ADD_TO_CART_DIALOG(Menu menu, Context context) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_add_food_to_cart);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_quantity=alertDialog.findViewById(R.id.tv_quantity);
        TextView tv_food_name=alertDialog.findViewById(R.id.tv_food_name);
        TextView tv_price=alertDialog.findViewById(R.id.tv_price);
        SelectableRoundedImageView img_food=alertDialog.findViewById(R.id.img_food);

        tv_food_name.setText(menu.getName());
        tv_price.setText("$"+menu.getPrice());
        if (menu.getCoverPic() != null) {
            Glide
                    .with(context)
                    .load(menu.getCoverPic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(img_food);
        }
        alertDialog.findViewById(R.id.img_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=Integer.parseInt(tv_quantity.getText().toString());
                    quantity=quantity+1;
                    tv_quantity.setText(String.valueOf(quantity));
            }
        });
        alertDialog.findViewById(R.id.img_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=Integer.parseInt(tv_quantity.getText().toString());
                if(quantity>1){
                    quantity=quantity-1;
                    tv_quantity.setText(String.valueOf(quantity));
                }
            }
        });
        alertDialog.findViewById(R.id.rl_add_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> params=new HashMap<>();
                params.put(Constants.ITEM_ID,menu.getId().toString());
                params.put(Constants.QUANTITY,tv_quantity.getText().toString());
                params.put(Constants.SIZE,"S");
                alertDialog.dismiss();
                addToCart(context,alertDialog,params);
            }
        });
        alertDialog.show();
    }
    public static void SHOW_ADD_TO_CART_DIALOG(ForYou menu, Context context) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_add_food_to_cart);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_quantity=alertDialog.findViewById(R.id.tv_quantity);
        TextView tv_food_name=alertDialog.findViewById(R.id.tv_food_name);
        TextView tv_price=alertDialog.findViewById(R.id.tv_price);
        SelectableRoundedImageView img_food=alertDialog.findViewById(R.id.img_food);

        tv_food_name.setText(menu.getName());
        tv_price.setText("$"+menu.getPrice());
        if (menu.getCoverPic() != null) {
            Glide
                    .with(context)
                    .load(menu.getCoverPic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_place_holder_image)
                    .into(img_food);
        }
        alertDialog.findViewById(R.id.img_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=Integer.parseInt(tv_quantity.getText().toString());
                quantity=quantity+1;
                tv_quantity.setText(String.valueOf(quantity));
            }
        });
        alertDialog.findViewById(R.id.img_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=Integer.parseInt(tv_quantity.getText().toString());
                if(quantity>1){
                    quantity=quantity-1;
                    tv_quantity.setText(String.valueOf(quantity));
                }
            }
        });
        alertDialog.findViewById(R.id.rl_add_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> params=new HashMap<>();
                params.put(Constants.ITEM_ID,menu.getId().toString());
                params.put(Constants.QUANTITY,tv_quantity.getText().toString());
                params.put(Constants.SIZE,"S");
                alertDialog.dismiss();
                addToCart(context,alertDialog,params);
            }
        });
        alertDialog.show();
    }

    private static void addToCart(Context context, Dialog alertDialog, Map<String, String> params) {
        KProgressHUD progressHUD = KProgressHUD.create(context);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.addToCart("Bearer " + accessToken, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body() != null) {
                    //alertDialog.dismiss();
                    progressHUD.dismiss();
                    Toast.makeText(context, "Item added to cart!", Toast.LENGTH_SHORT).show();
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

    public static void SHOW_TICKET_DETAIL_DIALOG(Context context) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_support_ticket_detail);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    public static void SHOW_SNACK_BAR(View parentLayout, Activity activity, String text) {
        Snackbar snackbar = Snackbar.make(parentLayout, text, Snackbar.LENGTH_LONG);
        View customView = activity.getLayoutInflater().inflate(R.layout.snackbar_internet_connection, null);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        TextView tv_subject = customView.findViewById(R.id.tv_subject);
        tv_subject.setText(text);
        snackbarLayout.addView(customView);
        snackbar.show();
    }
}
