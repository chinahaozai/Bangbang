package halewang.com.bangbang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import halewang.com.bangbang.adapter.ClaimAdapter;
import halewang.com.bangbang.adapter.RequirementAdapter;
import halewang.com.bangbang.model.MessageEvent;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.utils.PrefUtil;

/**
 * 我认领的需求界面
 */
public class ClaimActivity extends AppCompatActivity {

    private static final String TAG = "ClaimActivity";
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ClaimAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);
        initToolBar();
        initRecyclerView();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("认领的需求");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Requirement requirement = (Requirement)adapter.getItem(position);
                switch (view.getId()){
                    case R.id.requirement_item:
                        Intent intent = new Intent(ClaimActivity.this, Detail2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("requirement",requirement);
                        intent.putExtra("detail", bundle);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        BmobQuery<Requirement> query = new BmobQuery<>();
        query.addWhereEqualTo("receiverPhone", PrefUtil.getString(this,Constant.PHONE,""));
        query.findObjects(new FindListener<Requirement>() {
            @Override
            public void done(List<Requirement> list, BmobException e) {
                if (e == null) {
                    mAdapter = new ClaimAdapter(list);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.e(TAG, "查询失败: " + e.getMessage());
                    Toast.makeText(ClaimActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
