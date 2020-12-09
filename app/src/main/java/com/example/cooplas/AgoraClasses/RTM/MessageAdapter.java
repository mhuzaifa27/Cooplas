package com.example.cooplas.AgoraClasses.RTM;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.adapters.Chat.MessagingAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessageBean> messageBeanList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    private Context context;

    public MessageAdapter(Context context, List<MessageBean> messageBeanList, @NonNull OnItemClickListener listener) {
        this.inflater = ((Activity) context).getLayoutInflater();
        this.messageBeanList = messageBeanList;
        this.listener = listener;
        this.context=context;

    }

    @Override
    public int getItemViewType(int position) {
        if (messageBeanList.get(position).isBeSelf())
            return 0;
        else return 1;
    }

   /* @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.msg_item_layout, parent, false);
        return new MyViewHolder(view);
    }*/
   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       View view;
       RecyclerView.ViewHolder holder = null;
       switch (viewType) {
           case 0:
               view = inflater.inflate(R.layout.item_sent_message, parent, false);
               holder = new SenderViewHolder(view);
               break;
           case 1:
               view = inflater.inflate(R.layout.item_received_message, parent, false);
               holder = new ReceiverViewHolder(view);
               break;
       }
       return holder;
   }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setupView(holder, position);
    }

    @Override
    public int getItemCount() {
        return messageBeanList.size();
    }

    private void setupView(RecyclerView.ViewHolder holder, int position) {
        MessageBean bean = messageBeanList.get(position);
        RtmMessage rtmMessage = bean.getMessage();
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(bean);
        });
        switch (holder.getItemViewType()) {
            case 0:
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                senderViewHolder.tv_message.setText(rtmMessage.getText());
                //senderViewHolder.tv_name.setText(message.getSenderId().getName());
               /* Glide
                        .with(context)
                        .load(message.getSenderId().getProfilePic())
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_dummy)
                        .into(senderViewHolder.img_user);*/
                break;
            case 1:
                ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
                receiverViewHolder.tv_message.setText(rtmMessage.getText());
                //receiverViewHolder.tv_name.setText(message.getReceiverId().getName());
              /*  Glide
                        .with(context)
                        .load(message.getReceiverId().getProfilePic())
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_dummy)
                        .into(receiverViewHolder.img_user);*/
                break;
        }
        /*
        if (bean.isBeSelf()) {
            holder.textViewSelfName.setText(bean.getAccount());
        } else {
            holder.textViewOtherName.setText(bean.getAccount());
            if (bean.getBackground() != 0) {
                holder.textViewOtherName.setBackgroundResource(bean.getBackground());
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(bean);
        });

        RtmMessage rtmMessage = bean.getMessage();
        switch (rtmMessage.getMessageType()) {
            case RtmMessageType.TEXT:
                if (bean.isBeSelf()) {
                    holder.textViewSelfMsg.setVisibility(View.VISIBLE);
                    holder.textViewSelfMsg.setText(rtmMessage.getText());
                } else {
                    holder.textViewOtherMsg.setVisibility(View.VISIBLE);
                    holder.textViewOtherMsg.setText(rtmMessage.getText());
                }

                holder.imageViewSelfImg.setVisibility(View.GONE);
                holder.imageViewOtherImg.setVisibility(View.GONE);
                break;
            case RtmMessageType.IMAGE:
                RtmImageMessage rtmImageMessage = (RtmImageMessage) rtmMessage;
                RequestBuilder<Drawable> builder = Glide.with(holder.itemView)
                        .load(rtmImageMessage.getThumbnail())
                        .override(rtmImageMessage.getThumbnailWidth(), rtmImageMessage.getThumbnailHeight());
                if (bean.isBeSelf()) {
                    holder.imageViewSelfImg.setVisibility(View.VISIBLE);
                    builder.into(holder.imageViewSelfImg);
                } else {
                    holder.imageViewOtherImg.setVisibility(View.VISIBLE);
                    builder.into(holder.imageViewOtherImg);
                }

                holder.textViewSelfMsg.setVisibility(View.GONE);
                holder.textViewOtherMsg.setVisibility(View.GONE);
                break;
        }

        holder.layoutRight.setVisibility(bean.isBeSelf() ? View.VISIBLE : View.GONE);
        holder.layoutLeft.setVisibility(bean.isBeSelf() ? View.GONE : View.VISIBLE);*/
    }

    public interface OnItemClickListener {
        void onItemClick(MessageBean message);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOtherName;
        private TextView textViewOtherMsg;
        private ImageView imageViewOtherImg;
        private TextView textViewSelfName;
        private TextView textViewSelfMsg;
        private ImageView imageViewSelfImg;
        private RelativeLayout layoutLeft;
        private RelativeLayout layoutRight;

        MyViewHolder(View itemView) {
            super(itemView);

            textViewOtherName = itemView.findViewById(R.id.item_name_l);
            textViewOtherMsg = itemView.findViewById(R.id.item_msg_l);
            imageViewOtherImg = itemView.findViewById(R.id.item_img_l);
            textViewSelfName = itemView.findViewById(R.id.item_name_r);
            textViewSelfMsg = itemView.findViewById(R.id.item_msg_r);
            imageViewSelfImg = itemView.findViewById(R.id.item_img_r);
            layoutLeft = itemView.findViewById(R.id.item_layout_l);
            layoutRight = itemView.findViewById(R.id.item_layout_r);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        CircleImageView img_user;
        LinearLayout ll_detail;

        public SenderViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            /*tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message, tv_time_ago, tv_name;
        CircleImageView img_user;
        LinearLayout ll_detail;

        public ReceiverViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            /*tv_time_ago = itemView.findViewById(R.id.tv_time_ago);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_user = itemView.findViewById(R.id.img_user);
            ll_detail = itemView.findViewById(R.id.ll_detail);*/
        }
    }
}