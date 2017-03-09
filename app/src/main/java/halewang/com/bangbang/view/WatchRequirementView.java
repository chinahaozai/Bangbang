package halewang.com.bangbang.view;

import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import halewang.com.bangbang.widght.NoScrollViewPager;

/**
 * Created by halewang on 2017/2/22.
 */

public interface WatchRequirementView extends BaseView{

    SwipeRefreshLayout getRefreshLayout();
    RecyclerView getRecyclerView();
}
