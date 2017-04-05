package halewang.com.bangbang.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import halewang.com.bangbang.DetailActivity;
import halewang.com.bangbang.R;
import halewang.com.bangbang.adapter.RequirementAdapter;
import halewang.com.bangbang.model.Requirement;
import halewang.com.bangbang.view.SearchRequirementView;

/**
 * Created by halewang on 2017/3/13.
 */

public class SearchPresenter extends BasePresenter<SearchRequirementView>{

    private Context mContext;
    private EditText etSearch;
    private TextView tvSearch;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<Requirement> mData;
    private RequirementAdapter mAdapter;


    public SearchPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initData();
    }

    private void initView(){
        etSearch = getMView().getEdtiText();
        tvSearch = getMView().getTextView();
        mRecyclerView = getMView().getRecyclerView();
        mRefreshLayout = getMView().getSwipeRefreshLayout();
    }

    private void initData(){
        mRefreshLayout.setEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Requirement requirement = (Requirement)adapter.getItem(position);
                switch (view.getId()){
                    case R.id.requirement_item:
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("requirement",requirement);
                        intent.putExtra("detail", bundle);
                        mContext.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = etSearch.getText().toString();
                if(TextUtils.isEmpty(word)){
                    Toast.makeText(mContext,"请输入关键字",Toast.LENGTH_SHORT).show();
                    return;
                }
                getSearchRequirement(word);
            }
        });

    }

    private void getSearchRequirement(final String word){
        new BmobQuery<Requirement>().order("-updatedAt").findObjects(new FindListener<Requirement>() {
            @Override
            public void done(List<Requirement> list, BmobException e) {
                if(e == null && list.size() > 0){
                    mData = new ArrayList<Requirement>();
                    for(int i = 0; i < list.size(); i++){

                        if(containsIgnoreCase(list.get(i).getTitle(),word)){
                            mData.add(list.get(i));
                        }
                    }
                }
                mAdapter = new RequirementAdapter(mData);
                mAdapter.setEmptyView(View.inflate(mContext, R.layout.empty_view,null));
                mRecyclerView.setAdapter(mAdapter);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean containsIgnoreCase( String haystack, String needle ){
        Pattern p = Pattern.compile(needle,Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
        Matcher m = p.matcher(haystack);
        return m.find();
    }
}
