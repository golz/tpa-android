package com.michelle.goldwin.tpamobile.chatinstructor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.michelle.goldwin.tpamobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatInstructorFragment extends Fragment {


    public ChatInstructorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_instructor, container, false);

        ListView instructorListView = (ListView) view.findViewById(R.id.listViewChat);
        instructorListView.setAdapter(getInstructorAdapter());

        instructorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public InstructorListAdapter getInstructorAdapter()
    {
        InstructorListAdapter instructorListAdapter = new InstructorListAdapter(getContext());
        instructorListAdapter.addInstructor("Goldwin Japar");
        instructorListAdapter.addInstructor("Michelle Neysa");
        return instructorListAdapter;
    }
}
