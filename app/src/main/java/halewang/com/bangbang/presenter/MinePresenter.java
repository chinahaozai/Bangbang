package halewang.com.bangbang.presenter;

import android.content.Context;

import halewang.com.bangbang.view.FragmentMineView;

/**
 * Created by halewang on 2017/2/23.
 */

public class MinePresenter extends BasePresenter<FragmentMineView>{
    private Context mContext;
    public MinePresenter(Context mContext){
        this.mContext = mContext;
    }
}
