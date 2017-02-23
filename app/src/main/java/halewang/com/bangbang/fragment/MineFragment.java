package halewang.com.bangbang.fragment;

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
}
