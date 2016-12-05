package com.michelle.goldwin.tpamobile.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    /**
     * This Varialbe is used for calendar
     */
    private Calendar calendar;
    private int year_x;
    private int month_x;
    private int day_x;
    static final int CALENDAR_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* BEGIN INITIALIZE */
        imgProfile  = (ImageView) findViewById(R.id.imgProfile);
        txtFullname = (EditText) findViewById(R.id.txtFullname);
        txtDOB      = (EditText) findViewById(R.id.txtDOB);
        txtDOB.setKeyListener(null);
        txtHeight   = (EditText) findViewById(R.id.txtHeight);
        txtWeight   = (EditText) findViewById(R.id.txtWeight);
        btnUpdate   = (Button) findViewById(R.id.btnUpdate);
        btnCancel   = (Button) findViewById(R.id.btnCancel);
        firebaseAuth     = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        calendar    = Calendar.getInstance();
        year_x      = calendar.get(Calendar.YEAR);
        month_x     = calendar.get(Calendar.MONTH);
        day_x       = calendar.get(Calendar.DAY_OF_MONTH);
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
                    if(user.profileurl != null) Picasso.with(getApplicationContext()).load(user.profileurl).into(imgProfile);
                    if(user.fullname != null)   txtFullname.setText(user.fullname.toString());
                    if(user.DOB != null)        txtDOB.setText(user.DOB.toString());
                    if(user.height != null)     txtHeight.setText(user.height.toString());
                    if(user.weight != null)     txtWeight.setText(user.weight.toString());
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
                showDialog(CALENDAR_DIALOG_ID);
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

                user = new User(fullname, dob,height,weight,true,taskSnapshotDownloadUrl);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid().toString()).setValue(user);

                Toast.makeText(ProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                finish();
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

    /**
     * For Show Dialog Date Picker
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == CALENDAR_DIALOG_ID)
        {
            return new DatePickerDialog(this,datePickerListener,year_x,month_x,day_x);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x = i;
            month_x = i1 + 1;
            day_x = i2;
            txtDOB.setText(day_x+"/"+month_x+"/"+year_x);
        }
    };

    /**
     * For Upload Photo
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

            //Get Url
            uploadUri = data.getData();
            //Initialize photo's preview without download from Firebase Storage
            Picasso.with(getApplicationContext()).load(uploadUri).into(imgProfile);

            /* BEGIN OF DATA COMPRESSING WITH BITMAP */
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uploadUri);
            }catch (Exception e)
            {
                Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,30,baos); //Compress to 30% from Original
            byte[] newData = baos.toByteArray();
            /* END OF DATA COMPRESSING WITH BITMAP */

            StorageReference  filePath = storageReference.child("ProfilePictures").child(uploadUri.getLastPathSegment());
           // filePath.putFile(uploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            filePath.putBytes(newData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                    taskSnapshotDownloadUrl = taskSnapshot.getDownloadUrl().toString();
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
