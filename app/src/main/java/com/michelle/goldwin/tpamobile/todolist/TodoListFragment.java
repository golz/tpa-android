package com.michelle.goldwin.tpamobile.todolist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.global.LoggedUserInformation;
import com.michelle.goldwin.tpamobile.object.Calorie;
import com.michelle.goldwin.tpamobile.object.ChatMessage;
import com.michelle.goldwin.tpamobile.object.History;
import com.michelle.goldwin.tpamobile.object.TodoList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoListFragment extends Fragment{


    private DatabaseReference databaseReference;
    private Bundle extra;
    private FirebaseListAdapter<TodoList> adapter;

    public TodoListFragment() {
        // Required empty public constructor
    }

    public void displayList(View view){

        ListView listView = (ListView) view.findViewById(R.id.todolistListView);

        adapter = new FirebaseListAdapter<TodoList>(getActivity(),TodoList.class,R.layout.single_todo_list,FirebaseDatabase.getInstance().getReference().child("todolist/Lose Weight")) {
            @Override
            protected void populateView(View v, TodoList model, int position) {

                final TextView name = (TextView) v.findViewById(R.id.todoListTxt);
                final TextView cal = (TextView) v.findViewById(R.id.todoListCal);
                CheckBox cb = (CheckBox) v.findViewById(R.id.todoListCheckBox);

                DatabaseReference ref = getRef(position);
                name.setText(ref.getKey());
                /* ADD POSITIVE AND NEGATIVE VIEW FOR USER */
                cal.setText("-"+Integer.toString(model.getCal()));
                cal.setTextColor(getResources().getColor(R.color.colorGreen));


                cb.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        History history = new History(name.getText().toString(),Double.parseDouble(cal.getText().toString()),currentDateTimeString);
                        FirebaseDatabase.getInstance().getReference().child("histories").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(history);
                        Snackbar.make(view, "Added to history success", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        //Modify current calorie too
                        /* BEGIN BATAS GD*/
                        Date today = new Date();
                        try {
                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date todayWithZeroTime = formatter.parse(formatter.format(today));
                            if(LoggedUserInformation.getInstance().getCurrentCalorie() >= Double.parseDouble(cal.getText().toString()))
                            {
                                Calorie calorie = new Calorie(LoggedUserInformation.getInstance().getCurrentCalorie() + Double.parseDouble(cal.getText().toString()));
                                LoggedUserInformation.getInstance().setCurrentCalorie(LoggedUserInformation.getInstance().getCurrentCalorie() + calorie.value);
                                FirebaseDatabase.getInstance().getReference().child("calories").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(todayWithZeroTime.toString()).setValue(calorie);
                            }
                            else
                            {
                                Snackbar.make(view,"You need to eat something",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        /* END BATAS GD*/
                    }
                });

            }
        };

        listView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        displayList(view);



        return view;
    }
}
