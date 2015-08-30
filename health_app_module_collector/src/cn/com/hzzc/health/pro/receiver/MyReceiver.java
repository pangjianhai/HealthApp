package cn.com.hzzc.health.pro.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.com.hzzc.health.pro.InfoDetailActivity;
import cn.com.hzzc.health.pro.MainActivity;
import cn.com.hzzc.health.pro.MainPageLayoutSpaceActivity;
import cn.com.hzzc.health.pro.MineSpaceActivity;
import cn.com.hzzc.health.pro.ShareSentenceAllDetailActivity;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.config.PushTypeConst;
import cn.com.hzzc.health.pro.util.ExampleUtil;
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
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {// 接收自定义消息

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {// 获取消息通知的时候进入此分支
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {// 打开消息的时候进入此分支
			System.out.println("============================通知打开");
			String str = bundle.getString(EXTRAS_KEY);
			processCommonNotice(context, bundle.getString(EXTRAS_KEY));

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
		} else {
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

	private void processCommonNotice(Context context, String jsonExtra) {
		try {
			JSONObject j = new JSONObject(jsonExtra);
			String type = j.getString("type");
			String content = j.getString("content");
			JSONObject contentJ = new JSONObject(content);
			if (type.equals(PushTypeConst.comment_to_share)) {
				toSharePage(context, contentJ);
			} else if (type.equals(PushTypeConst.reply_to_comment)) {

			} else if (type.equals(PushTypeConst.focused_by_someone)) {
				toUserPage(context, contentJ);
			} else if (type.equals(PushTypeConst.share_introduced_to_3part)) {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param context
	 * @param j
	 * @user:pang
	 * @data:2015年8月30日
	 * @todo:查看信息详情
	 * @return:void
	 */
	private void toSharePage(Context context, JSONObject j) {
		try {
			String shareId = j.getString("shareId");
			Intent intent = new Intent(context,
					ShareSentenceAllDetailActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("share_sentence_id", shareId);
			context.startActivity(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param context
	 * @param j
	 * @user:pang
	 * @data:2015年8月30日
	 * @todo:查看个人详情
	 * @return:void
	 */
	private void toUserPage(Context context, JSONObject j) {

		try {
			String userId = j.getString("friendId");
			Intent intent = new Intent(context, ShowUserInfoDetail.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("uuid", userId);
			context.startActivity(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
