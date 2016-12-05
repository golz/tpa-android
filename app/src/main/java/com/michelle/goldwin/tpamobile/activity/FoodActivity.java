package com.michelle.goldwin.tpamobile.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.insertfood.FoodListAdapter;
import com.michelle.goldwin.tpamobile.object.Food;
import com.michelle.goldwin.tpamobile.object.User;
import com.squareup.picasso.Picasso;

public class FoodActivity extends AppCompatActivity {

    private EditText txtFoodName;
    private EditText txtFoodCalorie;
    private Button   btnAddFood;

    private FoodListAdapter foodListAdapter = new FoodListAdapter(getApplicationContext());

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        ListView foodListView = (ListView) findViewById(R.id.listViewFood);
        foodListView.setAdapter(foodListAdapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        /* BEGIN INITIALIZE */
        txtFoodName     = (EditText) findViewById(R.id.txtFoodName);
        txtFoodCalorie  = (EditText) findViewById(R.id.txtFoodCalorie);
        btnAddFood      = (Button) findViewById(R.id.btnAddFood);
        /* END INTIIALIZE */

        /* BEGIN READ OLD DATA WITH DATABASE REFERENCE */
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /**
                 * TODO: Retrieve all data from firebase (Foods) then validate Adapter will refresh after retrieve
                 */
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* END READ OLD DATA WITH DATABASE REFERENCE */

        /* BEGIN ACTION */
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodName = capitalize(txtFoodName.getText().toString().trim());
                Double foodCalorie = Double.parseDouble(txtFoodCalorie.getText().toString().trim());

                Food food = new Food(foodName,foodCalorie);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("foods").child(foodName).setValue(food);

                Toast.makeText(FoodActivity.this, "Insert success", Toast.LENGTH_SHORT).show();
            }
        });
        /* END ACTION */
    }
    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
