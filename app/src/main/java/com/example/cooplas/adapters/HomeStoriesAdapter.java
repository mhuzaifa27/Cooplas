package com.example.cooplas.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cooplas.R;
import com.example.cooplas.activities.StoryActivity;
import com.example.cooplas.events.AddStoryEvent;
import com.example.cooplas.models.home.homeFragmentModel.Story;
import com.example.cooplas.models.home.homeFragmentModel.UserStory;
import com.jobesk.gong.utils.FunctionsKt;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class HomeStoriesAdapter extends RecyclerView.Adapter<HomeStoriesAdapter.TravelBuddyViewHolder> {
    private List<UserStory> data;
    private Activity activity;

    public HomeStoriesAdapter(List<UserStory> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_storey, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        UserStory model = data.get(position);

        if (position == 0) {
            holder.story_card_cv.setVisibility(View.GONE);
            holder.add_card_cv.setVisibility(View.VISIBLE);
            String userImage = FunctionsKt.getImage(activity);
            Picasso.get().load(userImage).fit().centerCrop().into(holder.current_user_image);
        } else {
            holder.story_card_cv.setVisibility(View.VISIBLE);
            holder.add_card_cv.setVisibility(View.GONE);

            String firstName = model.getFirstName();
            String profilePic = model.getProfilePic();
            String firstImageStory = model.getStories().get(0).getMedia();

            holder.userName_tv.setText(firstName);

            Picasso.get().load(profilePic).fit().centerCrop().into(holder.userImage);

            Picasso.get().load(firstImageStory).fit().centerCrop().into(holder.story_image);

        }

        holder.story_card_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, StoryActivity.class);
                intent.putExtra("userStories", model);
                activity.startActivity(intent);
            }
        });


        holder.add_card_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new AddStoryEvent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage, story_image, current_user_image;
        private TextView userName_tv;
        private CardView story_card_cv, add_card_cv;


        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            current_user_image = itemView.findViewById(R.id.current_user_image);
            story_image = itemView.findViewById(R.id.story_image);
            userName_tv = itemView.findViewById(R.id.userName_tv);
            story_card_cv = itemView.findViewById(R.id.story_card_cv);
            add_card_cv = itemView.findViewById(R.id.add_card_cv);
        }
    }
}
