package halewang.com.bangbang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import halewang.com.bangbang.model.User;
import halewang.com.bangbang.utils.FileUtil;
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
    private static final int REQUEST_CODE_PICK = 0;		// 相册选图标记
    private static final int REQUEST_CODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUEST_CODE_CUTTING = 2;	// 图片裁切标记
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private String urlpath; // 图片本地路径
    private String avatarPath;  //上传后头像地址


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

    private void initView(){
        rootLayout = (LinearLayout) findViewById(R.id.activity_user_info);
        rlHeader = (RelativeLayout) findViewById(R.id.rl_header_item);
        rlUserName = (RelativeLayout) findViewById(R.id.rl_user_item);
        rlPassword = (RelativeLayout) findViewById(R.id.rl_password_item);
        tvUserName = (TextView) findViewById(R.id.tv_username);
        ivAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
    }

    private void initData(){
        menuWindow = new SelectPicPopupWindow(this, itemsOnClick);

        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.showAtLocation(rootLayout,
                        Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                    if(e==null){
                        Toast.makeText(UserInfoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                        avatarPath = bmobFile.getFileUrl();
                        commitUserInfo(AVATAR,avatarPath);
                    }else{
                        Toast.makeText(UserInfoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
        }
    }
    private void commitUserInfo(final int key, final String value){
        final User user = new User();
        switch (key){
            case USERNAME:
                if(!TextUtils.isEmpty(value)){
                    user.setName(value);
                }else{
                    Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            /*case PASSWORD:
                if(!TextUtils.isEmpty(value)) {
                    if (PatternUtil.matchPassword(etPassword.getText().toString())) {
                        user.setPassword(value);
                    }else{
                        Toast.makeText(this,"请使用6-16位且不包含特殊字符的密码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;*/
            case AVATAR:
                user.setAvatar(value);
                break;
            default:
                break;
        }
        user.update(PrefUtil.getString(this, Constant.OBJECT_ID, ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(UserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    switch (key){
                        case USERNAME:
                            PrefUtil.putString(UserInfoActivity.this, Constant.USER,value);
                            break;

                        case AVATAR:
                            PrefUtil.putString(UserInfoActivity.this, Constant.AVATAR,value);
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
}
