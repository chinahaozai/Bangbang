package halewang.com.bangbang.presenter;

import android.content.Context;

import halewang.com.bangbang.view.FragmentHomeView;
import halewang.com.bangbang.view.FragmentListView;

/**
 * Created by halewang on 2017/2/23.
 */

public class ListPresenter extends BasePresenter<FragmentListView>{
    private Context mContext;
    public ListPresenter(Context mContext){
        this.mContext = mContext;
    }
}
