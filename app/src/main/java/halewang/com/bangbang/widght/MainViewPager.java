package halewang.com.bangbang.widght;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by halewang on 2017/2/23.
 */

public class MainViewPager extends ViewPager{

    public boolean isCanScroll=false;

    public MainViewPager(Context context) {
        super(context);
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isCanScroll){
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!isCanScroll){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
