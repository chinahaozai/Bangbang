package halewang.com.bangbang.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Created by halewang on 2017/2/22.
 */

public interface RecommendView extends BaseView{

    SwipeRefreshLayout getSwipeRefreshLayout();
    RecyclerView getRecyclerView();
}
