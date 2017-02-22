package halewang.com.bangbang.presenter;

import halewang.com.bangbang.view.BaseView;

/**
 * Created by halewang on 2017/2/22.
 */

public interface Presenter<V extends BaseView> {
    /**
     * presenter和对应的view绑定
     * @param mView  目标view
     */
    void attachView(V mView);
    /**
     * presenter与view解绑
     */
    void detachView();

    void onCreate();

    void onDestroy();

}
