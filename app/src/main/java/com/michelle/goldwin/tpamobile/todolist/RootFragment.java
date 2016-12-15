package com.michelle.goldwin.tpamobile.todolist;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;

/**
 * Created by Michelle Neysa on 12/14/2016.
 */

public class RootFragment extends Fragment {

    boolean flag;

    public RootFragment(){


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_root,container,false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("currentTask")) {
                    flag = true;
                    System.out.println("ada Anak");
                }

                else {
                    flag = false;
                    System.out.println("Gak ada");
                }


        }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    if(!flag)
    {
        transaction.replace(R.id.root_fragment, new ChooseMissionFragment());
    }
    else transaction.replace(R.id.root_fragment, new TodoListFragment());

    transaction.commit();


        return view;
    }
}
