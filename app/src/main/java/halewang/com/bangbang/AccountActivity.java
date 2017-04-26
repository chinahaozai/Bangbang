package halewang.com.bangbang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import halewang.com.bangbang.model.Acount;
import halewang.com.bangbang.utils.PrefUtil;
import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class AccountActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ColumnChartView mChartView;
    private ColumnChartData data;
    private final static String[] X = new String[]{"收入", "支出"};
    private int payAndEarn[] = new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initToolBar();
        mChartView = (ColumnChartView) findViewById(R.id.chart);
        initData();
        initChartView();
    }

    private void initData(){
        BmobQuery<Acount> query = new BmobQuery<>();
        query.addWhereEqualTo("phone", PrefUtil.getString(this,Constant.PHONE,""));
        query.findObjects(new FindListener<Acount>() {
            @Override
            public void done(List<Acount> list, BmobException e) {
                Acount acount = list.get(0);
                payAndEarn[0] = acount.getEarning();
                payAndEarn[1] = acount.getPay();
                initChartView();
            }
        });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("账户");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initChartView() {
        // 使用的 2列，每列1个subcolumn。
        int numSubcolumns = 1;
        int numColumns = 2;
        //定义一个圆柱对象集合
        List<Column> columns = new ArrayList<Column>();
        //子列数据集合
        List<SubcolumnValue> values;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //遍历列数numColumns
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            //遍历每一列的每一个子列
            float f = (float) payAndEarn[i];

            for (int j = 0; j < numSubcolumns; ++j) {
                //为每一柱图添加颜色和数值
                //float f = list.get(i);
                values.add(new SubcolumnValue(f, ChartUtils.pickColor()));
            }
            //创建Column对象
            Column column = new Column(values);
            //这一步是能让圆柱标注数据显示带小数的重要一步 让我找了好久问题
            //作者回答https://github.com/lecho/hellocharts-android/issues/185
            /*ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
            column.setFormatter(chartValueFormatter);*/
            //是否有数据标注
            column.setHasLabels(true);
            //是否是点击圆柱才显示数据标注
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
            //给x轴坐标设置描述
            axisValues.add(new AxisValue(i).setLabel(X[i]));
        }
        //创建一个带有之前圆柱对象column集合的ColumnChartData
        data= new ColumnChartData(columns);

        //定义x轴y轴相应参数
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("元(¥)");//轴名称
        axisY.hasLines();//是否显示网格线
        axisY.setTextColor(R.color.new_black);//颜色

        axisX.hasLines();
        axisX.setTextColor(R.color.new_black);
        axisX.setValues(axisValues);
        //把X轴Y轴数据设置到ColumnChartData 对象中
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        mChartView.setZoomEnabled(false);//禁止手势缩放
        //给表填充数据，显示出来
        mChartView.setColumnChartData(data);
    }
}
