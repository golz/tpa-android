package com.michelle.goldwin.tpamobile.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.activity.HomeActivity;
import com.michelle.goldwin.tpamobile.object.CurrentTask;
import com.michelle.goldwin.tpamobile.object.History;
import com.michelle.goldwin.tpamobile.object.TodoList;
import com.michelle.goldwin.tpamobile.viewpager.FragmentChangeListener;
import com.michelle.goldwin.tpamobile.viewpager.ViewPagerAdapter;

/**
 * Created by Michelle Neysa on 12/11/2016.
 */


public class ChooseMissionFragment extends Fragment{


    HomeActivity homeActivity;

    public ChooseMissionFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_choose_mission, container, false);


        final Spinner spinner = (Spinner) view.findViewById(R.id.MissionSpinner);
        Button button = (Button) view.findViewById(R.id.MissionButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("todolist/"+spinner.getSelectedItem().toString());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        FirebaseDatabase.getInstance().getReference().child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/currentTask").removeValue();

                        for(DataSnapshot child : dataSnapshot.getChildren()){

                            String name = child.getKey().toString();
                            int cal = Integer.parseInt(child.child("cal").getValue().toString());

                            CurrentTask currentTask = new CurrentTask(name,cal);
                            FirebaseDatabase.getInstance().getReference().child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/currentTask").push().setValue(currentTask);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("option",spinner.getSelectedItem().toString());

                Fragment fragment = new TodoListFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.root_fragment,fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

            }
        });

        return view;
    }


}
