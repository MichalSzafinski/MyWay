package com.example.szafa.myway;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Client> data;

    public ArrayList<Client> getData() {
        return data;
    }

    public void setData(ArrayList<Client> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLocation;
        private Button btnDelete;

        public TextView getTxtLocation() {
            return txtLocation;
        }

        public Button getBtnDelete() {
            return btnDelete;
        }

        public ViewHolder(View view){
            super(view);
            txtLocation = view.findViewById(R.id.txtLocation);
            btnDelete = view.findViewById(R.id.btnDelete);
        }

    }

    public LocationAdapter(ArrayList<Client> data){
        this.data = data;
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_location, parent, false);
        return new LocationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, final int position) {
        holder.getTxtLocation().setText(data.get(position).getName());
        holder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
