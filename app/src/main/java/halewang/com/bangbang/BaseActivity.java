package halewang.com.bangbang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import halewang.com.bangbang.view.BaseView;
import halewang.com.bangbang.presenter.Presenter;

/**
 * Created by halewang on 2017/2/22.
 */

public abstract class BaseActivity<V extends BaseView, P extends Presenter<V>> extends AppCompatActivity{

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = initPresenter();
        mPresenter.attachView((V)this);
    }

    protected abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
