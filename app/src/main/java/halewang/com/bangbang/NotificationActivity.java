package halewang.com.bangbang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.model.User;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";

    private Toolbar mToolbar;
    private CircleImageView avatar;
    private TextView phone;
    private TextView tvUser;
    private TextView call;
    private TextView title;
    private TextView content;
    private TextView updateTime;
    private Requirement mRequirement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
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

    private void initView(){
        avatar = (CircleImageView) findViewById(R.id.iv_avatar);
        phone = (TextView) findViewById(R.id.tv_phone_num);
        tvUser = (TextView) findViewById(R.id.tv_user);
        call = (TextView) findViewById(R.id.tv_call);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        updateTime = (TextView) findViewById(R.id.tv_update_time);
    }

    private void initData(){
        BmobQuery<Requirement> query = new BmobQuery<>();
        Log.d(TAG, "initData: objectId is " + getIntent().getBundleExtra("detail").getString("objectId"));
        query.addWhereEqualTo("objectId",getIntent().getBundleExtra("detail").getString("objectId"));
        query.findObjects(new FindListener<Requirement>() {
            @Override
            public void done(List<Requirement> list, BmobException e) {
                if(e == null && list.size() > 0){
                    mRequirement = list.get(0);
                    phone.setText(mRequirement.getInitiatorPhone());
                    //user.setText(requirement.getInitiatorPhone());
                    title.setText(mRequirement.getTitle());
                    content.setText(mRequirement.getContent());
                    updateTime.setText(mRequirement.getUpdatedAt());
                    initUser(mRequirement.getReceiverPhone());
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callReceiver(mRequirement.getReceiverPhone());
                        }
                    });
                }
            }
        });
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
                        Glide.with(NotificationActivity.this)
                                .load(user1.getAvatar())
                                .into(avatar);
                    }
                }
            }
        });
    }

    private void callReceiver(String phoneNum){
        Toast.makeText(NotificationActivity.this,phoneNum,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }
}
