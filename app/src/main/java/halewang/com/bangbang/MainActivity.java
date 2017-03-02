package halewang.com.bangbang;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import halewang.com.bangbang.presenter.MainPresenter;
import halewang.com.bangbang.view.MainView;
import halewang.com.bangbang.widght.NoScrollViewPager;

public class MainActivity extends BaseActivity<MainView,MainPresenter>
        implements MainView{

    private static final String TAG = "MainActivity";
    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;
    BmobPushManager<BmobInstallation> bmobPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 使用推送服务时的初始化操作
        // 启动推送服务
        BmobPush.setDebugMode(true);
        BmobPush.startWork(this);
        bmobPush = new BmobPushManager<BmobInstallation>();
        BmobInstallation.getCurrentInstallation().save();

        mViewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mPresenter.onCreate();

        /*Button button = (Button) findViewById(R.id.btn_test);
        //testSingleNotify();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPushAll();
            }
        });*/

    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public NoScrollViewPager getViewPager() {
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

    private void testPushAll(){
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("deviceType", "android");
        bmobPush.setQuery(query);
        bmobPush.pushMessage("这可是官网的例子 应该可以了吧", new PushListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    Log.e(TAG, "推送错误:" + e.toString());
                }
            }
        });
    }
}
