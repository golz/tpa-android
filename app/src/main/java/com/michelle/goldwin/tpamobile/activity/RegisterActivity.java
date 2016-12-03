package com.michelle.goldwin.tpamobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.michelle.goldwin.tpamobile.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnFacebook;
    private Button btnSignin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         /* BEGIN INITIALIZE */
        txtEmail    = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnSignin   = (Button) findViewById(R.id.btnSignin);
        btnSignup   = (Button) findViewById(R.id.btnSignup);
        /* END INITIALIZE */

        /* BEGIN ACTION */
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email    = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if("".equals(email) || "".equals(password))
                {
                    Toast.makeText(RegisterActivity.this, "Username and Password must be filled.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Register to Firebase
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }

            }
        });
        /* END ACTION */
    }
}
