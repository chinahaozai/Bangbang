package halewang.com.bangbang.presenter;

import halewang.com.bangbang.view.BaseView;

/**
 * Created by halewang on 2017/2/22.
 */

public class BasePresenter<V extends BaseView> implements Presenter<V>{

    private V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        onDestroy();
    }

    /**
     * 判断 view是否为空
     *
     * @return
     */
    public boolean isAttachView() {
        return mView != null;
    }

    /**
     * 返回目标view
     *
     * @return
     */
    public V getMView() {
        return mView;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy(){

    }
}
