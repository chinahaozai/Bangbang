package halewang.com.bangbang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import halewang.com.bangbang.R;
import halewang.com.bangbang.presenter.HomePresenter;
import halewang.com.bangbang.view.FragmentHomeView;

/**
 * Created by halewang on 2017/2/23.
 */

public class HomeFragment extends BaseFragment<FragmentHomeView,HomePresenter> implements FragmentHomeView {
    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter(getActivity());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
