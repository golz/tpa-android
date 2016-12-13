package com.michelle.goldwin.tpamobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.chatinstructor.ChatFragment;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;
import com.michelle.goldwin.tpamobile.googlemaps.GoogleMapsFragment;
import com.michelle.goldwin.tpamobile.object.Calorie;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.object.History;
import com.michelle.goldwin.tpamobile.object.User;
import com.michelle.goldwin.tpamobile.todolist.ChooseMissionFragment;
import com.michelle.goldwin.tpamobile.todolist.TodoListFragment;
import com.michelle.goldwin.tpamobile.viewpager.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageView imgProfile;
    private TextView lblUserFullname;
    private TextView lblUserEmail;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* BEGIN INTIIALIZE */
        tabLayout       = (TabLayout) findViewById(R.id.tabLayout);
        viewPager       = (ViewPager) findViewById(R.id.viewPager);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        final FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        progressDialog =  new ProgressDialog(this);

        /* BEGIN READ OLD DATA WITH DATABASE REFERENCE */
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+firebaseAuth.getCurrentUser().getUid().toString()+"");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user == null)
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        /*SET TODAY CALORIE*/
        try {
            Date today = new Date();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date todayWithZeroTime = formatter.parse(formatter.format(today));
            databaseReference = FirebaseDatabase.getInstance().getReference("calories/"+firebaseAuth.getCurrentUser().getUid().toString()+"/"+todayWithZeroTime.toString());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Calorie calorie = dataSnapshot.getValue(Calorie.class);
                    if(calorie != null)
                        LoggedUserInformation.getInstance().setCurrentCalorie(calorie.value);
                    else
                        LoggedUserInformation.getInstance().setCurrentCalorie((double)0);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
        /* BEGIN READ OLD DATA WITH DATABASE REFERENCE */
        /* END INITIALIZE */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),EatActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /* BEGIN OF NAVIGATION VIEW */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View viewNavHeaderHome = navigationView.getHeaderView(0);

        imgProfile      = (ImageView) viewNavHeaderHome.findViewById(R.id.imgProfile);
        lblUserFullname = (TextView) viewNavHeaderHome.findViewById(R.id.lblUserFullname);
        lblUserEmail    = (TextView) viewNavHeaderHome.findViewById(R.id.lblUserEmail);

        /**
         * Check database for (New) Profile Image, Fullname and Email Address
         */
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+firebaseAuth.getCurrentUser().getUid().toString()+"");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(null != user)
                {
                    if(user.profileurl != null) Picasso.with(getApplicationContext()).load(user.profileurl).resize(128,128).into(imgProfile);
                    if(user.fullname != null)   lblUserFullname.setText(user.fullname);

                    /*Set Static LoggedUserInformation*/
                    if(user.fullname != null) LoggedUserInformation.getInstance().setFullname(user.fullname);
                    if(user.DOB != null) LoggedUserInformation.getInstance().setDOB(user.DOB);
                    if(user.height != null) LoggedUserInformation.getInstance().setHeight(user.height);
                    if(user.weight != null) LoggedUserInformation.getInstance().setWeight(user.weight);
                    if(user.profileurl != null) LoggedUserInformation.getInstance().setProfileurl(user.profileurl);
                }
                else
                {
                    lblUserFullname.setText(firebaseAuth.getCurrentUser().getEmail().split("@")[0].toString());
                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        lblUserEmail.setText(loggedUser.getEmail());
        /* END OF NAVIGATION VIEW */

        /* CALL `ViewPagerAdapter` */
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TodoListFragment(),"");
        viewPagerAdapter.addFragment(new GoogleMapsFragment(),"");
        viewPagerAdapter.addFragment(new ChatFragment(),"");
        /* END CALL */

        /* COMBINE */
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.getTabAt(0).setIcon(R.drawable.mission);
        tabLayout.getTabAt(1).setIcon(R.drawable.map);
        tabLayout.getTabAt(2).setIcon(R.drawable.chat);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_update_profile){
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        } else if (id == R.id.nav_insert_food) {
            startActivity(new Intent(getApplicationContext(),FoodActivity.class));
        } else if (id == R.id.nav_calorie_history) {
            startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();                 // Firebase Logout
            LoginManager.getInstance().logOut();    // Facebook Logout
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
