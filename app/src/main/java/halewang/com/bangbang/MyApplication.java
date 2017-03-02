package halewang.com.bangbang;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;

/**
 * Created by halewang on 2017/3/2.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "1bbe2cc127098", "de36347226a82d1ec7b716f27250cebe");
        // 初始化BmobSDK
        Bmob.initialize(this, "c791fc72fdee4d5cc79a1160fb675b58");
    }
}
