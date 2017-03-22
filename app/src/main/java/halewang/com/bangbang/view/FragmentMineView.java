package halewang.com.bangbang.view;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by halewang on 2017/2/23.
 */

public interface FragmentMineView extends BaseView{
    /**
     * 获取用户条目
     * @return
     */
    RelativeLayout getUserItem();

    /**
     * 获取用户名TextView
     * @return
     */
    TextView getTvUser();

    /**
     * 获取我的需求
     * @return
     */
    LinearLayout getMyRequirenment();

}
