package halewang.com.bangbang.presenter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import halewang.com.bangbang.view.FragmentMineView;

/**
 * Created by halewang on 2017/2/23.
 */

public class MinePresenter extends BasePresenter<FragmentMineView>{
    private Context mContext;
    private RelativeLayout rlUserItem;
    private TextView tvUser;
    public MinePresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();

    }

    private void initView(){
        rlUserItem = getMView().getUserItem();
        tvUser = getMView().getTvUser();
    }
}
