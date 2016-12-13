package com.michelle.goldwin.tpamobile.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;
import com.michelle.goldwin.tpamobile.object.Calorie;
import com.michelle.goldwin.tpamobile.object.Food;
import com.michelle.goldwin.tpamobile.object.History;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EatActivity extends AppCompatActivity {

    private FirebaseListAdapter<Food> adapter;

    private EditText txtFoodName;
    private EditText txtFoodCalorie;
    private Button btnAddFood;

    private DatabaseReference databaseReference;

    private void displayFood()
    {

        ListView foodListView = (ListView) findViewById(R.id.listViewFood);
        adapter = new FirebaseListAdapter<Food>(this, Food.class, R.layout.single_food, FirebaseDatabase.getInstance().getReference().child("foods")) {
            @Override
            protected void populateView(View v, Food model, int position) {
                TextView foodName       = (TextView) v.findViewById(R.id.lblFoodName);
                TextView foodCalorie    = (TextView) v.findViewById(R.id.lblFoodCalorie);
                foodName.setText(model.foodname);
                foodCalorie.setText(model.calorie.toString());
            }
        };
        foodListView.setAdapter(adapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView foodName       = (TextView) view.findViewById(R.id.lblFoodName);
                final TextView foodCalorie    = (TextView) view.findViewById(R.id.lblFoodCalorie);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                alertBuilder.setTitle("Did you eat this?");
                alertBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        History history = new History(foodName.getText().toString(),Double.parseDouble(foodCalorie.getText().toString()),currentDateTimeString);
                        FirebaseDatabase.getInstance().getReference().child("histories").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
//                                Snackbar.make(view, "Added to history success", Snackbar.LENGTH_SHORT)
//                                        .setAction("Action", null).show();

                        /* BEGIN BATAS GD*/
                        Date today = new Date();
                        try {
                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date todayWithZeroTime = formatter.parse(formatter.format(today));

                            Calorie calorie = new Calorie(LoggedUserInformation.getInstance().getCurrentCalorie() + Double.parseDouble(foodCalorie.getText().toString()));
                            LoggedUserInformation.getInstance().setCurrentCalorie(LoggedUserInformation.getInstance().getCurrentCalorie() + calorie.value);
                            FirebaseDatabase.getInstance().getReference().child("calories").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(todayWithZeroTime.toString()).setValue(calorie);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        /* END BATAS GD*/
                        EatActivity.this.finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                //Create Dialog
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width   = displayMetrics.widthPixels;
        int height  = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.75));

        displayFood();
    }
}
