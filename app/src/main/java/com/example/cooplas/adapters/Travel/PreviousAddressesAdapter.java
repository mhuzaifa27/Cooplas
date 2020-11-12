package com.example.cooplas.adapters.Travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cooplas.R;
import com.example.cooplas.models.Travel.Customer.RecentLocation;

import java.util.List;

public class PreviousAddressesAdapter extends RecyclerView.Adapter<PreviousAddressesAdapter.TravelBuddyViewHolder> {
    private List<RecentLocation> data;
    private Context context;
    private boolean isLoadingAdded;
    private IClicks iClicks;
    public interface IClicks{
        void onItemClick(View view, RecentLocation name);
    }
    public void setOnIClickListener(IClicks iClicks){
        this.iClicks=iClicks;
    }

    public PreviousAddressesAdapter(List<RecentLocation> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(RecentLocation mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<RecentLocation> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(RecentLocation city) {
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
        add(new RecentLocation());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        RecentLocation item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RecentLocation getItem(int position) {
        return data.get(position);
    }

    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_previous_addresses, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        RecentLocation recentLocation = data.get(position);
        holder.tv_place.setText(recentLocation.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iClicks!=null){
                    iClicks.onItemClick(view,recentLocation);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if (data.size() < 5)
            return data.size();
        else return 5;
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_place;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            tv_place = itemView.findViewById(R.id.tv_place);
        }
    }
}
