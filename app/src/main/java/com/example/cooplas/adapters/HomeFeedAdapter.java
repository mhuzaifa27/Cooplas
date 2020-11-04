package com.example.cooplas.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.models.homeFragmentModel.HomeModel;
import com.example.cooplas.models.homeFragmentModel.Post;

import java.util.ArrayList;

public class HomeFeedAdapter extends RecyclerView.Adapter {

    private ArrayList<Post> dataList;
    private Activity activity;


    public HomeFeedAdapter(Activity activity, ArrayList<Post> dataList) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataList.get(position).getType()) {
            case 1:
                return Post.TYPE_HORIZONTAL_LIST;
            case 2:
                return Post.TYPE_TEXT;
            case 3:
                return Post.TYPE_IMAGES_SINGLE;
            case 4:
                return Post.TYPE_IMAGES_DOUBLE;
            case 5:
                return Post.TYPE_IMAGES_TRIPPLE;
            case 6:
                return Post.TYPE_IMAGES_MULTIPLE;
            case 7:
                return Post.TYPE_VIDEO;
            default:
                return -1;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Post.TYPE_HORIZONTAL_LIST:

                View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_horizontal_list, parent, false);
                return new ViewHolderHorizontalList(layoutTwo);
            case Post.TYPE_TEXT:

                View layoutThree = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_text, parent, false);
                return new ViewHolderText(layoutThree);
            case Post.TYPE_IMAGES_SINGLE:

                View layoutFour = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_single, parent, false);
                return new ViewHolderImageSingle(layoutFour);

            case Post.TYPE_IMAGES_DOUBLE:

                View layoutFive = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_double, parent, false);
                return new ViewHolderImageDouble(layoutFive);

            case Post.TYPE_IMAGES_TRIPPLE:

                View layoutSix = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_tripple, parent, false);
                return new ViewHolderImageTripple(layoutSix);

            case Post.TYPE_IMAGES_MULTIPLE:

                View layoutSeven = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_image_multiple, parent, false);
                return new ViewHolderImageMultiple(layoutSeven);

            case Post.TYPE_VIDEO:

                View layoutEight = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_video, parent, false);
                return new ViewHolderVideo(layoutEight);

            default:

                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        Log.d("onBindViewHolder", "onBindViewHolder: " + position);

        Post postModel = dataList.get(position);

        switch (dataList.get(position).getType()) {
            case Post.TYPE_HORIZONTAL_LIST:

                ArrayList<String> values = new ArrayList<>();
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");
                values.add("a");


                HomeStoriesAdapter adapter = new HomeStoriesAdapter(values, activity);
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                ((ViewHolderHorizontalList) holder).rv_stories.setLayoutManager(layoutManager);
                ((ViewHolderHorizontalList) holder).rv_stories.setAdapter(adapter);


                break;
            case Post.TYPE_TEXT:
//
//                ((ViewHolderText) holder).menu_right_img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//
//                    }
//                });


                break;
            case Post.TYPE_IMAGES_SINGLE:

//                ((ViewHolderText) holder).menu_right_img


                break;
            case Post.TYPE_IMAGES_TRIPPLE:


                break;
            case Post.TYPE_IMAGES_MULTIPLE:


                break;
            case Post.TYPE_VIDEO:


                break;
            default:
                return;
        }
    }

//    private void popupMenu(String postID, View v) {
//        //Creating the instance of PopupMenu
//        PopupMenu popup = new PopupMenu(activity, v);
//        // to inflate the menu resource (defined in XML) into the PopupMenu
//        popup.getMenuInflater().inflate(R.menu.item_menu, popup.getMenu());
//        //popup with OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.report:
//                        showCustomDialog(postID);
//                        break;
//
//                    default:
//                        break;
//
//                }
//                return false;
//            }
//        });
//
//        popup.show();
//
//    }
//
//

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolderHorizontalList extends RecyclerView.ViewHolder {
        private RecyclerView rv_stories;

        public ViewHolderHorizontalList(@NonNull View itemView) {
            super(itemView);

            rv_stories = itemView.findViewById(R.id.rv_stories);


        }

    }

    static class ViewHolderText extends RecyclerView.ViewHolder {


        public ViewHolderText(@NonNull View itemView) {
            super(itemView);


        }

    }

    static class ViewHolderVideo extends RecyclerView.ViewHolder {


        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);


        }

    }

    static class ViewHolderImageSingle extends RecyclerView.ViewHolder {


        public ViewHolderImageSingle(@NonNull View itemView) {
            super(itemView);


//            iv_profile = itemView.findViewById(R.id.iv_profile);
//            tv_name = itemView.findViewById(R.id.tv_name);
//            tv_time = itemView.findViewById(R.id.);
//            tv_description = itemView.findViewById(R.id.);
//            firt_image_view = itemView.findViewById(R.id.);
//            iv_fav = itemView.findViewById(R.id.);
//            tv_like_count = itemView.findViewById(R.id.);
//            iv_comment = itemView.findViewById(R.id.);
//            tv_comment_count = itemView.findViewById(R.id.);
//            iv_share = itemView.findViewById(R.id.);
//            tv_share = itemView.findViewById(R.id.);
//            iv_write_comment = itemView.findViewById(R.id.);
//            et_comment = itemView.findViewById(R.id.);
//            iv_send = itemView.findViewById(R.id.);


        }

    }

    static class ViewHolderImageDouble extends RecyclerView.ViewHolder {

        public ViewHolderImageDouble(@NonNull View itemView) {
            super(itemView);


        }
    }

    static class ViewHolderImageTripple extends RecyclerView.ViewHolder {


        public ViewHolderImageTripple(@NonNull View itemView) {
            super(itemView);


        }

    }

    static class ViewHolderImageMultiple extends RecyclerView.ViewHolder {


        public ViewHolderImageMultiple(@NonNull View itemView) {
            super(itemView);


        }
    }


    private void shareText(int postID) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareBody = "http://gong/community?post_id=" + postID;
        intent.setType("text/plain");
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(intent, "Share Using:"));
    }
}
