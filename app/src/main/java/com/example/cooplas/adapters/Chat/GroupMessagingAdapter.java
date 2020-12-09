package com.example.cooplas.adapters.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.cooplas.Firebase.AppState;
import com.example.cooplas.Firebase.ChangeEventListener;
import com.example.cooplas.Firebase.Models.Message;
import com.example.cooplas.Firebase.Models.User;
import com.example.cooplas.Firebase.Services.UserService;
import com.example.cooplas.R;
import com.example.cooplas.activities.home.ImageViewActivity;
import com.example.cooplas.activities.home.VideoViewActivity;
import com.example.cooplas.utils.AudioPlayer;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.SessionManager;
import com.example.cooplas.utils.TimeUtils;
import com.google.firebase.database.DatabaseError;
import com.jobesk.gong.utils.FunctionsKt;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> data;
    private Context context;
    private Activity activity;
    private SessionManager sessionManager;
    private boolean isLoadingAdded = false;
    private AudioPlayer audioPlayer;
    private UserService userService;

    public GroupMessagingAdapter(List<Message> data, Context context, Activity activity) {
        this.data = data;
        this.context = context;
        this.activity = activity;
        sessionManager = new SessionManager(context);
        audioPlayer = new AudioPlayer(false);
    }

    public void add(Message mc) {
        data.add(0, mc);
        if (data.size() > 1)
            notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public void addAll(List<Message> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(Message
                               city) {
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
        add(new Message
                ());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        Message
                item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Message
    getItem(int position) {
        return data.get(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getMessageBy().equalsIgnoreCase(AppState.currentBpackCustomer.getUserId())) {
            if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_TEXT))
                return 0;
            else if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_AUDIO))
                return 2;
            else if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_IMAGE))
                return 4;
            else if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_VIDEO))
                return 6;
            else return -1;
        } else {
            if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_TEXT))
                return 1;
            else if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_AUDIO))
                return 3;
            else if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_IMAGE))
                return 5;
            else if (data.get(position).getType().equalsIgnoreCase(Constants.TYPE_VIDEO))
                return 7;
            else
                return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.item_sent_message, parent, false);
                holder = new SenderTextViewHolder(view);
                break;
            case 2:
                view = inflater.inflate(R.layout.item_sent_audio, parent, false);
                holder = new SenderAudioViewHolder(view);
                break;
            case 4:
                view = inflater.inflate(R.layout.item_sent_image, parent, false);
                holder = new SenderImageViewHolder(view);
                break;
            case 6:
                view = inflater.inflate(R.layout.item_sent_video, parent, false);
                holder = new SenderVideoViewHolder(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.item_group_received_message, parent, false);
                holder = new ReceiverTextViewHolder(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.item_received_audio, parent, false);
                holder = new ReceiverAudioViewHolder(view);
                break;
            case 5:
                view = inflater.inflate(R.layout.item_group_received_image, parent, false);
                holder = new ReceiverImageViewHolder(view);
                break;
            case 7:
                view = inflater.inflate(R.layout.item_group_received_video, parent, false);
                holder = new ReceiverVideoViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = data.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                SenderTextViewHolder senderTextViewHolder = (SenderTextViewHolder) holder;
                senderTextViewHolder.tv_message.setText(message.getMessage());
                senderTextViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));
                //senderViewHolder.tv_name.setText(chat.getSenderId().getName());
                /*Glide
                        .with(context)
                        .load(chat.getSenderId().getProfilePic())
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_dummy)
                        .into(senderViewHolder.img_user);*/
                break;
            case 2:
                SenderAudioViewHolder senderAudioViewHolder = (SenderAudioViewHolder) holder;
                senderAudioViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));
                //senderAudioViewHolder.tv_message.setText(message.getMessage());
                //senderViewHolder.tv_name.setText(chat.getSenderId().getName());
                Glide
                        .with(context)
                        .load(FunctionsKt.getImage(context))
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_dummy_user)
                        .into(senderAudioViewHolder.img_user);
                senderAudioViewHolder.tv_duration.setText(audioPlayer.getAudioDuration(message.getRecordingTime()));
                senderAudioViewHolder.img_play_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        senderAudioViewHolder.img_pause_audio.setVisibility(View.VISIBLE);
                        senderAudioViewHolder.img_play_audio.setVisibility(View.GONE);
                        audioPlayer.startPlayer(activity, message.getMessage(), senderAudioViewHolder.seek_audio, senderAudioViewHolder.img_play_audio, senderAudioViewHolder.img_pause_audio, message.getRecordingTime());
                    }
                });
                senderAudioViewHolder.img_pause_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        senderAudioViewHolder.img_pause_audio.setVisibility(View.GONE);
                        senderAudioViewHolder.img_play_audio.setVisibility(View.VISIBLE);
                        audioPlayer.stopPlayer(senderAudioViewHolder.seek_audio, senderAudioViewHolder.img_play_audio, senderAudioViewHolder.img_pause_audio);
                    }
                });
                break;
            case 4:
                SenderImageViewHolder senderImageViewHolder = (SenderImageViewHolder) holder;
                senderImageViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));
                Glide
                        .with(context)
                        .load(message.getMessage())
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_place_holder_image)
                        .into(senderImageViewHolder.img_media_image);
                senderImageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> array = new ArrayList<>();
                        array.add(message.getMessage());
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "0");
                        activity.startActivity(intent);
                    }
                });
                break;
            case 6:
                SenderVideoViewHolder senderVideoViewHolder = (SenderVideoViewHolder) holder;
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.isMemoryCacheable();
                String videopath = message.getMessage();
                Glide.with(activity).setDefaultRequestOptions(requestOptions).load(videopath).into(senderVideoViewHolder.first_image_view);
                senderVideoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("videoLink", "onClick: " + message.getMessage());
                        Intent intent = new Intent(activity, VideoViewActivity.class);
                        intent.putExtra("videoLink", message.getMessage());
                        intent.putExtra("from", "firebase");
                        activity.startActivity(intent);
                    }
                });
                senderVideoViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));
                break;
            case 1:
                ReceiverTextViewHolder receiverTextViewHolder = (ReceiverTextViewHolder) holder;
                receiverTextViewHolder.tv_message.setText(message.getMessage());
                receiverTextViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));
                userService = new UserService();
                userService.setOnChangedListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(EventType type, int index, int oldIndex) {

                    }

                    @Override
                    public void onDataChanged() {
                        User user = userService.getUserById(message.getMessageBy());
                        if (user != null)
                            receiverTextViewHolder.tv_name.setText(user.getUserName());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
                break;
            case 3:
                ReceiverAudioViewHolder receiverAudioViewHolder = (ReceiverAudioViewHolder) holder;
                receiverAudioViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));
                //receiverAudioViewHolder.tv_message.setText(message.getMessage());
                //senderViewHolder.tv_name.setText(chat.getSenderId().getName());

                userService = new UserService();
                userService.setOnChangedListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(EventType type, int index, int oldIndex) {

                    }

                    @Override
                    public void onDataChanged() {
                        User user = userService.getUserById(message.getMessageBy());
                        if (user != null) {
                            Glide
                                    .with(context)
                                    .load(user.getImage())
                                    .centerCrop()
                                    .dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_dummy_user)
                                    .into(receiverAudioViewHolder.img_user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

                receiverAudioViewHolder.tv_duration.setText(audioPlayer.getAudioDuration(message.getRecordingTime()));
                receiverAudioViewHolder.img_play_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        receiverAudioViewHolder.img_play_audio.setVisibility(View.GONE);
                        receiverAudioViewHolder.img_pause_audio.setVisibility(View.VISIBLE);
                        audioPlayer.startPlayer(activity, message.getMessage(), receiverAudioViewHolder.seek_audio, receiverAudioViewHolder.img_play_audio, receiverAudioViewHolder.img_pause_audio, message.getRecordingTime());
                    }
                });
                receiverAudioViewHolder.img_pause_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        receiverAudioViewHolder.img_play_audio.setVisibility(View.VISIBLE);
                        receiverAudioViewHolder.img_play_audio.setVisibility(View.GONE);
                        audioPlayer.stopPlayer(receiverAudioViewHolder.seek_audio, receiverAudioViewHolder.img_play_audio, receiverAudioViewHolder.img_pause_audio);
                    }
                });
                break;
            case 5:
                ReceiverImageViewHolder receiverImageViewHolder = (ReceiverImageViewHolder) holder;
                receiverImageViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));

                userService = new UserService();
                userService.setOnChangedListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(EventType type, int index, int oldIndex) {

                    }

                    @Override
                    public void onDataChanged() {
                        User user = userService.getUserById(message.getMessageBy());
                        if (user != null)
                            receiverImageViewHolder.tv_name.setText(user.getUserName());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
                Glide
                        .with(context)
                        .load(message.getMessage())
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_place_holder_image)
                        .into(receiverImageViewHolder.img_media_image);
                receiverImageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> array = new ArrayList<>();
                        array.add(message.getMessage());
                        Intent intent = new Intent(activity, ImageViewActivity.class);
                        intent.putStringArrayListExtra("listOfImages", array);
                        intent.putExtra("position", "0");
                        activity.startActivity(intent);
                    }
                });
                break;
            case 7:
                ReceiverVideoViewHolder receiverVideoViewHolder = (ReceiverVideoViewHolder) holder;
                RequestOptions requestOptions1 = new RequestOptions();
                requestOptions1.isMemoryCacheable();
                String videopath1 = message.getMessage();
                Glide.with(activity).setDefaultRequestOptions(requestOptions1).load(videopath1).into(receiverVideoViewHolder.first_image_view);
                receiverVideoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("videoLink", "onClick: " + message.getMessage());
                        Intent intent = new Intent(activity, VideoViewActivity.class);
                        intent.putExtra("videoLink", message.getMessage());
                        activity.startActivity(intent);
                    }
                });

                userService = new UserService();
                userService.setOnChangedListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(EventType type, int index, int oldIndex) {

                    }

                    @Override
                    public void onDataChanged() {
                        User user = userService.getUserById(message.getMessageBy());
                        if (user != null)
                            receiverVideoViewHolder.tv_name.setText(user.getUserName());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
                receiverVideoViewHolder.tv_time_ago.setText(TimeUtils.getTime(message.getTime()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SenderTextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        CircleImageView img_user;
        LinearLayout ll_detail;

        public SenderTextViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            /*
            tv_name = itemView.findViewById(R.id.tv_name);
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class SenderAudioViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        CircleImageView img_user;
        ImageView img_play_audio, img_pause_audio;
        SeekBar seek_audio;
        TextView tv_duration;

        public SenderAudioViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);

            img_user = itemView.findViewById(R.id.img_user);
            img_play_audio = itemView.findViewById(R.id.img_play_audio);
            img_pause_audio = itemView.findViewById(R.id.img_pause_audio);

            seek_audio = itemView.findViewById(R.id.seek_audio);

            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            /*
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class SenderImageViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        SelectableRoundedImageView img_media_image;

        public SenderImageViewHolder(View itemView) {
            super(itemView);
            img_media_image = itemView.findViewById(R.id.img_media_image);
            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            /*
            tv_name = itemView.findViewById(R.id.tv_name);
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class SenderVideoViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        CircleImageView img_user;
        LinearLayout ll_detail;
        ImageView first_image_view;
        RelativeLayout rl_video;

        public SenderVideoViewHolder(View itemView) {
            super(itemView);
            rl_video = itemView.findViewById(R.id.rl_video);
            first_image_view = itemView.findViewById(R.id.first_image_view);
            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            /*
            tv_name = itemView.findViewById(R.id.tv_name);
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class ReceiverTextViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        CircleImageView img_user;
        LinearLayout ll_detail;

        public ReceiverTextViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            tv_name = itemView.findViewById(R.id.tv_name);
            /*
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class ReceiverAudioViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        CircleImageView img_user;
        ImageView img_play_audio, img_pause_audio;
        SeekBar seek_audio;
        TextView tv_duration;

        public ReceiverAudioViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            img_user = itemView.findViewById(R.id.img_user);

            img_play_audio = itemView.findViewById(R.id.img_play_audio);
            img_pause_audio = itemView.findViewById(R.id.img_pause_audio);

            seek_audio = itemView.findViewById(R.id.seek_audio);

            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);

            /*
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class ReceiverImageViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        SelectableRoundedImageView img_media_image;

        public ReceiverImageViewHolder(View itemView) {
            super(itemView);
            img_media_image = itemView.findViewById(R.id.img_media_image);

            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            tv_name = itemView.findViewById(R.id.tv_name);

            /*
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class ReceiverVideoViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        ImageView first_image_view;
        RelativeLayout rl_video;

        public ReceiverVideoViewHolder(View itemView) {
            super(itemView);
            rl_video = itemView.findViewById(R.id.rl_video);
            first_image_view = itemView.findViewById(R.id.first_image_view);
            tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            tv_name = itemView.findViewById(R.id.tv_name);

            /*
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }
}
