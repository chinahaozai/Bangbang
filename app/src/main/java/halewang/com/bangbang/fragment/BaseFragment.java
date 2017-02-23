package halewang.com.bangbang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import halewang.com.bangbang.presenter.Presenter;
import halewang.com.bangbang.view.BaseView;

/**
 * Created by halewang on 2017/2/23.
 */

public abstract class BaseFragment<V extends BaseView, P extends Presenter<V>> extends Fragment {

    private static final String TAG = "BaseFragment";
    protected P mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPresenter = initPresenter();
        if(mPresenter != null) {
            mPresenter.attachView((V) this);
            mPresenter.onCreate();
        }else{
            Log.d(TAG, "onViewCreated: mPresenter is null");
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public abstract P initPresenter();
}
