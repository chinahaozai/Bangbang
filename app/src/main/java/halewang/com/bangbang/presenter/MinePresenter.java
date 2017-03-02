package halewang.com.bangbang.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import halewang.com.bangbang.LoginActivity;
import halewang.com.bangbang.view.FragmentMineView;

/**
 * Created by halewang on 2017/2/23.
 */

public class MinePresenter extends BasePresenter<FragmentMineView>{
    private Context mContext;
    private RelativeLayout rlUserItem;
    private TextView tvUser;
    public MinePresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
    }

    private void initView(){
        rlUserItem = getMView().getUserItem();
        tvUser = getMView().getTvUser();
    }

    private void initData(){
        rlUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
    }

    private void test(){
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
// 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");

// 提交用户信息（此方法可以不调用）
                    //registerUser(country, phone);
                }
            }
        });
        registerPage.show(mContext);
    }
}
