package halewang.com.bangbang.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.helper.NotificationCompat;
import halewang.com.bangbang.MainActivity;
import halewang.com.bangbang.NotificationActivity;
import halewang.com.bangbang.R;

/**
 * Created by halewang on 2017/2/27.
 */

public class MyPushMessageReceiver extends BroadcastReceiver {

    private static final String TAG = "MyPushMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String message = intent.getStringExtra("msg");
        String objectId = null;    //认领人手机号
        try {
            JSONObject object = new JSONObject(message);
            objectId = object.get("alert").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "客户端收到推送内容：" + message);
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("objectId",objectId);
        i.putExtra("detail", bundle);
        Log.d(TAG, "客户端收到objectId：" + objectId);
        i.setClass(context, NotificationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context)
                .setTicker("帮帮的推送")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("您发布的任务被认领")
                .setContentText("你的需求已经被认领了，点击查看是谁认领了你的需求")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE)
                .setContentIntent(pi);
        // 发送通知
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = mBuilder.build();
        nm.notify(0, n);
    }

}