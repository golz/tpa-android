package com.michelle.goldwin.tpamobile.insertfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michelle.goldwin.tpamobile.R;
import com.michelle.goldwin.tpamobile.object.Food;

import java.util.ArrayList;

/**
 * Created by Goldwin on 6/12/2016.
 */

public class FoodListAdapter extends BaseAdapter{

    private ArrayList<Food> foodList;
    private Context context;

    public FoodListAdapter(Context context) {
        super();
        this.context = context;
        foodList = new ArrayList<>();
    }

    public void addFood(Food food)
    {
        foodList.add(food);
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Food getItem(int i) {
        return foodList.get(i);
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
            view = inflater.inflate(R.layout.single_food,null);
        }
        TextView foodName       = (TextView) view.findViewById(R.id.lblFoodName);
        TextView foodCalorie    = (TextView) view.findViewById(R.id.lblFoodCalorie);
        foodName.setText(getItem(i).foodname);
        foodCalorie.setText(getItem(i).calorie.toString());

        //view.setTag(); buat id nanti
        return view;
    }
}
