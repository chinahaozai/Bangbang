package halewang.com.bangbang.view;

import android.support.design.widget.TabLayout;
import halewang.com.bangbang.widght.MainViewPager;

/**
 * Created by halewang on 2017/2/22.
 */

public interface MainView extends BaseView{

    MainViewPager getViewPager();
    TabLayout getTabLayout();
}
