package halewang.com.bangbang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import halewang.com.bangbang.model.User;
import halewang.com.bangbang.utils.MD5Util;
import halewang.com.bangbang.utils.PrefUtil;

/**
 * 这个类就不用MVP了，突然感觉MVP麻烦了，是我封装的问题吗。。还是有时间了解一下MVVM 吧。
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private Toolbar mToolbar;
    private EditText etPassword;
    private EditText etUser;
    private Button btnLogin;
    private TextView tvForgetPassword;
    private TextView tvRegister;
    private Button QQLogin;
    private Button WeiboLogin;
    private TextWatcher mWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initToolBar();
        initView();
        initData();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("登录");
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
        etPassword = (EditText) findViewById(R.id.et_password);
        etUser = (EditText) findViewById(R.id.et_user);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        QQLogin = (Button) findViewById(R.id.qq_login);
        WeiboLogin = (Button) findViewById(R.id.weibo_login);
    }

    private void initData() {
        mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnLogin.setEnabled(!TextUtils.isEmpty(etUser.getText().toString())
                        && !TextUtils.isEmpty(etPassword.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etUser.addTextChangedListener(mWatcher);
        etPassword.addTextChangedListener(mWatcher);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void register() {
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String phone = (String) phoneMap.get("phone");
                    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phoneNum", phone);
                    intent.putExtra("phone", bundle);
                    startActivity(intent);
                }
            }
        });
        registerPage.show(this);
    }

    private void doLogin() {

        BmobQuery<User> userQuery = new BmobQuery<>();
        userQuery.addWhereEqualTo("phone", etUser.getText().toString());
        userQuery.addWhereEqualTo("password", MD5Util.encrypt(etPassword.getText().toString()));
        userQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list.size() > 0) {
                    User user = list.get(0);
                    PrefUtil.putString(LoginActivity.this, Constant.USER, user.getName());
                    PrefUtil.putString(LoginActivity.this, Constant.PHONE, user.getPhone());
                    PrefUtil.putString(LoginActivity.this, Constant.AVATAR, user.getAvatar());
                    PrefUtil.putString(LoginActivity.this, Constant.OBJECT_ID, user.getObjectId());
                    PrefUtil.putBoolean(LoginActivity.this, Constant.IS_ONLINE, true);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败，用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
