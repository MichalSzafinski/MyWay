package com.example.szafa.myway;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    private String[] dataTable;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button buttonClient;

        public TextView getButtonClient() {
            return buttonClient;
        }

        public ViewHolder(View view){
            super(view);
            buttonClient = view.findViewById(R.id.buttonClient);
        }

    }

    public ClientAdapter(String[] data){
        dataTable = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_client, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getButtonClient().setText(dataTable[position]);
    }

    @Override
    public int getItemCount() {
        return dataTable.length;
    }
}
