package halewang.com.bangbang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import halewang.com.bangbang.model.Acount;
import halewang.com.bangbang.model.User;
import halewang.com.bangbang.utils.MD5Util;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnRegister;
    private TextWatcher mWatcher;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initToolBar();
        initView();
        initData();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("注册");
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
        etPasswordConfirm = (EditText) findViewById(R.id.et_password_confirm);
        btnRegister = (Button) findViewById(R.id.btn_register);
    }

    private void initData(){
        phoneNum = getIntent().getBundleExtra("phone").getString("phoneNum");
        mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnRegister.setEnabled(!TextUtils.isEmpty(etPasswordConfirm.getText().toString())
                        && !TextUtils.isEmpty(etPassword.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etPasswordConfirm.addTextChangedListener(mWatcher);
        etPassword.addTextChangedListener(mWatcher);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPasswordConfirm.getText().toString().equals(etPassword.getText().toString())){
                    register(etPassword.getText().toString());
                }else{
                    Toast.makeText(RegisterActivity.this,"密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String password){
        User user = new User();
        user.setPhone(phoneNum);
        user.setPassword(MD5Util.encrypt(password));
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    initAcount();
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"注册失败,请重试",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initAcount(){
        Acount acount = new Acount();
        acount.setPhone(phoneNum);
        acount.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e != null){
                    Toast.makeText(RegisterActivity.this,"初始化账户失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
