package halewang.com.bangbang.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import halewang.com.bangbang.adapter.RequirementAdapter;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.view.RecommendView;

/**
 * Created by halewang on 2017/3/13.
 */

public class RecommendPresenter extends BasePresenter<RecommendView>{

    private static final String TAG = "MoneyListPresenter";

    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RequirementAdapter mAdapter;
    private int start = 0;
    private final int LIMIT = 20;

    public RecommendPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
        initRefresh();
    }

    private void initView(){
        mRefreshLayout = getMView().getSwipeRefreshLayout();
        mRecyclerView = getMView().getRecyclerView();
    }

    private void initData() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        BmobQuery<Requirement> query = new BmobQuery<>();
        query.addWhereEqualTo("receiverPhone","");
        query.findObjects(new FindListener<Requirement>() {
            @Override
            public void done(List<Requirement> list, BmobException e) {
                if (e == null) {
                    Log.e(TAG, "done: " + list.toString());
                    //list.sort(mComparator);
                    Collections.sort(list,mComparator);
                    mAdapter = new RequirementAdapter(list);

                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.e(TAG, "查询失败: " + e.getMessage());
                    Toast.makeText(mContext, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initRefresh(){
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.loadMoreComplete();
                BmobQuery<Requirement> query = new BmobQuery<>();
                query.addWhereEqualTo("receiverPhone","");
                query.findObjects(new FindListener<Requirement>() {
                    @Override
                    public void done(List<Requirement> list, BmobException e) {
                        if (e == null) {
                            mAdapter.refreshData(list);
                        } else {
                            Toast.makeText(mContext, "查询失败", Toast.LENGTH_SHORT).show();
                        }
                        mRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });
    }

    private Comparator<Requirement> mComparator = new Comparator<Requirement>() {
        @Override
        public int compare(Requirement o1, Requirement o2) {

            return getWeight(o1) > getWeight(o2) ? -1 : getWeight(o1) < getWeight(o2) ? 1 : 0;
        }
    };

    /**
     * 根据时间、赏金、热度和地点四个条件算出Requirement的权重
     * @param requirement
     * @return
     */
    private int getWeight(Requirement requirement){
        return requirement.getMoney() * 1 + requirement.getWatchCount() * 1;
    }
}
