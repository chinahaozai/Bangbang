package halewang.com.bangbang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by halewang on 2017/2/24.
 */

public class ListPagerAdapter extends FragmentStatePagerAdapter{
    private List<Fragment> mFragments;
    private List<String> mTabs;
    public ListPagerAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> mTabs) {
        super(fm);
        this.mFragments = mFragments;
        this.mTabs = mTabs;
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
        return mTabs.get(position);
    }
}
