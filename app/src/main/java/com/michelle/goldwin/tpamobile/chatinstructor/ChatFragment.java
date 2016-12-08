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

import com.google.firebase.database.FirebaseDatabase;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.activity.ChatRoomActivity;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


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

                if(user.getText().toString().equals("Goldwin Japar")) {
                    startActivity(new Intent(getContext(), ChatRoomActivity.class).putExtra("key","rL9F6eUljrWbTfGZoStgV05n1BH3").putExtra("name",user.getText().toString()));
                }
                else startActivity(new Intent(getContext(), ChatRoomActivity.class).putExtra("key","9DOHdZE6tvelBdyBWtwZuMzKPuo1").putExtra("name",user.getText().toString()));

            }
        });
        return view;
    }
    public InstructorListAdapter getInstructorAdapter()
    {
        InstructorListAdapter instructorListAdapter = new InstructorListAdapter(getContext());

        /*
        if(LoggedUserInformation.getInstance().getKey().equals("9DOHdZE6tvelBdyBWtwZuMzKPuo1") || LoggedUserInformation.getInstance().getKey().equals("rL9F6eUljrWbTfGZoStgV05n1BH3")){
            //All user here except the instructors
        }
        */
            instructorListAdapter.addInstructor("Goldwin Japar");
            instructorListAdapter.addInstructor("Michelle Neysa");

        return instructorListAdapter;
    }
}
