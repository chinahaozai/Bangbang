package halewang.com.bangbang.presenter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import halewang.com.bangbang.MainActivity;
import halewang.com.bangbang.adapter.ListPagerAdapter;
import halewang.com.bangbang.fragment.RecommendFragment;
import halewang.com.bangbang.fragment.HotFragment;
import halewang.com.bangbang.fragment.MoneyFragment;
import halewang.com.bangbang.view.FragmentListView;

/**
 * Created by halewang on 2017/2/23.
 */

public class ListPresenter extends BasePresenter<FragmentListView>{
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mItems;
    private List<Fragment> mFragments;

    public ListPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initTabLayout();
        initViewPager();
    }

    private void initTabLayout(){
        mTabLayout = getMView().getTabLayout();
        mItems = new ArrayList<>();
        mItems.add("推荐榜");
        mItems.add("赏金榜");
        mItems.add("人气榜");
        mTabLayout.addTab(mTabLayout.newTab().setText(mItems.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mItems.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mItems.get(2)));
    }

    private void initViewPager(){
        mViewPager = getMView().getViewPager();
        mViewPager.setOffscreenPageLimit(2);
        mFragments = new ArrayList<>();
        mFragments.add(new RecommendFragment());
        mFragments.add(new MoneyFragment());
        mFragments.add(new HotFragment());

        MainActivity activity = (MainActivity) mContext;
        ListPagerAdapter mAdapter = new ListPagerAdapter(activity.getSupportFragmentManager(), mFragments, mItems);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
    }
}
