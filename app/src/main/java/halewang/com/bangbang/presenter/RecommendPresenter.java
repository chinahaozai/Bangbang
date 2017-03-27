package halewang.com.bangbang.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import halewang.com.bangbang.Constant;
import halewang.com.bangbang.DetailActivity;
import halewang.com.bangbang.R;
import halewang.com.bangbang.adapter.RequirementAdapter;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.utils.PrefUtil;
import halewang.com.bangbang.view.RecommendView;

/**
 * Created by halewang on 2017/3/13.
 */

public class RecommendPresenter extends BasePresenter<RecommendView> {

    private static final String TAG = "RecommendPresenter";

    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RequirementAdapter mAdapter;
    private int start = 0;
    private final int LIMIT = 20;

    public RecommendPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
        initRefresh();
    }

    private void initView() {
        mRefreshLayout = getMView().getSwipeRefreshLayout();
        mRecyclerView = getMView().getRecyclerView();
    }

    private void initData() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Requirement requirement = (Requirement)adapter.getItem(position);
                switch (view.getId()){
                    case R.id.requirement_item:
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("requirement",requirement);
                        intent.putExtra("detail", bundle);
                        mContext.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        BmobQuery<Requirement> query = new BmobQuery<>();
        query.findObjects(new FindListener<Requirement>() {
            @Override
            public void done(List<Requirement> list, BmobException e) {
                if (e == null) {
                    Collections.sort(list, mComparator);
                    mAdapter = new RequirementAdapter(list);

                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.e(TAG, "查询失败: " + e.getMessage());
                    Toast.makeText(mContext, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.loadMoreComplete();
                BmobQuery<Requirement> query = new BmobQuery<>();
                query.findObjects(new FindListener<Requirement>() {
                    @Override
                    public void done(List<Requirement> list, BmobException e) {
                        if (e == null) {
                            Collections.sort(list, mComparator);
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
     *
     * @param requirement
     * @return
     */
    private double getWeight(Requirement requirement) {
        double moneyWeight = Math.log(requirement.getMoney()*0.5);
        double hotWeight = Math.log(requirement.getWatchCount()) * 2;
        double timeWeight = getTimeWeight(requirement.getTime());
        //double sitWeight = getSiteWeight(requirement.getSite());
        return moneyWeight + hotWeight - timeWeight/* + sitWeight*/;
    }

    private int getSiteWeight(String site) {
        String s1 = site.substring(0, site.indexOf("省"));
        String s2 = site.substring(site.indexOf("省"), site.indexOf("市"));
        String mySite = PrefUtil.getString(mContext, Constant.LOCATION, "");
        String s3 = mySite.substring(0, site.indexOf("省"));
        String s4 = mySite.substring(mySite.indexOf("省"), mySite.indexOf("市"));

        int weight = 0;
        if (s1.equals(s3)) {
            weight++;
        }
        if (s2.equals(s4)) {
            weight++;
        }
        return weight * 2;
    }

    private double getTimeWeight(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double days = 0;
        try {
            Date d1 = new Date();
            Date d2 = df.parse(time);
            long diff = d1.getTime() - d2.getTime();
            days = diff / (1000 * 60 * 60 * 24);
            Log.d(TAG, "getTime: " + time);
            Log.d(TAG, "getTimeWeight: "+ Math.log(days));
        } catch (Exception e) {
            Log.e(TAG, "getTimeWeight: " + e.getMessage());
        }
        return Math.log(days)*0.8;
    }
}
