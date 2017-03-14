package halewang.com.bangbang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import halewang.com.bangbang.R;
import halewang.com.bangbang.presenter.RecommendPresenter;
import halewang.com.bangbang.view.RecommendView;

/**
 * Created by halewang on 2017/2/24.
 */

public class RecommendFragment extends BaseFragment<RecommendView,RecommendPresenter> implements RecommendView {
    private View mView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_base,null);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public RecommendPresenter initPresenter() {
        return new RecommendPresenter(getActivity());
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
