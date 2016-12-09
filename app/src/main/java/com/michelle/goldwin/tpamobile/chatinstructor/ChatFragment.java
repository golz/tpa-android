package com.michelle.goldwin.tpamobile.chatinstructor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.activity.ChatRoomActivity;
import com.michelle.goldwin.tpamobile.activity.HomeActivity;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.object.User;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

  private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
  private DatabaseReference databaseReference;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_instructor, container, false);

        final ListView instructorListView = (ListView) view.findViewById(R.id.listViewChat);
        instructorListView.setAdapter(getInstructorAdapter());

        instructorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView user = (TextView) view.findViewById(R.id.lblInstructorName);

                    startActivity(new Intent(getContext(), ChatRoomActivity.class).putExtra("key",FirebaseAuth.getInstance().getCurrentUser().getUid()).putExtra("name",user.getText().toString()));

            }
        });
        return view;
    }
    public InstructorListAdapter getInstructorAdapter()
    {
        final InstructorListAdapter instructorListAdapter = new InstructorListAdapter(getContext());


        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals("9DOHdZE6tvelBdyBWtwZuMzKPuo1") || FirebaseAuth.getInstance().getCurrentUser().getUid().equals("rL9F6eUljrWbTfGZoStgV05n1BH3")){

            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        for (DataSnapshot childData : dataSnapshot.getChildren()) {

                            String name = childData.getValue(User.class).fullname.toString();
                            if (!name.equals("Goldwin Japar") && !name.equals("Michelle Neysa")) {
                                instructorListAdapter.addInstructor(name);
                                System.out.println(name);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            instructorListAdapter.addInstructor("Goldwin Japar");
            instructorListAdapter.addInstructor("Michelle Neysa");
        }

        return instructorListAdapter;
    }
}
