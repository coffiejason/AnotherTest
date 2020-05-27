package com.figure.anothertest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.figure.anothertest.tabs.Tab1;
import com.figure.anothertest.tabs.Tab2;
import com.figure.anothertest.tabs.Tab3;

public class PageAdapter extends FragmentPagerAdapter {
    private int numoftabs;


    public PageAdapter(@NonNull FragmentManager fm,int numoftabs) {
        super(fm);
        this.numoftabs = numoftabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tab1();
            case 1:
                //return new Tab2();
            case 2:
                return new Tab3();
            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
