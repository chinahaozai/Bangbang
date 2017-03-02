package halewang.com.bangbang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 这个类就不用MVP了，突然感觉MVP麻烦了，是我封装的问题吗。。还是有时间了解一下MVVM 吧。
 */
public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etPassword;
    private EditText etUser;
    private Button btnLogin;
    private TextView tvForgetPassword;
    private TextView tvRegister;
    private Button QQLogin;
    private Button WeiboLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initToolBar();
        initView();
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

    private void initView(){
        etPassword = (EditText) findViewById(R.id.et_password);
        etUser = (EditText) findViewById(R.id.et_user);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        QQLogin = (Button) findViewById(R.id.qq_login);
        WeiboLogin = (Button) findViewById(R.id.weibo_login);
    }
}
