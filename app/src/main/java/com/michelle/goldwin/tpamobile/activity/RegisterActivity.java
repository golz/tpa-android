package com.michelle.goldwin.tpamobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.michelle.goldwin.tpamobile.R;

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private LoginButton btnFacebook;
    private Button btnSignin;
    private Button btnSignup;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         /* BEGIN INITIALIZE */
        txtEmail    = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnSignin   = (Button) findViewById(R.id.btnSignin);
        btnSignup   = (Button) findViewById(R.id.btnSignup);
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null)
        {
            //Redirecting if user has been logged in before
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        callbackManager = CallbackManager.Factory.create();
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

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegisterActivity.this, "Username and Password must be filled.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();
                    /* START TO REGISTER TO FIREBASE */
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    /* END TO REGISTER TO FIREBASE*/
                }
            }
        });
        btnFacebook.setReadPermissions(Arrays.asList("email","public_profile"));
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                });
                finish();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException e) {

            }
        });
        /* END ACTION */
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        Log.e("data",data.toString());
    }
}
