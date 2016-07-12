package space.hamsters.clockdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by hamster on 16/7/12.
 *
 * The only ViewPager adapter here
 */
public class AlarmClockFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<PageFragment> mFragments;

    AlarmClockFragmentPagerAdapter(FragmentManager fm, ArrayList<PageFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((PageFragment)getItem(position)).getTitle();
    }
}
