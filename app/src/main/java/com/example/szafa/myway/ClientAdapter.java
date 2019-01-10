package com.example.szafa.myway;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    private ArrayList<Client> data;
    private ArrayList<Client> clientsToVisit;
    private ClientListActivity clientListActivity;

    public ArrayList<Client> getData() {
        return data;
    }

    public void setData(ArrayList<Client> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button buttonClient;

        public Button getButtonClient() {
            return buttonClient;
        }

        public ViewHolder(View view){
            super(view);
            buttonClient = view.findViewById(R.id.buttonClient);
        }

    }

    public ClientAdapter(ArrayList<Client> data, ArrayList<Client> clientsToVisit, ClientListActivity clientListActivity){
        this.data = data;
        this.clientsToVisit = clientsToVisit;
        this.clientListActivity = clientListActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_client, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Button clientButton = holder.getButtonClient();
        clientButton.setText(data.get(position).getName());
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client clientAtPosition = data.get(position);
                for(Client c : clientsToVisit){
                    if(c.getId() == clientAtPosition.getId()){
                        return;
                    }
                }
                clientListActivity.returnNewClient(clientAtPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
