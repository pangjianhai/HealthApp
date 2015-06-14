package cn.com.health.pro.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.com.health.pro.InfoDetailActivity;
import cn.com.health.pro.MainActivity;
import cn.com.health.pro.util.ExampleUtil;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	private static final String EXTRAS_KEY = "cn.jpush.android.EXTRA";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		System.out.println("[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {

			String str = bundle.getString(EXTRAS_KEY);
			try {
				JSONObject or_obj = new JSONObject(str);
				String zcId = or_obj.getString("zcId");
				String zcTitle = or_obj.getString("zcTitle");
				Intent it = new Intent(context, InfoDetailActivity.class);
				it.putExtra("doc_id", zcId);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(it);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
		} else {
			System.out.println("[MyReceiver] Unhandled intent - "
					+ intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		
	}
}
