package com.michelle.goldwin.tpamobile.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.object.History;

import org.w3c.dom.Text;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseListAdapter<History> adapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private void displayHistory() {
        ListView listHistory = (ListView) findViewById(R.id.historyList);

        adapter = new FirebaseListAdapter<History>(this, History.class, R.layout.single_history, FirebaseDatabase.getInstance().getReference().child("histories/"+firebaseAuth.getCurrentUser().getUid())) {
            @Override
            protected void populateView(View v, History model, int position) {
                TextView historyName    = (TextView) v.findViewById(R.id.historyName);
                TextView historyValue   = (TextView) v.findViewById(R.id.historyValue);
                TextView historyTime    = (TextView) v.findViewById(R.id.historyTime);

                if(model!=null)
                {
                    historyName.setText(model.name);
                    historyValue.setText(model.value.toString());
                    historyTime.setText(model.time);

                    if((double)model.value < 0)
                        historyValue.setTextColor(getResources().getColor(R.color.colorGreen));
                    else
                        historyValue.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        };

        listHistory.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        /* BEGIN INITIALIZATION */
        firebaseAuth = FirebaseAuth.getInstance();

        displayHistory();
        /* END INITIALIZATION */
    }
}
