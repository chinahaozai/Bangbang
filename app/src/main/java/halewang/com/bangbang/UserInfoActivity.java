package halewang.com.bangbang;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.model.User;
import halewang.com.bangbang.utils.FileUtil;
import halewang.com.bangbang.utils.MD5Util;
import halewang.com.bangbang.utils.PatternUtil;
import halewang.com.bangbang.utils.PrefUtil;
import halewang.com.bangbang.widght.SelectPicPopupWindow;

public class UserInfoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout rootLayout;
    private RelativeLayout rlHeader;
    private RelativeLayout rlUserName;
    private RelativeLayout rlPassword;
    private TextView tvUserName;
    private CircleImageView ivAvatar;

    private static final int USERNAME = 0;
    private static final int PASSWORD = 1;
    private static final int AVATAR = 2;

    private SelectPicPopupWindow menuWindow;
    private static final int REQUEST_CODE_PICK = 0;        // 相册选图标记
    private static final int REQUEST_CODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUEST_CODE_CUTTING = 2;    // 图片裁切标记
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private String urlpath; // 图片本地路径
    private String avatarPath;  //上传后头像地址
    private float alpha = 1f;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    backgroundAlpha((float)msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        initToolBar();
        initData();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("个人资料");
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
        rootLayout = (LinearLayout) findViewById(R.id.activity_user_info);
        rlHeader = (RelativeLayout) findViewById(R.id.rl_header_item);
        rlUserName = (RelativeLayout) findViewById(R.id.rl_name_item);
        rlPassword = (RelativeLayout) findViewById(R.id.rl_password_item);
        tvUserName = (TextView) findViewById(R.id.tv_username);
        ivAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
    }

    private void initData() {
        menuWindow = new SelectPicPopupWindow(this, itemsOnClick);

        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        while(alpha>0.35f){
                            try {
                                //4是根据弹出动画时间和减少的透明度计算
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg =mHandler.obtainMessage();
                            msg.what = 1;
                            //每次减少0.01，精度越高，变暗的效果越流畅
                            alpha-=0.01f;
                            msg.obj =alpha ;
                            mHandler.sendMessage(msg);
                        }
                    }

                }).start();
                menuWindow.showAtLocation(rootLayout,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        rlUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeName();
            }
        });

        rlPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePassword();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PrefUtil.getBoolean(this, Constant.IS_ONLINE, false)) {
            if (!TextUtils.isEmpty(PrefUtil.getString(this, Constant.AVATAR, ""))) {
                Glide.with(this)
                        .load(PrefUtil.getString(this, Constant.AVATAR, ""))
                        .into(ivAvatar);
            }
        }

        tvUserName.setText(!PrefUtil.getString(this, Constant.USER, "").equals("")
                ? PrefUtil.getString(this, Constant.USER, "") : "");
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    takePhoto();
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    pickPhoto();
                    break;
                default:
                    break;
            }
        }
    };

    public void takePhoto() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        startActivityForResult(takeIntent, REQUEST_CODE_TAKE);
    }

    public void pickPhoto() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, REQUEST_CODE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUEST_CODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUEST_CODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            urlpath = FileUtil.saveFile(this, "temphead.jpg", photo);
            ivAvatar.setImageBitmap(photo);
            final BmobFile bmobFile = new BmobFile(new File(urlpath));
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(UserInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        avatarPath = bmobFile.getFileUrl();
                        commitUserInfo(AVATAR, avatarPath);
                    } else {
                        Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
        }
    }

    private void commitUserInfo(final int key, final String value) {
        final User user = new User();
        switch (key) {
            case USERNAME:
                if (!TextUtils.isEmpty(value)) {
                    user.setName(value);
                } else {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case PASSWORD:
                if(!TextUtils.isEmpty(value)) {
                    user.setPassword(value);

                }else{
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case AVATAR:
                user.setAvatar(value);
                PrefUtil.putString(this,Constant.AVATAR,value);
                break;
            default:
                break;
        }
        user.update(PrefUtil.getString(this, Constant.OBJECT_ID, ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(UserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    switch (key) {
                        case USERNAME:
                            PrefUtil.putString(UserInfoActivity.this, Constant.USER, value);
                            break;

                        case AVATAR:
                            PrefUtil.putString(UserInfoActivity.this, Constant.AVATAR, value);
                            break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(UserInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    //Popupwindow退出时，背景渐变回去
    public void onDismiss() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                //此处while的条件alpha不能<= 否则会出现黑屏
                while(alpha<1f){
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("HeadPortrait","alpha:"+alpha);
                    Message msg =mHandler.obtainMessage();
                    msg.what = 1;
                    alpha+=0.01f;
                    msg.obj =alpha ;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();
    }

    private void showChangeName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(UserInfoActivity.this,R.layout.dialog_change_name,null);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_dialog_confirm);
        final EditText etName = (EditText) view.findViewById(R.id.et_dialog_name);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etName.getText().toString())){
                    commitUserInfo(USERNAME,etName.getText().toString());
                    dialog.dismiss();
                }else{
                    Toast.makeText(UserInfoActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void showChangePassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(UserInfoActivity.this,R.layout.dialog_change_password,null);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_dialog_confirm);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_dialog_password);
        final EditText etPasswordNew = (EditText) view.findViewById(R.id.et_dialog_new_password);
        final EditText etPasswordNewConfirm = (EditText) view.findViewById(R.id.et_dialog_new_password_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String passwordNew = etPasswordNew.getText().toString();
                String passwordNewConfirm = etPasswordNewConfirm.getText().toString();
                if(!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(passwordNew)&&
                        !TextUtils.isEmpty(passwordNewConfirm)){
                    if(!passwordNew.equals(passwordNewConfirm)){
                        Toast.makeText(UserInfoActivity.this,"新密码不一致",Toast.LENGTH_SHORT).show();
                    }else{
                        checkPassword(password, passwordNew, dialog);
                    }
                }else{
                    Toast.makeText(UserInfoActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void checkPassword(final String password, final String newPassword, final AlertDialog dialog){
        BmobQuery<User> userQuery = new BmobQuery<>();
        userQuery.addWhereEqualTo("phone", PrefUtil.getString(UserInfoActivity.this,Constant.PHONE,""));
        userQuery.addWhereEqualTo("password", MD5Util.encrypt(password));
        userQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (list.size() > 0) {
                    commitUserInfo(PASSWORD,MD5Util.encrypt(newPassword));
                    dialog.dismiss();
                } else {
                    Toast.makeText(UserInfoActivity.this, "密码错误,请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
