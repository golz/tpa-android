package com.michelle.goldwin.tpamobile.chatinstructor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michelle.goldwin.tpamobile.R;

import java.util.ArrayList;

/**
 * Created by Goldwin on 2/12/2016.
 */

public class InstructorListAdapter extends BaseAdapter{

    private ArrayList<String> instructorList;
    private Context context;

    public InstructorListAdapter(Context context) {
        super();
        this.context = context;
        instructorList = new ArrayList<>();
    }

    public void addInstructor(String name)
    {
        instructorList.add(name);
    }

    @Override
    public int getCount() {
        return instructorList.size();
    }

    @Override
    public Object getItem(int i) {
        return instructorList.get(i);
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
            view = inflater.inflate(R.layout.single_instructor,null);
        }
        TextView instructorName = (TextView) view.findViewById(R.id.lblInstructorName);
        instructorName.setText(getItem(i).toString());

        //view.setTag(); buat id nanti
        return view;
    }
}
