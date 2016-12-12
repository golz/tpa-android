package com.michelle.goldwin.tpamobile.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.michelle.goldwin.tpamobile.R;

import java.util.ArrayList;

/**
 * Created by Michelle Neysa on 12/9/2016.
 */

public class TodoListAdapter extends BaseAdapter{

    private ArrayList<String> todoList;
    private Context context;

    public TodoListAdapter(Context context) {
        super();
        this.context = context;
        todoList = new ArrayList<>();
    }

    public void addData(String name){ todoList.add(name);}
    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int i) {
        return todoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(null == view)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.single_todo_list,null);
        }

        TextView value = (TextView) view.findViewById(R.id.todolistTxt);
        CheckBox cb = (CheckBox) view.findViewById(R.id.todolistCheckBox);

        value.setText(getItem(i).toString());
        cb.setChecked(false);

        return view;
    }


}
