package com.example.mopay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerViewAdapter extends FragmentPagerAdapter {

    public PagerViewAdapter(FragmentManager fm){

        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null ;

        switch (position){

            case 0 :
                fragment = new About_meFragment();
                break;
            case 1 :
                fragment = new CardFragment();
                break;
            case 2:
                fragment = new SecurityFragment();
                break;

                default: fragment = new About_meFragment();
                    break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}


