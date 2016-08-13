package org.hdm.app.sambia.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.hdm.app.sambia.screens.FragmentIntroFour;
import org.hdm.app.sambia.screens.FragmentIntroOne;
import org.hdm.app.sambia.screens.FragmentIntroThird;
import org.hdm.app.sambia.screens.FragmentIntroTwo;

/**
 * Page-Adapter for all Walkthrough-fragments of IntroActivity.
 */
public class IntroPageAdapter extends FragmentStatePagerAdapter {

    /**
     * Intro page count
     */
    final static int NUM_PAGES = 4;

    /**
     * Constructor
     * @param fm
     */
    public IntroPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // for each different position, return another fragment with different layout
        switch(position) {
            case 0:
                Fragment fragmentOne = new FragmentIntroOne();
                return fragmentOne;
            case 1:
                Fragment fragmentTwo = new FragmentIntroFour();
                return fragmentTwo;
            case 2:
                Fragment fragmentThree = new FragmentIntroTwo();
                return fragmentThree;
            case 3:
                Fragment fragmentFour = new FragmentIntroThird();
                return fragmentFour;
            default:
                return new FragmentIntroOne();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}