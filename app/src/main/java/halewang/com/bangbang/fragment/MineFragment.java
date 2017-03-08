package halewang.com.bangbang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.Constant;
import halewang.com.bangbang.R;
import halewang.com.bangbang.presenter.ListPresenter;
import halewang.com.bangbang.presenter.MinePresenter;
import halewang.com.bangbang.utils.PrefUtil;
import halewang.com.bangbang.view.FragmentListView;
import halewang.com.bangbang.view.FragmentMineView;

/**
 * Created by halewang on 2017/2/23.
 */

public class MineFragment extends BaseFragment<FragmentMineView, MinePresenter> implements FragmentMineView {
    private View mView;
    private CircleImageView ivAvatar;
    private TextView tvUser;
    private TextView tvPhoneNum;

    @Override
    public MinePresenter initPresenter() {
        return new MinePresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        ivAvatar = (CircleImageView) mView.findViewById(R.id.iv_avatar);
        tvUser = (TextView) mView.findViewById(R.id.tv_user);
        tvPhoneNum = (TextView) mView.findViewById(R.id.tv_phone_num);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public RelativeLayout getUserItem() {
        return (RelativeLayout) mView.findViewById(R.id.rl_user_item);
    }

    @Override
    public TextView getTvUser() {
        return (TextView) mView.findViewById(R.id.tv_user);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PrefUtil.getBoolean(getActivity(), Constant.IS_ONLINE, false)) {
            if (!TextUtils.isEmpty(PrefUtil.getString(getActivity(), Constant.AVATAR, ""))) {
                Glide.with(this)
                        .load(PrefUtil.getString(getActivity(), Constant.AVATAR, ""))
                        .into(ivAvatar);
            }
        }

        if (PrefUtil.getBoolean(getActivity(), Constant.IS_ONLINE, false)) {

            tvUser.setText(!PrefUtil.getString(getActivity(), Constant.USER, "").equals("")
                    ? PrefUtil.getString(getActivity(), Constant.USER, "")
                    : PrefUtil.getString(getActivity(), Constant.PHONE, ""));

            String string = PrefUtil.getString(getActivity(), Constant.PHONE, "");
            String temp = string.substring(3, 7);

            tvPhoneNum.setText(string.replace(temp, "****"));
        }
    }
}
