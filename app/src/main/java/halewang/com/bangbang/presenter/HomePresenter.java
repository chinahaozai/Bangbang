package halewang.com.bangbang.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import halewang.com.bangbang.PostRequirementActivity;
import halewang.com.bangbang.view.FragmentHomeView;

/**
 * Created by halewang on 2017/2/23.
 */

public class HomePresenter extends BasePresenter<FragmentHomeView>{
    private Context mContext;
    private Button btnSend;
    private Button btnFind;
    public HomePresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
    }

    private void initView(){
        btnSend = getMView().getSendButton();
        btnFind = getMView().getFindButton();
    }

    private void initData(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PostRequirementActivity.class));
            }
        });
    }
}
