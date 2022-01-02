package com.maxgreenbd.edgeperfomance.Models;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.maxgreenbd.edgeperfomance.HomeFragments.FragmentEveningRoutine;
import com.maxgreenbd.edgeperfomance.HomeFragments.FragmentMorningRoutine;
import com.maxgreenbd.edgeperfomance.HomeFragments.FragmentWeeklyRoutine;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment[] fragment = {null};

        if (position == 0) {
            fragment[0] = new FragmentMorningRoutine();
        } else if (position == 1) {
            fragment[0] = new FragmentEveningRoutine();
        } else if (position == 2) {
            fragment[0] = new FragmentWeeklyRoutine();
        }
        return fragment[0];
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Morning Routine";
        }  else if (position == 1) {
            return "Evening Routine";
        }else if (position == 2) {
            return "Weekly Routine";
        }
        return null;
    }

}
