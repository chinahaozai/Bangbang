package halewang.com.bangbang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import halewang.com.bangbang.R;
import halewang.com.bangbang.presenter.ListPresenter;
import halewang.com.bangbang.presenter.MinePresenter;
import halewang.com.bangbang.view.FragmentListView;
import halewang.com.bangbang.view.FragmentMineView;

/**
 * Created by halewang on 2017/2/23.
 */

public class MineFragment extends BaseFragment<FragmentMineView,MinePresenter> implements FragmentMineView {
    @Override
    public MinePresenter initPresenter() {
        return new MinePresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
