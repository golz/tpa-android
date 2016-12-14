package com.michelle.goldwin.tpamobile.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.michelle.goldwin.tpamobile.todolist.ChooseMissionFragment;

import java.util.ArrayList;

/**
 * Created by Goldwin on 2/12/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    private ArrayList<Fragment> fragmentBody;
    private ArrayList<String> fragmentTitle;
    private FragmentManager fragmentManager;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentBody    = new ArrayList<>();
        fragmentTitle   = new ArrayList<>();
    }
    public void addFragment(Fragment fragment,String title)
    {
        fragmentBody.add(fragment);
        fragmentTitle.add(title);
    }
    public void replaceFragment(Fragment newFragment, String newTitle)
    {
        fragmentBody.set(0,newFragment);
        fragmentTitle.set(0,newTitle);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentBody.get(position);
    }
    @Override
    public int getCount() {
        return fragmentBody.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
