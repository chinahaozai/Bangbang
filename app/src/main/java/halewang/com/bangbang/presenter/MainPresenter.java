package halewang.com.bangbang.presenter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import halewang.com.bangbang.view.MainView;

/**
 * Created by halewang on 2017/2/22.
 */

public class MainPresenter extends BasePresenter<MainView>{

    private Context mContext;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public MainPresenter(Context mContext){
        this.mContext = mContext;

        initView();
    }

    private void initView(){
        mViewPager = getMView().getViewPager();
        mTabLayout = getMView().getTabLayout();
    }
}
