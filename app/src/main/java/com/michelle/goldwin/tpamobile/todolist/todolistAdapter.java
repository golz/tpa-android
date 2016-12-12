package com.michelle.goldwin.tpamobile.todolist;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.object.Food;
import com.michelle.goldwin.tpamobile.object.History;
import com.michelle.goldwin.tpamobile.object.TodoList;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michelle Neysa on 12/9/2016.
 */

public class TodoListAdapter extends BaseAdapter{

    private ArrayList<TodoList> todoList;
    private Context context;

    public TodoListAdapter(Context context) {
        super();
        this.context = context;
        todoList = new ArrayList<>();
    }

    public void addData(TodoList list){ todoList.add(list);}
    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public TodoList getItem(int i) {
        return todoList.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(null == view) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.single_todo_list, null);
        }

            TextView name = (TextView) view.findViewById(R.id.todoListTxt);
            final TextView cal = (TextView) view.findViewById(R.id.todoListCal);
            CheckBox cb = (CheckBox) view.findViewById(R.id.todoListCheckBox);


        return view;
    }


}
