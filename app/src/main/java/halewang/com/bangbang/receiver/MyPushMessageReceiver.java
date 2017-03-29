package halewang.com.bangbang.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.helper.NotificationCompat;
import halewang.com.bangbang.MainActivity;
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
        String alert = null;
        try {
            JSONObject object = new JSONObject(message);
            alert = object.get("alert").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "客户端收到推送内容：" + message);
        Intent i = new Intent();
        i.setClass(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context)
                .setTicker("帮帮的推送")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("您发布的任务被认领")
                .setContentText(alert)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE)
                .setContentIntent(pi);
        // 发送通知
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = mBuilder.build();
        nm.notify(0, n);
    }

}