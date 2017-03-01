package halewang.com.bangbang.presenter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import halewang.com.bangbang.MainActivity;
import halewang.com.bangbang.R;
import halewang.com.bangbang.adapter.MainPagerAdapter;
import halewang.com.bangbang.fragment.HomeFragment;
import halewang.com.bangbang.fragment.ListFragment;
import halewang.com.bangbang.fragment.MineFragment;
import halewang.com.bangbang.view.MainView;
import halewang.com.bangbang.widght.NoScrollViewPager;

/**
 * Created by halewang on 2017/2/22.
 */

public class MainPresenter extends BasePresenter<MainView>{
    private static final String TAG = "MainPresenter";

    private Context mContext;
    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private List<String> mTabs;
    private MainActivity mActivity;
    private MainPagerAdapter mAdapter;

    public MainPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new ListFragment());
        mFragments.add(new MineFragment());
        mAdapter = new MainPagerAdapter(mActivity.getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);

        initTabLayout();
    }

    private void initView(){
        if(!isAttachView()){
            Log.d(TAG, "initView: view is empty!!!");
        }else {
            mActivity = (MainActivity) mContext;
            mViewPager = getMView().getViewPager();
            mTabLayout = getMView().getTabLayout();
        }
    }

    private void initTabLayout(){
        mTabs = new ArrayList<>();
        mTabs.add("首页");
        mTabs.add("榜单");
        mTabs.add("我");
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabs.get(0)).setIcon(R.drawable.tab_home_selector));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabs.get(1)).setIcon(R.drawable.tab_list_selector));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTabs.get(2)).setIcon(R.drawable.tab_mine_selector));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position,true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
