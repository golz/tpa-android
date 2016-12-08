package com.michelle.goldwin.tpamobile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.insertfood.FoodListAdapter;
import com.michelle.goldwin.tpamobile.object.Food;

public class FoodActivity extends AppCompatActivity {

    private FirebaseListAdapter<Food> adapter;

    private EditText txtFoodName;
    private EditText txtFoodCalorie;
    private Button   btnAddFood;

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
                Toast.makeText(getApplicationContext(), view.findViewById(R.id.lblFoodName).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        /* BEGIN INITIALIZE */
        displayFood();
        txtFoodName     = (EditText) findViewById(R.id.txtFoodName);
        txtFoodCalorie  = (EditText) findViewById(R.id.txtFoodCalorie);
        btnAddFood      = (Button) findViewById(R.id.btnAddFood);
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");
        /* END INTIIALIZE */

        /* BEGIN ACTION */
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(txtFoodName.getText()) && !TextUtils.isEmpty(txtFoodCalorie.getText()))
                {
                    String foodName = capitalize(txtFoodName.getText().toString().trim());
                    Double foodCalorie = Double.parseDouble(txtFoodCalorie.getText().toString().trim());

                    Food food = new Food(foodName,foodCalorie);
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("foods").child(foodName).setValue(food);

                    displayFood();
                    Toast.makeText(FoodActivity.this, "Insert success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /* END ACTION */
    }
    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
