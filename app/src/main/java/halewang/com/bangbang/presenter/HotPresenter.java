package halewang.com.bangbang.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import halewang.com.bangbang.adapter.RequirementAdapter;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.view.HotView;
import halewang.com.bangbang.view.MoneyListView;

/**
 * Created by halewang on 2017/3/13.
 */

public class HotPresenter extends BasePresenter<HotView>{

    private static final String TAG = "MoneyListPresenter";

    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RequirementAdapter mAdapter;
    private int start = 0;
    private final int LIMIT = 20;

    public HotPresenter(Context mContext){
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
        query.setLimit(LIMIT).order("-watchCount").addWhereEqualTo("receiverPhone","");
        start += LIMIT;
        query.findObjects(new FindListener<Requirement>() {
            @Override
            public void done(List<Requirement> list, BmobException e) {
                if (e == null) {
                    mAdapter = new RequirementAdapter(list);
                    mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequested() {
                            loadMore();
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.e(TAG, "查询失败: " + e.getMessage());
                    Toast.makeText(mContext, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMore() {
        BmobQuery<Requirement> query = new BmobQuery<>();
        query.setLimit(LIMIT)
                .setSkip(start)
                .order("-watchCount")
                .addWhereEqualTo("receiverPhone","")
                .findObjects(new FindListener<Requirement>() {
                    @Override
                    public void done(List<Requirement> list, BmobException e) {
                        Log.d(TAG, "start: " + start);
                        Log.d(TAG, "size: " + list.size());
                        if (e == null) {
                            if (list.size() < LIMIT) {
                                mAdapter.loadMoreEnd();
                            }
                            mAdapter.addData(list);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "查询失败: " + e.getMessage());
                            Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        start += LIMIT;
    }

    private void initRefresh(){
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.loadMoreComplete();
                BmobQuery<Requirement> query = new BmobQuery<>();
                query.setLimit(LIMIT).order("-watchCount").addWhereEqualTo("receiverPhone","");
                start = LIMIT;
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
}
