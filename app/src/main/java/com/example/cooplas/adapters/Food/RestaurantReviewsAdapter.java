package com.example.cooplas.adapters.Food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.models.Food.Review;
import com.example.cooplas.models.Food.Review;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantReviewsAdapter extends RecyclerView.Adapter<RestaurantReviewsAdapter.TravelBuddyViewHolder> {
    private List<Review> data;
    private Context context;
    private boolean isLoadingAdded;

    public RestaurantReviewsAdapter(List<Review> data, Context context) {
        this.data = data;
        this.context = context;
    }
    public void add(Review mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<Review> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(Review city) {
        int position = data.indexOf(city);
        if (position > -1) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Review());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        Review item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
    public Review getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_restaurant_review, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        Review review = data.get(position);

        if (review.getUser().getFirstName() != null && review.getUser().getLastName()!=null)
            holder.tv_user_name.setText(review.getUser().getFirstName() +" "+ review.getUser().getLastName());
        if (review.getComments() != null)
            holder.tv_comment.setText(review.getComments());
        /*if (review.getCreatedAt() != null)
            holder.tv_date.setText(review.getCreatedAt());*/
        if (review.getStars() != null)
            holder.rb_rating.setRating(Float.parseFloat(review.getStars()));

        if (review.getUser().getProfilePic() != null) {
            Glide
                    .with(context)
                    .load(review.getUser().getProfilePic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_dummy_user)
                    .into(holder.img_user);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name, tv_comment, tv_date;
        CircleImageView img_user;
        RatingBar rb_rating;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_user_name=itemView.findViewById(R.id.tv_user_name);
            tv_comment=itemView.findViewById(R.id.tv_comment);
            tv_date=itemView.findViewById(R.id.tv_date);

            img_user=itemView.findViewById(R.id.img_user);
            rb_rating=itemView.findViewById(R.id.rb_rating);
        }
    }
}
