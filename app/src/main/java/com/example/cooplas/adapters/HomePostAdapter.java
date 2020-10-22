package com.example.cooplas.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.ShowMenu;

import java.util.List;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.TravelBuddyViewHolder> {
    private List<String> data;
    private Context context;
    private Activity activity;
    private IClicks iClicks;

    public interface IClicks{
        void OnMenuClick(View view);
    }
    public void setOnMenuClick(IClicks iClicks){
        this.iClicks=iClicks;
    }

    public HomePostAdapter(List<String> data, Context context, Activity activity) {
        this.data = data;
        this.context = context;
        this.activity=activity;
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.postitem, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        String s = data.get(position);

        holder.iv_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iClicks!=null){
                    iClicks.OnMenuClick(view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_subject, tv_body, tv_date;
        ImageView iv_option;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            iv_option=itemView.findViewById(R.id.iv_option);
        }
    }
}
