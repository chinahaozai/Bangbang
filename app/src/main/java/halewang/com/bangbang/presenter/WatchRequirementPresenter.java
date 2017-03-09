package halewang.com.bangbang.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import halewang.com.bangbang.adapter.RequirementAdapter;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.view.WatchRequirementView;

/**
 * Created by halewang on 2017/3/9.
 */

public class WatchRequirementPresenter extends BasePresenter<WatchRequirementView> {

    private static final String TAG = "WatchRequirementPresent";

    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    public WatchRequirementPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
    }

    private void initView(){
        mRefreshLayout = getMView().getRefreshLayout();
        mRecyclerView = getMView().getRecyclerView();
    }

    private void initData(){
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        BmobQuery<Requirement> query = new BmobQuery<>();
        query.setLimit(40);
        query.findObjects(new FindListener<Requirement>() {
            @Override
            public void done(List<Requirement> list, BmobException e) {
                if(e == null){
                    Log.e(TAG, "done: "+list.toString() );
                    mRecyclerView.setAdapter(new RequirementAdapter(list));
                }else{
                    Log.e(TAG, "查询失败: " + e.getMessage());
                    Toast.makeText(mContext,"查询失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
