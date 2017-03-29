package halewang.com.bangbang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.model.User;
import halewang.com.bangbang.utils.PrefUtil;

public class DetailActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private CircleImageView avatar;
    private TextView phone;
    private TextView tvUser;
    private TextView money;
    private TextView title;
    private TextView content;
    private TextView updateTime;
    private TextView site;
    private Button btnEnsure;
    private String latitude;
    private String longitude;
    private String myLatitude = "";
    private String myLongitude = "";
    private AMapLocationClient mLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private Requirement requirement;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mContext = this;
        initToolBar();
        initView();
        initData();
        initLocation();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("需求详情");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView(){
        avatar = (CircleImageView) findViewById(R.id.iv_avatar);
        phone = (TextView) findViewById(R.id.tv_phone_num);
        tvUser = (TextView) findViewById(R.id.tv_user);
        money = (TextView) findViewById(R.id.tv_money);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        updateTime = (TextView) findViewById(R.id.tv_update_time);
        site = (TextView) findViewById(R.id.tv_location);
        btnEnsure = (Button) findViewById(R.id.btn_ensure);

    }

    private void initData(){
        requirement = (Requirement) getIntent().getBundleExtra("detail").getSerializable("requirement");
        if(!TextUtils.isEmpty(requirement.getReceiverPhone())){
            btnEnsure.setEnabled(false);
            btnEnsure.setText("已被认领");
        }
        initUser(requirement.getInitiatorPhone());
        //user.setText(requirement.getInitiatorPhone());
        money.setText(requirement.getMoney()+"¥");
        title.setText(requirement.getTitle());
        content.setText(requirement.getContent());
        updateTime.setText(requirement.getUpdatedAt());
        site.setText(requirement.getSite());
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLongitude.isEmpty()||myLatitude.isEmpty()){
                    Toast.makeText(mContext,"定位中。。。",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(mContext, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("latitude",latitude);
                bundle.putString("longitude",longitude);
                bundle.putString("myLatitude",myLatitude);
                bundle.putString("myLongitude",myLongitude);
                intent.putExtra("position", bundle);
                startActivity(intent);
            }
        });
        latitude = requirement.getLatitude();
        longitude = requirement.getLongitude();
        //判断当前设备是否查看了这条需求，如果没有则该条需求查看数+1
        if(!MainActivity.watchedRequirement.contains(requirement.getObjectId())){
            MainActivity.watchedRequirement.add(requirement.getObjectId());
            requirement.setWatchCount(requirement.getWatchCount()+1);
            requirement.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e != null){
                        Toast.makeText(mContext,"数据发生变化，请重试",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }

        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PrefUtil.getBoolean(mContext,Constant.IS_ONLINE,false)){
                    startActivity(new Intent(mContext,LoginActivity.class));
                }else {
                    showEnsureDialog();
                }
            }
        });
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
                        myLatitude = String.valueOf(amapLocation.getLatitude());
                        myLongitude = String.valueOf(amapLocation.getLongitude());
                    }
                }else{
                    Log.d("halewang", "onLocationChanged: amapLocation is null");
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    private void initUser(String phoneNum){
        String temp = phoneNum.substring(3, 7);
        phone.setText(phoneNum.replace(temp, "****"));
        final BmobQuery<User> user = new BmobQuery<>();
        user.addWhereEqualTo("phone",phoneNum);
        user.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(list.size() > 0){
                    User user1 = list.get(0);
                    if(user1.getName().isEmpty()){
                        tvUser.setVisibility(View.GONE);
                    }else{
                        tvUser.setText(user1.getName());
                    }
                    if(!user1.getAvatar().isEmpty()){
                        Glide.with(mContext)
                                .load(user1.getAvatar())
                                .into(avatar);
                    }
                }
            }
        });
    }

    private void showEnsureDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("认领需求");
        alertDialog.setMessage("确定要认领这个需求吗？");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "点错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateRequirement();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void updateRequirement(){
        requirement.setReceiverPhone(PrefUtil.getString(mContext,Constant.PHONE,""));
        requirement.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    sendNotify();
                    btnEnsure.setEnabled(false);
                    btnEnsure.setText("已被认领");
                }else{
                    Toast.makeText(mContext,"认领失败，请检查网络",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void sendNotify(){
        String installationId = requirement.getInstallationId();
        BmobPushManager bmobPush = new BmobPushManager();
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("installationId", installationId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage("你的需求已经被认领了，点击查看是谁认领了你的需求", new PushListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Snackbar.make(btnEnsure, "认领成功，已通知需求发起人", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
