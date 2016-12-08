package com.michelle.goldwin.tpamobile.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;


public class ChatRoomActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;

    private Toolbar toolbar;
    private FloatingActionButton btnSend;
    private EditText inp;

    private DatabaseReference databaseReference;

    private Bundle extra;

    public void displayChat() {

        ListView listMsg = (ListView) findViewById(R.id.msgList);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message, FirebaseDatabase.getInstance().getReference().child("users/"+extra.getString("key")+"/chats")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView msgTxt = (TextView) v.findViewById(R.id.msgTxt);
                TextView msgUser = (TextView) v.findViewById(R.id.msgUser);
                TextView msgTime = (TextView) v.findViewById(R.id.msgTime);

                if (model.getUser().equals(LoggedUserInformation.getInstance().getFullname()) || model.getUser().equals(extra.getString("name"))) {
                    {
                        msgTxt.setText(model.getMessage());
                        msgUser.setText(model.getUser());
                        msgTime.setText(android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
                    }

                };
            }
        };
            listMsg.setAdapter(adapter);
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        /**
         * TODO: Get Extra from intent
         */

        extra = getIntent().getExtras();

        /* BEGIN INTIIALIZE */
        displayChat();
        btnSend = (FloatingActionButton) findViewById(R.id.btnSend);
        inp    =  (EditText) findViewById(R.id.input);
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+extra.getString("key")+"/chats");
        /* END INITIALIZE */



        /**
         * TODO: Change to name for Toolbar's title
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(extra.getString("name"));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(inp.getText())) {

                    ChatMessage chat = new ChatMessage(inp.getText().toString(), LoggedUserInformation.getInstance().getFullname() , extra.getString("key"));
                    FirebaseDatabase.getInstance().getReference().child("users/"+extra.getString("key")+"/chats").push().setValue(chat);
                    inp.setText("");
                    displayChat();
                }
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
}
