package com.example.cooplas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.utils.ShowDialogues;

import java.util.List;

public class SupportTicketsAdapter extends RecyclerView.Adapter<SupportTicketsAdapter.TravelBuddyViewHolder> {
    private List<String> data;
    Context context;

    public SupportTicketsAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_support_ticket, parent, false);
        return new TravelBuddyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        String s = data.get(position);

        if (s.equalsIgnoreCase("1")){
            holder.tv_status.setBackground(context.getResources().getDrawable(R.drawable.round_yellow));
            holder.tv_status.setText(R.string.reviewing);
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
        }
        else if(s.equalsIgnoreCase("2")){
            holder.tv_status.setBackground(context.getResources().getDrawable(R.drawable.round_green));
            holder.tv_status.setText(R.string.resolved);
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
        }
        else if(s.equalsIgnoreCase("3")){
            holder.tv_status.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            holder.tv_status.setText(R.string.declined);
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
        }
        else{
            holder.tv_status.setBackground(context.getResources().getDrawable(R.drawable.round_light_grey));
            holder.tv_status.setText(R.string.pending);
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogues.SHOW_TICKET_DETAIL_DIALOG(context);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_status;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
