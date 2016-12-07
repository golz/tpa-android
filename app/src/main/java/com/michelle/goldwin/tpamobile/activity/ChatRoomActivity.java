package com.michelle.goldwin.tpamobile.activity;

import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.chatinstructor.ChatMessage;
import com.michelle.goldwin.tpamobile.chatinstructor.InstructorListAdapter;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;

    public void displayChat() {

        ListView listMsg = (ListView) findViewById(R.id.msgList);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message, FirebaseDatabase.getInstance().getReference().child("Chat")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView msgTxt = (TextView) v.findViewById(R.id.msgTxt);
                TextView msgUser = (TextView) v.findViewById(R.id.msgUser);
                TextView msgTime = (TextView) v.findViewById(R.id.msgTime);

                msgTxt.setText(model.getMessage());
                msgUser.setText(model.getUser());
                msgTime.setText(android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getTime()));
            }
        };

        listMsg.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        displayChat();

        FloatingActionButton btnSend = (FloatingActionButton) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inp = (EditText) findViewById(R.id.input);

                ChatMessage chat = new ChatMessage(inp.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                FirebaseDatabase.getInstance().getReference().child("Chat").push().setValue(chat);



                inp.setText("");
                displayChat();
            }
        });


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
}
