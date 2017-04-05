package halewang.com.bangbang.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.youth.banner.Banner;

import halewang.com.bangbang.PostRequirementActivity;
import halewang.com.bangbang.WatchRequirementActivity;
import halewang.com.bangbang.model.zhihu.ApiManage;
import halewang.com.bangbang.model.zhihu.NewsBean;
import halewang.com.bangbang.model.zhihu.NewsDetailBean;
import halewang.com.bangbang.view.FragmentHomeView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by halewang on 2017/2/23.
 */

public class HomePresenter extends BasePresenter<FragmentHomeView> {

    private static final String TAG = "HomePresenter";

    private Context mContext;
    private Button btnSend;
    private Button btnFind;
    private Banner mBanner;

    public HomePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
    }

    private void initView() {
        btnSend = getMView().getSendButton();
        btnFind = getMView().getFindButton();
        mBanner = getMView().getBanner();
    }

    private void initData() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PostRequirementActivity.class));
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, WatchRequirementActivity.class));
            }
        });

        ApiManage.getNewsService().getNews()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsBean>() {
                    @Override
                    public void call(NewsBean newsBean) {
                        Log.d(TAG, "成功获取到数据: " + newsBean.toString());
                    }
                });
        ApiManage.getNewsDetailService().getNews("3977867")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsDetailBean>() {
                    @Override
                    public void call(NewsDetailBean newsDetailBean) {
                        Log.d(TAG, "成功获取到详情数据: " + newsDetailBean.toString());
                    }
                });
    }
}
