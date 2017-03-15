package halewang.com.bangbang;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.presenter.MainPresenter;
import halewang.com.bangbang.utils.PrefUtil;
import halewang.com.bangbang.view.MainView;
import halewang.com.bangbang.widght.NoScrollViewPager;

public class MainActivity extends BaseActivity<MainView,MainPresenter>
        implements MainView{

    private static final String TAG = "MainActivity";
    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;
    BmobPushManager<BmobInstallation> bmobPush;
    private AMapLocationClient mLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public static List<String> watchedRequirement = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 使用推送服务时的初始化操作
        // 启动推送服务
        BmobPush.setDebugMode(true);
        BmobPush.startWork(this);
        bmobPush = new BmobPushManager<BmobInstallation>();
        if(PrefUtil.getBoolean(this,Constant.FIRST_IN,true)){
            BmobInstallation.getCurrentInstallation().save();
            PrefUtil.putBoolean(this,Constant.FIRST_IN,false);
        }

        mViewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mPresenter.onCreate();
        initLocation();
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
    private void initLocation(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        Log.d(TAG, "onLocationChanged: " + amapLocation.getAddress());
                        PrefUtil.putString(MainActivity.this,Constant.LOCATION,amapLocation.getAddress());
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }
}
