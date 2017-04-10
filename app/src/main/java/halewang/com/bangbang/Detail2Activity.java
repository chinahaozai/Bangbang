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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.model.Acount;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.model.User;
import halewang.com.bangbang.utils.PrefUtil;

public class Detail2Activity extends AppCompatActivity {

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

    private void initView(){
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

    private void initData(){
        mRequirement = (Requirement) getIntent().getBundleExtra("detail").getSerializable("requirement");

        if(!TextUtils.isEmpty(mRequirement.getReceiverPhone()) && mRequirement.getStatus().equals("zero")){
            haveReceiver = true;
            call.setVisibility(View.VISIBLE);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callReceiver(mRequirement.getReceiverPhone());
                }
            });
            btnFinish.setVisibility(View.VISIBLE);
            btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else{
            //call.setVisibility(View.GONE);
            btnFinish.setEnabled(false);
            btnFinish.setText("已结束");
        }
        phone.setText(mRequirement.getInitiatorPhone());
        //user.setText(requirement.getInitiatorPhone());
        money.setText(mRequirement.getMoney()+"¥");
        title.setText(mRequirement.getTitle());
        content.setText(mRequirement.getContent());
        updateTime.setText(mRequirement.getUpdatedAt());
        initUser(mRequirement.getInitiatorPhone());

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealRequirementFinish();
            }
        });
    }

    private void dealRequirementFinish(){
        BmobQuery<Acount> query = new BmobQuery<>();
        query.addWhereEqualTo("phone",PrefUtil.getString(Detail2Activity.this,Constant.PHONE,null));
        query.findObjects(new FindListener<Acount>() {
            @Override
            public void done(List<Acount> list, BmobException e) {
                if(e == null){
                    Acount acount = list.get(0);
                    int pay = acount.getPay();
                    acount.setPay(pay+mRequirement.getMoney());
                    acount.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                        }
                    });
                }
            }
        });

        BmobQuery<Acount> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("phone",mRequirement.getReceiverPhone());
        query1.findObjects(new FindListener<Acount>() {
            @Override
            public void done(List<Acount> list, BmobException e) {
                if(e == null){
                    Acount acount = list.get(0);
                    int earning = acount.getEarning();
                    acount.setEarning(earning+mRequirement.getMoney());
                    acount.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                        }
                    });
                }
            }
        });

        mRequirement.setStatus("one");
        mRequirement.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null) {
                    Toast.makeText(Detail2Activity.this, "该需求成功结束", Toast.LENGTH_SHORT).show();
                    btnFinish.setEnabled(false);
                    btnFinish.setText("已结束");
                }else{
                    Toast.makeText(Detail2Activity.this, "该需求结束失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail2_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!haveReceiver) {
            switch (item.getItemId()) {
                case R.id.write:
                    Intent intent = new Intent(this, PostRequirementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("requirement", mRequirement);
                    intent.putExtra("detail", bundle);
                    startActivity(intent);
                    return true;
                case R.id.delete:
                    showDeleteDialog();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);

            }
        }else{
            Toast.makeText(Detail2Activity.this,"被认领的任务不能修改和删除",Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        dialog.setTitle("确认删除");
        dialog.setMessage("是否要删除这个需求?");
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRequirement.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            Toast.makeText(Detail2Activity.this,"删除成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(Detail2Activity.this,"删除失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();

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
                        Glide.with(Detail2Activity.this)
                                .load(user1.getAvatar())
                                .into(avatar);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void callReceiver(String phoneNum){
        if (ContextCompat.checkSelfPermission(Detail2Activity.this, Manifest.permission.CALL_PHONE)
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
                Toast.makeText(Detail2Activity.this, "拨打电话权限被拒绝", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
