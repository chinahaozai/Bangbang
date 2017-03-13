package halewang.com.bangbang;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import halewang.com.bangbang.presenter.SearchPresenter;
import halewang.com.bangbang.view.SearchRequirementView;

public class SearchActivity extends BaseActivity<SearchRequirementView,SearchPresenter> implements SearchRequirementView{

    private LinearLayout llBack;
    private EditText etSearch;
    private TextView tvSearch;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ImageView ivDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        mPresenter.onCreate();
    }

    private void initView(){
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                ivDelete.setVisibility(View.GONE);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivDelete.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected SearchPresenter initPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public EditText getEdtiText() {
        return etSearch;
    }

    @Override
    public TextView getTextView() {
        return tvSearch;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
