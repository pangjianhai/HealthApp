package cn.com.hzzc.health.pro;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import cn.com.hzzc.health.pro.persist.SharedPreInto;
import cn.com.hzzc.health.pro.topic.HomeAllShowActivity;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 启动APP程序
 *
 */
public class AppStartActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.app_start);

		/**
		 * 获取上一次登陆过的用户名和密码，尝试自动登陆
		 */
		String UserId = new SharedPreInto(AppStartActivity.this)
				.getSharedFieldValue("name");
		String pwd = new SharedPreInto(AppStartActivity.this)
				.getSharedFieldValue("password");
		/**
		 * 已经登陆过用户名密码了先进行验证，如果通过直接登陆成功
		 */
		if (UserId != null && !"".equals(UserId) && pwd != null
				&& !"".equals(pwd)) {
			attempToLoginAuto(UserId, pwd);
		} else {
			skipToLogin();
		}

	}

	/**
	 * 
	 * @param UserId
	 * @param pwd
	 * @user:pang
	 * @data:2015年6月19日
	 * @todo:尝试自动登陆
	 * @return:void
	 */
	private void attempToLoginAuto(String UserId, String pwd) {
		try {
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				/**
				 * 请求成功，进行处理
				 */
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					afterAutoLogin(responseInfo.result);
				}

				/**
				 * 请求失败，直接登录到填写页面
				 */
				@Override
				public void onFailure(HttpException error, String msg) {
					skipToLogin();
				}
			};
			JSONObject j = new JSONObject();
			j.put("UserId", UserId);
			j.put("Password", pwd);
			Map map = new HashMap();
			map.put("para", j.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.userLogin, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月12日
	 * @todo 验证结束
	 * @author pang
	 */
	public void afterAutoLogin(String result) {
		try {
			if (result != null && !"".equals(result)) {
				JSONObject data = new JSONObject(result);
				String uuid = data.get("flag") + "";
				// 登陆成功
				if (uuid != null && !"".equals(uuid) && !"fail".equals(uuid)) {
					skipSuccess(uuid);
				} else {// 验证失败，需要输入账号信息登陆
					skipToLogin();
					// 重置已经保存到账号信息（因为可能因为用户已经修改账号信息，所以清空之前的账号信息）
					new SharedPreInto(AppStartActivity.this)
							.initAccountAfterReg("", "", "");
				}
			} else {
				skipToLogin();
				new SharedPreInto(AppStartActivity.this).initAccountAfterReg(
						"", "", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月12日
	 * @todo 跳转到登陆界面（后台可以匿名登录，改为了主页面）
	 * @author pang
	 */
	private void skipToLogin() {
		// System.out.println("skipToLogin");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(AppStartActivity.this,
						HomeAllShowActivity.class);
				startActivity(intent);
				AppStartActivity.this.finish();
			}
		}, 1000);
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月12日
	 * @todo 自动登陆成功
	 * @author pang
	 */
	private void skipSuccess(String id) {
		String UserId = new SharedPreInto(AppStartActivity.this)
				.getSharedFieldValue("name");
		String pwd = new SharedPreInto(AppStartActivity.this)
				.getSharedFieldValue("password");
		new SharedPreInto(AppStartActivity.this).initAccountAfterReg(id,
				UserId, pwd);
		Intent intent = new Intent();
		intent.setClass(AppStartActivity.this, AppLoginLoadingActivity.class);
		startActivity(intent);
		/**
		 * 必须有这一行代码，否则回退的时候有时候会出问题
		 */
		finish();
	}

}
