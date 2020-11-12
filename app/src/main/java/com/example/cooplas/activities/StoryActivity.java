package com.example.cooplas.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cooplas.R;
import com.example.cooplas.models.home.homeFragmentModel.Story;
import com.example.cooplas.models.home.homeFragmentModel.UserStory;
import com.example.cooplas.utils.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
    private StoriesProgressView storiesProgressView;
    private ProgressBar progressBar;
    private ImageView storyImage;
    private int counter = 0;
    private ArrayList<String> arrayList;
    private ConstraintLayout arrow_left, bottin_point, center_point, arrow_right;
    private ImageView user_image;
    private TextView userName_tv, post_time_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story);


        Intent intent = getIntent();
        UserStory myParcelableObject = intent.getParcelableExtra("userStories");

        List<Story> story = myParcelableObject.getStories();
        Collections.reverse(story);


        arrayList = new ArrayList<>();
        for (int i = 0; i < story.size(); i++) {

            String mediaPath = story.get(i).getMedia();
            arrayList.add(mediaPath);
        }


        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(arrayList.size()); // <- set stories
        storiesProgressView.setStoryDuration(6000L); // <- set a story duration
        storiesProgressView.setStoriesListener(this); // <- set listener
        storiesProgressView.startStories(); // <- start progress


        arrow_left = findViewById(R.id.arrow_left);
        arrow_right = findViewById(R.id.arrow_right);
        center_point = findViewById(R.id.center_point);


        user_image = findViewById(R.id.user_image);
        userName_tv = findViewById(R.id.userName_tv);
        post_time_tv = findViewById(R.id.post_time_tv);


        String firstName = myParcelableObject.getFirstName();
        String getDate = myParcelableObject.getStories().get(0).getCreatedAt();
        String getPic = myParcelableObject.getProfilePic();

        userName_tv.setText(firstName);
        post_time_tv.setText(getDate);

        Picasso.get().load(getPic).fit().centerCrop().into(user_image);


        storyImage = findViewById(R.id.storyImage);
        bottin_point = findViewById(R.id.bottin_point);

        storiesProgressView.pause();
        setImages(arrayList.get(counter));

        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter <= 0) {
                    return;
                }

                storiesProgressView.reverse();

            }
        });

        arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter > arrayList.size()) {

                    finish();
                }

                storiesProgressView.skip();

            }
        });


        center_point.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        storiesProgressView.pause();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        storiesProgressView.resume();
                        return true; // if you want to handle the touch event

                }

                return false;
            }
        });


        bottin_point.setOnTouchListener(new OnSwipeTouchListener(StoryActivity.this) {
            public void onSwipeTop() {
//                Toast.makeText(MyActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
//                Toast.makeText(MyActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
//                Toast.makeText(MyActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
//                Toast.makeText(MyActivity.this, "bottom", Toast.LENGTH_SHORT).show();

                storiesProgressView.destroy();
                finish();
            }

        });


    }

    private void setImages(String image) {

        progressBar = (ProgressBar) findViewById(R.id.progress);

//        storiesProgressView.pause();
        Glide.with(this)
                .load(image)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        storiesProgressView.resume();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        storiesProgressView.resume();
                        return false;
                    }
                })
                .into(storyImage);

    }

    @Override
    public void onNext() {
        // Toast.makeText(this, "onNext", Toast.LENGTH_SHORT).show();
        counter = counter + 1;

        if (counter > arrayList.size()) {

            finish();
        }
        setImages(arrayList.get(counter));
    }

    @Override
    public void onPrev() {
        // Call when finished revserse animation.
        //  Toast.makeText(this, "onPrev", Toast.LENGTH_SHORT).show();
        counter = counter - 1;
        if (counter < 0) {
            finish();
        }
        setImages(arrayList.get(counter));
    }

    @Override
    public void onComplete() {
        //  Toast.makeText(this, "onComplete", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();

    }
}