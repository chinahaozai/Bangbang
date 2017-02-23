package halewang.com.bangbang.fragment;

import halewang.com.bangbang.presenter.ListPresenter;
import halewang.com.bangbang.view.FragmentListView;

/**
 * Created by halewang on 2017/2/23.
 */

public class ListFragment extends BaseFragment<FragmentListView,ListPresenter> implements FragmentListView {
    @Override
    public ListPresenter initPresenter() {
        return new ListPresenter(getActivity());
    }
}
