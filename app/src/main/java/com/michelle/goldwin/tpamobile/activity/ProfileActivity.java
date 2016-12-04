package com.michelle.goldwin.tpamobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.object.User;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private EditText txtFullname;
    private EditText txtDOB;
    private EditText txtHeight;
    private EditText txtWeight;
    private Button btnUpdate;
    private Button btnCancel;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private static final int GALLERY_INTENT = 2;
    /**
     * This Uri is used for profile image
     */
    private Uri uploadUri;
    private String taskSnapshotDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* BEGIN INITIALIZE */
        imgProfile  = (ImageView) findViewById(R.id.imgProfile);
        txtFullname = (EditText) findViewById(R.id.txtFullname);
        txtDOB      = (EditText) findViewById(R.id.txtDOB);
        txtHeight   = (EditText) findViewById(R.id.txtHeight);
        txtWeight   = (EditText) findViewById(R.id.txtWeight);
        btnUpdate   = (Button) findViewById(R.id.btnUpdate);
        btnCancel   = (Button) findViewById(R.id.btnCancel);
        firebaseAuth     = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        /* END INITIALIZE */

        /* BEGIN READ OLD DATA WITH DATABASE REFERENCE */
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+firebaseAuth.getCurrentUser().getUid().toString()+"");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading your profile");
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(null != user)
                {
                    /**
                     * TODO: Validate null value for attributes below and Validate URL
                     */
                    if(user.profileurl != null) Picasso.with(getApplicationContext()).load(user.profileurl).into(imgProfile);
                    txtFullname.setText(user.fullname.toString());
                    txtDOB.setText(user.DOB.getDate()+"/"+user.DOB.getMonth()+"/"+user.DOB.getYear());
                    txtHeight.setText(user.height.toString());
                    txtWeight.setText(user.weight.toString());
                }
                else
                {
                    txtFullname.setText(firebaseAuth.getCurrentUser().getEmail().split("@")[0].toString());
                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /* END READ OLD DATA WITH DATABASE REFERENCE */

        /* BEGIN ACTION */
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = txtFullname.getText().toString().trim();
                String dob      = txtDOB.getText().toString().trim();
                Double height   = Double.parseDouble(txtHeight.getText().toString().trim());
                Double weight   = Double.parseDouble(txtWeight.getText().toString().trim());

                User user = null;
                try {
                    user = new User(fullname, (Date)new SimpleDateFormat("d/M/y").parse(dob),height,weight,true,taskSnapshotDownloadUrl);
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid().toString()).setValue(user);

                    Toast.makeText(ProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (ParseException e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /* END ACTION */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * For upload an image from our gallery, with android permission to READ_EXTERNAL_STORAGE
         * to our Firebase Storage
         */
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            uploadUri = data.getData();

            StorageReference  filePath = storageReference.child("ProfilePictures").child(uploadUri.getLastPathSegment());
            filePath.putFile(uploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                    taskSnapshotDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                    Picasso.with(getApplicationContext()).load(taskSnapshotDownloadUrl).into(imgProfile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double uploadPercentage = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading... " + (int)uploadPercentage + "%");
                }
            });
        }
    }
}
