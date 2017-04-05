package halewang.com.bangbang;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.model.User;

public class ClaimDetailActivity extends AppCompatActivity {

    private static final String TAG = "Detail2Activity";
    private static final int REQUEST_CALL_PHONE = 1;

    private Toolbar mToolbar;
    private CircleImageView avatar;
    private TextView phone;
    private TextView tvUser;
    private TextView money;
    private TextView title;
    private TextView call;
    private TextView content;
    private TextView updateTime;
    private Button btnFinish;
    private Requirement mRequirement;
    private boolean haveReceiver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        initToolBar();
        initView();
        initData();
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

    private void initView() {
        avatar = (CircleImageView) findViewById(R.id.iv_avatar);
        phone = (TextView) findViewById(R.id.tv_phone_num);
        call = (TextView) findViewById(R.id.tv_call);
        tvUser = (TextView) findViewById(R.id.tv_user);
        money = (TextView) findViewById(R.id.tv_money);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        updateTime = (TextView) findViewById(R.id.tv_update_time);
        btnFinish = (Button) findViewById(R.id.btn_finish);
    }

    private void initData() {
        mRequirement = (Requirement) getIntent().getBundleExtra("detail").getSerializable("requirement");

        haveReceiver = true;
        call.setVisibility(View.VISIBLE);
        call.setText("联系发起人");
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        phone.setText(mRequirement.getInitiatorPhone());
        //user.setText(requirement.getInitiatorPhone());
        money.setText(mRequirement.getMoney() + "¥");
        title.setText(mRequirement.getTitle());
        content.setText(mRequirement.getContent());
        updateTime.setText(mRequirement.getUpdatedAt());
        initUser(mRequirement.getInitiatorPhone());
    }

    private void initUser(String phoneNum) {
        String temp = phoneNum.substring(3, 7);
        phone.setText(phoneNum.replace(temp, "****"));
        final BmobQuery<User> user = new BmobQuery<>();
        user.addWhereEqualTo("phone", phoneNum);
        user.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list.size() > 0) {
                    User user1 = list.get(0);
                    if (user1.getName().isEmpty()) {
                        tvUser.setVisibility(View.GONE);
                    } else {
                        tvUser.setText(user1.getName());
                    }
                    if (!user1.getAvatar().isEmpty()) {
                        Glide.with(ClaimDetailActivity.this)
                                .load(user1.getAvatar())
                                .into(avatar);
                    }
                }
            }
        });
    }
    private void callReceiver(String phoneNum){
        if (ContextCompat.checkSelfPermission(ClaimDetailActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL_PHONE);
        } else{
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri uri = Uri.parse("tel:" + phoneNum);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL_PHONE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                callReceiver(mRequirement.getReceiverPhone());
            }else{
                Toast.makeText(ClaimDetailActivity.this, "拨打电话权限被拒绝", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
