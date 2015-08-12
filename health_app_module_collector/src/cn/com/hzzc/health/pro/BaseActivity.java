package cn.com.hzzc.health.pro;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.util.ActivityCollector;
import cn.com.hzzc.health.pro.util.ExampleUtil;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 
 * @author pang
 * @todo 所有activity的父类
 *
 */
public class BaseActivity extends InstrumentedActivity {

	/**
	 * 用户ID
	 */
	public String userId;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		/**
		 * 获取用户ID
		 */
		userId = HealthApplication.getUserId();
		/**
		 * activity放入收集器
		 */
		ActivityCollector.addActivity(this);
		/**
		 * 设置极光通信
		 */
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 
	 * @tags @param contenxt
	 * @tags @return
	 * @date 2015年5月28日
	 * @todo 判断是否可以联网
	 * @author pang
	 */
	public boolean isNetWorkConnected() {
		ConnectivityManager cm = (ConnectivityManager) BaseActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 * @user:pang
	 * @data:2015年7月20日
	 * @todo:是否登录使用APP
	 * @return:boolean
	 */
	public boolean isLogin() {
		if (userId == null || "".equals(userId)) {
			return false;
		}
		return true;
	}

	/**************************************** 关于非登录用户需要提示的popwindow ********************************************/

	public void no_login_alter(View v) {
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View noLoginAlter = inflater.inflate(R.layout.app_nologin_alter,
				null, false);
		final PopupWindow popWindow = new PopupWindow(noLoginAlter, 400,
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		/**
		 * 子控件开始
		 */
		ImageView close_nologin_alert_image = (ImageView) noLoginAlter
				.findViewById(R.id.close_nologin_alert_image);
		close_nologin_alert_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}
		});

		Button alert_login = (Button) noLoginAlter
				.findViewById(R.id.alert_login);
		alert_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						AppLoginStartActivity.class);
				startActivity(intent);
			}

		});
		Button alert_reg = (Button) noLoginAlter.findViewById(R.id.alert_reg);
		alert_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AppRegActivity.class);
				startActivity(intent);
			}

		});
		/**
		 * 子控件结束
		 */
		// 点击空白处时，隐藏掉pop窗口
		popWindow.setFocusable(true);
		backgroundAlpha(0.7f);
		// 添加pop窗口关闭事件
		popWindow.setOnDismissListener(new PoponDismissListener());
		popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	class PoponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

	/**
	 * 
	 * @param url
	 *            地址
	 * @param p
	 *            参数
	 * @param rcb
	 *            回调函数
	 * @user:pang
	 * @data:2015年7月13日
	 * @todo:发送普通的POST http请求
	 * @return:void
	 */
	public void send_normal_request(String url, Map<String, String> p,
			RequestCallBack<?> rcb) {
		if (!isNetWorkConnected()) {
			Toast.makeText(getApplicationContext(), "没有网络咯！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params = new RequestParams();
		if (p != null) {
			Iterator<Map.Entry<String, String>> it = p.entrySet().iterator();
			/**
			 * 添加参数
			 */
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				params.addBodyParameter(entry.getKey(), entry.getValue());
			}
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, rcb);
		Toast.makeText(this, "【测试代码】刚进行了http请求", Toast.LENGTH_SHORT).show();
	}

	/**
	 * ****************************JPush
	 */

	public void initAlis() {
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, userId));
	}

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:// 设置alias
				System.out.println("设置alias");
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:// 设置tags
				JPushInterface.setAliasAndTags(getApplicationContext(), null,
						(Set<String>) msg.obj, mTagsCallback);
				break;

			default:
			}
		}
	};
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			System.out.println("code:" + code);
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
				} else {
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
			}

			// ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
				} else {
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
			}

			// ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

}
