package halewang.com.bangbang.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by halewang on 2017/2/22.
 */

public interface MainView extends BaseView{

    ViewPager getViewPager();
    TabLayout getTabLayout();
}
