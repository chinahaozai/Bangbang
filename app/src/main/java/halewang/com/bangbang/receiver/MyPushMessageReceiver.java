package halewang.com.bangbang.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.bmob.push.PushConstants;

/**
 * Created by halewang on 2017/2/27.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {

    private static final String TAG = "MyPushMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d(TAG, "客户端收到推送内容："+intent.getStringExtra("msg"));
            Toast.makeText(context,intent.getStringExtra("msg"),Toast.LENGTH_SHORT).show();
        }
    }

}