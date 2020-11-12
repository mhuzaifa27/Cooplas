package com.example.cooplas.adapters.Food;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cooplas.R;

import java.util.List;

public class SavedAddressesAdapter extends RecyclerView.Adapter<SavedAddressesAdapter.TravelBuddyViewHolder> {
    private List<String> data;
    private Context context;
    private ICLicks icLicks;
    private boolean isLoadingAdded;

    public interface ICLicks {
        void onFavouriteClick(View view, String String, ImageView img_favourite);
    }

    public void OnClickListener(ICLicks icLicks) {
        this.icLicks = icLicks;
    }

    public SavedAddressesAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void add(String mc) {
        data.add(mc);
        if (data.size() > 0) {
            notifyItemInserted(0);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<String> mcList) {
        data = mcList;
        notifyDataSetChanged();
    }

    public void remove(String city) {
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
        add(new String());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = data.size() - 1;
        String item = getItem(position);
        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public String getItem(int position) {
        return data.get(position);
    }


    @Override
    public TravelBuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_saved_address, parent, false);
        return new TravelBuddyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelBuddyViewHolder holder, int position) {
        String foodString = data.get(position);

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context, R.style.MyDialogTheme)
                        .setIcon(R.drawable.ic_logo)
                        .setTitle(context.getResources().getString(R.string.app_name))
                        .setMessage(R.string.delete_address_warning)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TravelBuddyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_delete;

        public TravelBuddyViewHolder(View itemView) {
            super(itemView);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
