package halewang.com.bangbang.adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import halewang.com.bangbang.R;
import halewang.com.bangbang.model.Requirement;

/**
 * Created by halewang on 2017/3/9.
 */

public class RequirementAdapter extends BaseQuickAdapter<Requirement, BaseViewHolder>{
    public RequirementAdapter(List<Requirement> data) {
        super(R.layout.item_requirement,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Requirement item) {
        helper.addOnClickListener(R.id.requirement_item)
                .setText(R.id.tv_title,item.getTitle())
                .setText(R.id.tv_content,item.getContent())
                .setText(R.id.tv_money,item.getMoney()+"¥")
                .setText(R.id.tv_time,item.getTime())
                .setText(R.id.tv_watch_count,String.valueOf(item.getWatchCount()));
        if(!item.getReceiverPhone().equals("")){
             helper.setVisible(R.id.iv_status,true);
        }else{
            helper.setVisible(R.id.iv_status,false);
        }

    }
}
