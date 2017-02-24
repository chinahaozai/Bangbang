package halewang.com.bangbang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import halewang.com.bangbang.R;
import halewang.com.bangbang.presenter.ListPresenter;
import halewang.com.bangbang.view.FragmentListView;

/**
 * Created by halewang on 2017/2/23.
 */

public class ListFragment extends BaseFragment<FragmentListView,ListPresenter> implements FragmentListView {
    private View mView;
    @Override
    public ListPresenter initPresenter() {
        return new ListPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_list,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public TabLayout getTabLayout() {
        return (TabLayout) mView.findViewById(R.id.tablayout_list);
    }

    @Override
    public ViewPager getViewPager() {
        return (ViewPager) mView.findViewById(R.id.viewpager_list);
    }
}
