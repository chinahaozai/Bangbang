package halewang.com.bangbang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.utils.PrefUtil;

public class PostRequirementActivity extends AppCompatActivity {

    private static final String TAG = "PostRequirementActivity";
    private static final String STATUS_ONE = "one"; //代表有人接受这个任务
    private static final String STATUS_ZERO = "zero"; //代表没有人接受这个任务

    private Toolbar mToolbar;
    private EditText etTitle;
    private EditText etMoney;
    private EditText etContent;
    private RelativeLayout waitLayout;
    private AMapLocationClient mLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String location;
    private double latitude;
    private double longitude;
    private boolean isWrite = false;    //判断是否是从点击编辑启动的界面


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_requirement);

        initToolBar();
        initView();
        initLocation();
        initData();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("需求发布");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_requirement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post:
                post();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initView(){
        etTitle = (EditText) findViewById(R.id.et_title);
        etMoney = (EditText) findViewById(R.id.et_money);
        etContent = (EditText) findViewById(R.id.et_content);
        waitLayout = (RelativeLayout) findViewById(R.id.wait_layout);
    }

    private void initLocation(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        Log.d(TAG, "onLocationChanged: " + amapLocation.getAddress());
                        location = amapLocation.getAddress();
                        latitude = amapLocation.getLatitude();
                        longitude = amapLocation.getLongitude();
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    private void post(){
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        String money = etMoney.getText().toString();
        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(money)){
            Toast.makeText(PostRequirementActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        waitLayout.setVisibility(View.VISIBLE);
        Requirement requirement = new Requirement();
        requirement.setSite(location);
        requirement.setTitle(title);
        //requirement.setMoney(money);
        requirement.setMoney(Integer.valueOf(money));
        requirement.setContent(content);
        requirement.setTime(getCurrentTime());
        requirement.setWatchCount(1);
        requirement.setStatus(STATUS_ZERO);
        requirement.setInitiatorPhone(PrefUtil.getString(PostRequirementActivity.this,Constant.PHONE,""));
        requirement.setReceiverPhone("");
        requirement.setInstallationId(BmobInstallation.getInstallationId(PostRequirementActivity.this));
        requirement.setLatitude(String.valueOf(latitude));
        requirement.setLongitude(String.valueOf(longitude));
        requirement.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Toast.makeText(PostRequirementActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(PostRequirementActivity.this,"发布失败，请重试",Toast.LENGTH_SHORT).show();
                }
                waitLayout.setVisibility(View.GONE);
            }
        });
    }

    private String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private void initData(){
        if(getIntent().getBundleExtra("detail")!=null) {
            Requirement requirement = (Requirement) getIntent().getBundleExtra("detail").getSerializable("requirement");
            if (requirement != null) {
                isWrite = true;
                etTitle.setText(requirement.getTitle());
                etContent.setText(requirement.getContent());
                etMoney.setText(String.valueOf(requirement.getMoney()));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isWrite){
            showGiveUp();
        }else{
            super.onBackPressed();
        }
    }

    private void showGiveUp(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setTitle("放弃编辑");
        dialog.setMessage("若放弃编辑，则此次编辑的内容将会丢失，并无法回复，确定放弃？");
        dialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();

    }
}
