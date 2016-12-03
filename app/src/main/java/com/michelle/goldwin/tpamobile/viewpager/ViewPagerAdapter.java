package com.michelle.goldwin.tpamobile.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Goldwin on 2/12/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    private ArrayList<Fragment> fragmentBody;
    private ArrayList<String> fragmentTitle;

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
