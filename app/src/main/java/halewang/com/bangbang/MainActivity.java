package halewang.com.bangbang;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import halewang.com.bangbang.presenter.MainPresenter;
import halewang.com.bangbang.view.MainView;
import halewang.com.bangbang.widght.MainViewPager;

public class MainActivity extends BaseActivity<MainView,MainPresenter>
        implements MainView{

    private MainViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (MainViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mPresenter.onCreate();
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public MainViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public TabLayout getTabLayout() {
        return mTabLayout;
    }
}
