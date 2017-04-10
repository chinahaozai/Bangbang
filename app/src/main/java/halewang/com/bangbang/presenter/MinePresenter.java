package halewang.com.bangbang.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import halewang.com.bangbang.AboutActivity;
import halewang.com.bangbang.ClaimActivity;
import halewang.com.bangbang.Constant;
import halewang.com.bangbang.LoginActivity;
import halewang.com.bangbang.MyRequirementActivity;
import halewang.com.bangbang.UserInfoActivity;
import halewang.com.bangbang.utils.PrefUtil;
import halewang.com.bangbang.view.FragmentMineView;

/**
 * Created by halewang on 2017/2/23.
 */

public class MinePresenter extends BasePresenter<FragmentMineView>{
    private Context mContext;
    private RelativeLayout rlUserItem;
    private TextView tvUser;
    private LinearLayout myRequirement;
    private LinearLayout claimRequirement;
    private LinearLayout aboutRequirement;
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
        myRequirement = getMView().getMyRequirenment();
        claimRequirement = getMView().getClaimRequirement();
        aboutRequirement = getMView().getAboutAPP();
    }

    private void initData(){
        rlUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrefUtil.getBoolean(mContext, Constant.IS_ONLINE,false)) {
                    mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
                }else {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });

        myRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MyRequirementActivity.class));
            }
        });

        claimRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ClaimActivity.class));
            }
        });

        aboutRequirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, AboutActivity.class));
            }
        });

    }

}
