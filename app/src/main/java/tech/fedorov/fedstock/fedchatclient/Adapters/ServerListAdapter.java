package tech.fedorov.fedstock.fedchatclient.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tech.fedorov.fedstock.fedchatclient.MainActivity;
import tech.fedorov.fedstock.fedchatclient.Memory.FileHandler;
import tech.fedorov.fedstock.R;
import tech.fedorov.fedstock.fedchatclient.Servers.Server;

public class ServerListAdapter extends RecyclerView.Adapter<ServerListAdapter.ViewHolder>{
    private ArrayList<Server> mData;
    private ServerListAdapter adapterContext = ServerListAdapter.this;
    private LayoutInflater mInflater;
    private Context activityContext;
    private FileHandler fileHandler;
    private TextView emptyListAlert;

    // data is passed into the constructor
    public ServerListAdapter(Context context, ArrayList<Server> data, TextView emptyListAlert) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.activityContext = context;
        fileHandler = new FileHandler(context);
        this.emptyListAlert = emptyListAlert;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.server_item_cardview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Server serv = mData.get(position);
        String adress = serv.getAddress();
        String name = serv.getName();
        holder.serverAddress.setText(adress);
        holder.serverName.setText(name);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                fileHandler.writeObjectToPrivateFile("servers", mData);
                adapterContext.notifyDataSetChanged();
                if (mData.size() < 1) {
                    emptyListAlert.setVisibility(View.VISIBLE);
                } else {
                    emptyListAlert.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityContext, tech.fedorov.fedstock.fedchatclient.MainActivity.class);
                intent.putExtra("name", serv.getName());
                intent.putExtra("ip", serv.getIP());
                intent.putExtra("port", serv.getPORT());
                activityContext.startActivity(intent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serverAddress;
        TextView serverName;
        ImageButton removeButton;
        ConstraintLayout constraintLayout;
        ViewHolder(View itemView) {
            super(itemView);
            serverAddress = itemView.findViewById(R.id.ServerAdress);
            serverName = itemView.findViewById(R.id.ServerName);
            removeButton = itemView.findViewById(R.id.remove_button);
            constraintLayout = itemView.findViewById(R.id.server_item_field);
        }
    }
}
