package com.example.cooplas.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.activities.MainActivity;
import com.example.cooplas.models.home.singlePost.Comment;
import com.example.cooplas.utils.CircleTransform;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> data;
    private Context context;
    private Activity activity;

    public CommentAdapter(List<Comment> data, Activity activity) {

        this.data = data;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment comment = data.get(position);

        String image = comment.getUser().getProfilePic();
        Picasso.get().load(image).fit().centerCrop().transform(new CircleTransform()).into(holder.iv_comment_profile);
        holder.tv_comment_name.setText(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
        holder.tv_comment_body.setText(comment.getBody());
        holder.tv_comment_time.setText(comment.getCreatedAt());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                alertSelection(comment, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_comment_profile;
        private TextView tv_comment_name, tv_comment_time, tv_comment_body;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_comment_profile = itemView.findViewById(R.id.iv_comment_profile);
            tv_comment_name = itemView.findViewById(R.id.tv_comment_name);
            tv_comment_body = itemView.findViewById(R.id.tv_comment_body);
            tv_comment_time = itemView.findViewById(R.id.tv_comment_time);
        }
    }

    private void alertSelection(Comment commentModel, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle("Choose an animal");
        String[] animals = {"Update Comment", "Delete Comment"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Update
//                        updateComment(commentID, position);
                        showAlertUpdate(commentModel, position);
                        break;
                    case 1: // Delete
                        deleteComment(commentModel.getId(), position);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteComment(int postID, int position) {
        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(activity);

        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.deleteComment("Bearer " + accessToken, String.valueOf(postID));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postDel", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
                    data.remove(position);
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    private void updateComment(int postID, int position, String body) {

        KProgressHUD progressHUD = KProgressHUD.create(activity);
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(activity);
        APIInterface apiInterface = APIClient.getClient(activity).create(APIInterface.class);
        Call<JsonObject> call = apiInterface.updateComment("Bearer " + accessToken, String.valueOf(postID), body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("postDel", "" + new Gson().toJson(response.body()));
                progressHUD.dismiss();
                if (response.isSuccessful()) {
//                    data.remove(position);
//                    notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("onFailure", t + "");
                call.cancel();
                progressHUD.dismiss();
            }
        });
    }

    private void showAlertUpdate(Comment commentModel, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialouge_comment_update, null);
        builder.setView(dialogView);
        ImageView iv_comment_profile = dialogView.findViewById(R.id.iv_comment_profile);
        TextView tv_comment_name = dialogView.findViewById(R.id.tv_comment_name);
        TextView tv_filled = dialogView.findViewById(R.id.tv_filled);
        EditText tv_comment_body_et = dialogView.findViewById(R.id.tv_comment_body_et);
        String userName = commentModel.getUser().getFirstName() + " " + commentModel.getUser().getLastName();
        tv_comment_name.setText(userName);
        String profilePic = commentModel.getUser().getProfilePic();
        Picasso.get().load(profilePic).fit().centerCrop().transform(new CircleTransform()).into(iv_comment_profile);
        tv_comment_body_et.setText(commentModel.getBody());
        final AlertDialog dialog = builder.create();
        tv_filled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = tv_comment_body_et.getText().toString().trim();
                if (comment.isEmpty()) {
                    Toast.makeText(context, "Please Enter Comment!", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.get(position).setBody(comment);
                notifyItemChanged(position);
                updateComment(commentModel.getId(), position, comment);
                dialog.dismiss();
            }
        });
        // Display the custom alert dialog on interface
        dialog.show();
    }
}
