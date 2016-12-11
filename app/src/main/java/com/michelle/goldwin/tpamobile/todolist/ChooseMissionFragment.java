package com.michelle.goldwin.tpamobile.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.object.TodoList;

/**
 * Created by Michelle Neysa on 12/11/2016.
 */

public class ChooseMissionFragment extends Fragment {

    public ChooseMissionFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_choose_mission, container, false);


        Spinner spinner = (Spinner) view.findViewById(R.id.MissionSpinner);
        Button button = (Button) view.findViewById(R.id.MissionButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TodoListFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_mission,fragment).commit();
            }
        });
        return view;
    }
}
