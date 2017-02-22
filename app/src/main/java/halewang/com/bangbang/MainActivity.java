package halewang.com.bangbang;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import halewang.com.bangbang.presenter.MainPresenter;
import halewang.com.bangbang.view.MainView;

public class MainActivity extends BaseActivity<MainView,MainPresenter>
        implements MainView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter.onCreate();
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public ViewPager getViewPager() {
        return (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public TabLayout getTabLayout() {
        return (TabLayout) findViewById(R.id.tablayout);
    }
}
