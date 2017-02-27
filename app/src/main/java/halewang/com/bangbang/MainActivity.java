package halewang.com.bangbang;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import halewang.com.bangbang.presenter.MainPresenter;
import halewang.com.bangbang.view.MainView;
import halewang.com.bangbang.widght.MainViewPager;

public class MainActivity extends BaseActivity<MainView,MainPresenter>
        implements MainView{

    private static final String TAG = "MainActivity";
    private MainViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化BmobSDK
        Bmob.initialize(this, "c791fc72fdee4d5cc79a1160fb675b58");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);
        mViewPager = (MainViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mPresenter.onCreate();

        //testSingleNotify();
        testAllPush();
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

    private void testSingleNotify(){
        String installationId = "C50AA889B95207A21576AEA3C0EBFEAF";
        BmobPushManager bmobPush = new BmobPushManager();
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("installationId", installationId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage("这是我用手机发送的通知");
    }

    private void testAllPush(){
        BmobPushManager bmobPush = new BmobPushManager();
        bmobPush.pushMessageAll("Hello Bmob.");
        Log.d(TAG, "testAllPush: 执行了这一段代码，但是为啥就不好使呢");
    }
}
