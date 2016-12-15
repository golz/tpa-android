package com.michelle.goldwin.tpamobile.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;
import com.michelle.goldwin.tpamobile.object.Calorie;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.object.History;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                    /* BEGIN CHANGE SIGN FOR POSITIVE OR NEGATIVE */
                    if(model.value > 0)
                        historyValue.setText("+"+model.value.toString());
                    else
                        historyValue.setText(model.value.toString());
                    /* BEGIN CHANGE SIGN FOR POSITIVE OR NEGATIVE */
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
    private void displayCurrentCalories(){
        TextView currentCalories = (TextView) findViewById(R.id.totalCalories);
        Double userCalories = LoggedUserInformation.getInstance().getCurrentCalorie();
        currentCalories.setText("Today Calories : "+ userCalories+" Calories");
        if(userCalories >= 1500 && userCalories <= 2500)
            currentCalories.setTextColor(getResources().getColor(R.color.colorGreen));
        else if((userCalories >= 1300 && userCalories <= 1499) || (userCalories >= 2501 && userCalories <= 2700))
            currentCalories.setTextColor(getResources().getColor(R.color.colorPrimary));
        else
            currentCalories.setTextColor(getResources().getColor(R.color.colorRed));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        /* BEGIN INITIALIZATION */
        firebaseAuth = FirebaseAuth.getInstance();
        displayHistory();
        displayCurrentCalories();
        /* END INITIALIZATION */
    }
}
