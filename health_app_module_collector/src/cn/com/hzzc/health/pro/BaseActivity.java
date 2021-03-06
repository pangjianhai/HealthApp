package cn.com.hzzc.health.pro;

import java.io.File;
import java.util.Iterator;
import java.util.List;
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
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.util.ActivityCollector;
import cn.com.hzzc.health.pro.util.ExampleUtil;
import cn.com.hzzc.health.pro.util.LoginUtil;
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
		LoginUtil.no_login_alter(v, this);
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
		// Toast.makeText(this, "【测试代码】刚进行了http请求", Toast.LENGTH_SHORT).show();
	}

	public void send_normal_request_for_file(String url, Map<String, String> p,
			List<File> files, RequestCallBack<?> rcb) {
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
		if (files != null && !files.isEmpty()) {
			for (File f : files) {
				params.addBodyParameter(f.getAbsolutePath(), f);
			}
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, rcb);
	}

	/**
	 * ****************************JPush
	 */

	/**
	 * @user:pang
	 * @data:2015年8月30日
	 * @todo:设置alias，现在是在登录成功之后设置，其实最终应该是在注册成功之后设置
	 * @return:void
	 */
	public void initAlis() {
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, userId));
	}

	/**
	 * @param tagIds
	 * @user:pang
	 * @data:2015年8月30日
	 * @todo:设置系统分析的标签（集合里面应该是标签的ID），应该在每一次系统给自己自定义标签的时候调用
	 * @return:void
	 */
	public void initShareTags(Set<String> tagIds) {
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagIds));
	}

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:// 设置alias
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
