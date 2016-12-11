package com.michelle.goldwin.tpamobile.todolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.object.TodoList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoListFragment extends Fragment {

    private DatabaseReference databaseReference;
    private Bundle extra;
    private FirebaseListAdapter<TodoList> adapter;

    public TodoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.todolistListView);
        listView.setAdapter(getData());

        return view;
    }

    public TodoListAdapter getData(){

        final TodoListAdapter todoListAdapter = new TodoListAdapter(getContext());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("todolist/Lose Weight");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                            String name = child.getKey().toString();
                            todoListAdapter.addData(name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return todoListAdapter;
    }
}
