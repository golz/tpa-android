package com.michelle.goldwin.tpamobile.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.michelle.goldwin.tpamobile.R;

import java.util.ArrayList;

/**
 * Created by Michelle Neysa on 12/9/2016.
 */

public class MissionListAdapter extends BaseAdapter{

    private ArrayList<String> missionList;
    private Context context;


    public MissionListAdapter(){

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(null == view){

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.single_instructor,null);

        }

        return null;
    }
}
