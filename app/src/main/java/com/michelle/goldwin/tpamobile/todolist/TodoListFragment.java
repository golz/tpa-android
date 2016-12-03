package com.michelle.goldwin.tpamobile.todolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michelle.goldwin.tpamobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoListFragment extends Fragment {


    public TodoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View todoListView = inflater.inflate(R.layout.fragment_todo_list, container, false);

        return todoListView;
    }

}
