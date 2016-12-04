package com.michelle.goldwin.tpamobile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michelle.goldwin.tpamobile.R;

public class ProfileActivity extends AppCompatActivity {


    private EditText txtFullname;
    private EditText txtDOB;
    private EditText txtHeight;
    private EditText txtWeight;
    private Button btnUpdate;
    private Button btnCancel;

    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* BEGIN INITIALIZE */
        txtFullname = (EditText) findViewById(R.id.txtFullname);
        txtDOB      = (EditText) findViewById(R.id.txtDOB);
        txtHeight   = (EditText) findViewById(R.id.txtHeight);
        txtWeight   = (EditText) findViewById(R.id.txtWeight);
        btnUpdate   = (Button) findViewById(R.id.btnUpdate);
        btnCancel   = (Button) findViewById(R.id.btnCancel);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, firebaseUser.getUid().toString(), Toast.LENGTH_SHORT).show();
        /* END INITIALIZE */

        /* BEGIN ACTION */
        
        /* END ACTIOn */
    }
}
