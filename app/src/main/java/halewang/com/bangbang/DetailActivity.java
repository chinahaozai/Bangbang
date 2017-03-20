package halewang.com.bangbang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.model.Requirement;

public class DetailActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private CircleImageView avatar;
    private TextView phone;
    private TextView user;
    private TextView money;
    private TextView title;
    private TextView content;
    private TextView updateTime;
    private TextView site;
    private Button btnEnsure;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        user = (TextView) findViewById(R.id.tv_user);
        money = (TextView) findViewById(R.id.tv_money);
        title = (TextView) findViewById(R.id.tv_title);
        content = (TextView) findViewById(R.id.tv_content);
        updateTime = (TextView) findViewById(R.id.tv_update_time);
        site = (TextView) findViewById(R.id.tv_location);
        btnEnsure = (Button) findViewById(R.id.btn_ensure);

    }

    private void initData(){
        Requirement requirement = (Requirement) getIntent().getBundleExtra("detail").getSerializable("requirement");
        phone.setText(requirement.getInitiatorPhone());
        //user.setText(requirement.getInitiatorPhone());
        money.setText(requirement.getMoney()+"¥");
        title.setText(requirement.getTitle());
        content.setText(requirement.getContent());
        updateTime.setText(requirement.getUpdatedAt());
        site.setText(requirement.getSite());
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("latitude",latitude);
                bundle.putString("longitude",longitude);
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
                        Toast.makeText(DetailActivity.this,"数据发生变化，请重试",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }
}
