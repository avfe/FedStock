package tech.fedorov.fedstock.fedchatclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tech.fedorov.fedstock.R;
import tech.fedorov.fedstock.fedchatclient.Adapters.ServerListAdapter;
import tech.fedorov.fedstock.fedchatclient.Memory.FileHandler;
import tech.fedorov.fedstock.fedchatclient.Servers.Server;

public class ServerListActivity extends AppCompatActivity {
    private ServerListAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Server> servers;
    private TextView newChatButton;
    private TextView emptyListAlert;
    private FileHandler fileHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serverslist);
        servers = new ArrayList<>();
        // Find list of servers in private files
        fileHandler = new FileHandler(this);
        String[] files = getApplicationContext().fileList();
        if (FileHandler.findInArray("servers", files)) {
            servers = (ArrayList<Server>) fileHandler.readObjectFromPrivateFile("servers");
        }

        emptyListAlert = (TextView) findViewById(R.id.emptyListAlert);
        if (servers.size() == 0) {
            emptyListAlert.setVisibility(View.VISIBLE);
        } else {
            emptyListAlert.setVisibility(View.INVISIBLE);
        }

        // set up the RecyclerView
        recyclerView = findViewById(R.id.ServerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServerListAdapter(this, servers, emptyListAlert);
        recyclerView.setAdapter(adapter);

        Intent startIntent = new Intent(this, StartActivity.class);
        newChatButton = (TextView) findViewById(R.id.newChatButton);
        newChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent.putExtra("servers", servers);
                startActivity(startIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fileHandler = new FileHandler(this);
        String[] files = getApplicationContext().fileList();
        if (FileHandler.findInArray("servers", files)) {
            servers = (ArrayList<Server>) fileHandler.readObjectFromPrivateFile("servers");
            adapter = new ServerListAdapter(this, servers, emptyListAlert);
            recyclerView.setAdapter(adapter);
        }
        if (servers.size() == 0) {
            emptyListAlert.setVisibility(View.VISIBLE);
        } else {
            emptyListAlert.setVisibility(View.INVISIBLE);
        }
    }
}
