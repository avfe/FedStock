package tech.fedorov.fedstock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import tech.fedorov.fedstock.fedquotes.MainActivity;

public class ReviewActivity extends AppCompatActivity {
    private TextView fedStock;
    private TextView fedChat;
    private TextView fedNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        fedStock = findViewById(R.id.fedquotes);
        fedChat = findViewById(R.id.fedchat);
        fedNotes = findViewById(R.id.fednotes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fedStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), tech.fedorov.fedstock.fedquotes.MainActivity.class);
                startActivity(intent);
            }
        });

        fedChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), tech.fedorov.fedstock.fedchatclient.ServerListActivity.class);
                startActivity(intent);
            }
        });

        fedNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), tech.fedorov.fedstock.fednotes.activities.MainActivity.class);
                startActivity(intent);
            }
        });
    }
}