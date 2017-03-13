package halewang.com.bangbang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import halewang.com.bangbang.presenter.WatchRequirementPresenter;
import halewang.com.bangbang.view.WatchRequirementView;

public class WatchRequirementActivity extends BaseActivity<WatchRequirementView, WatchRequirementPresenter> implements WatchRequirementView {

    private LinearLayout llBack;
    private LinearLayout llSearch;
    private TextView tvSelect;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_requirement);
        initView();
        mPresenter.onCreate();
        initData();
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llSearch = (LinearLayout) findViewById(R.id.ll_search);
        tvSelect = (TextView) findViewById(R.id.tv_select);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void initData(){
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WatchRequirementActivity.this,SearchActivity.class));
            }
        });
    }

    @Override
    protected WatchRequirementPresenter initPresenter() {
        return new WatchRequirementPresenter(this);
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

}
