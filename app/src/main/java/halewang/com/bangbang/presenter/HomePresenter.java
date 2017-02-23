package halewang.com.bangbang.presenter;

import android.content.Context;

import halewang.com.bangbang.view.FragmentHomeView;

/**
 * Created by halewang on 2017/2/23.
 */

public class HomePresenter extends BasePresenter<FragmentHomeView>{
    private Context mContext;
    public HomePresenter(Context mContext){
        this.mContext = mContext;
    }
}
