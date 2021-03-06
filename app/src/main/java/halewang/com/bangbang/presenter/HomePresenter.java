package halewang.com.bangbang.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import halewang.com.bangbang.Constant;
import halewang.com.bangbang.LoginActivity;
import halewang.com.bangbang.NewsContentActivity;
import halewang.com.bangbang.PostRequirementActivity;
import halewang.com.bangbang.WatchRequirementActivity;
import halewang.com.bangbang.model.zhihu.ApiManage;
import halewang.com.bangbang.model.zhihu.News;
import halewang.com.bangbang.model.zhihu.NewsBean;
import halewang.com.bangbang.model.zhihu.NewsDetailBean;
import halewang.com.bangbang.utils.PrefUtil;
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

    private List<String> titles;
    private List<String> imageUrls;
    private List<String> contentUrls;

    public HomePresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
        initBanner();
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
                if(PrefUtil.getBoolean(mContext,Constant.IS_ONLINE,false)) {
                    mContext.startActivity(new Intent(mContext, PostRequirementActivity.class));
                }else{
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, WatchRequirementActivity.class));
            }
        });

    }

    private void initBanner() {
        titles = new ArrayList<>();
        imageUrls = new ArrayList<>();
        contentUrls = new ArrayList<>();

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(mContext)
                        .load(path)
                        .centerCrop()
                        .into(imageView);
            }
        });
        mBanner.setBannerAnimation(Transformer.DepthPage);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(4000);
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);

        mBanner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(mContext, NewsContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", titles.get(position - 1));
                bundle.putString("showUrl", contentUrls.get(position - 1));
                intent.putExtra("news", bundle);
                mContext.startActivity(intent);
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
                        for (News news : newsBean.getNews()) {
                            titles.add(news.getTitle());
                            imageUrls.add(news.getImage());
                            contentUrls.add(news.getShare_url());
                        }
                        mBanner.setImages(imageUrls);
                        mBanner.setBannerTitles(titles);
                        mBanner.start();
                    }
                });
    }
}
