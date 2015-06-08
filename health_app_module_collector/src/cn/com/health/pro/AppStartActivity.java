package cn.com.health.pro;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import cn.com.health.pro.persist.SharedPreInto;
import cn.com.health.pro.task.SkipLoginAccountTask;

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
			try {
				JSONObject j = new JSONObject();
				j.put("UserId", UserId);
				j.put("Password", pwd);
				new SkipLoginAccountTask(AppStartActivity.this).execute(j
						.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			skipToLogin();
		}

	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月12日
	 * @todo 验证结束
	 * @author pang
	 */
	public void skipOver(String result) {
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
	 * @todo 跳转到登陆界面
	 * @author pang
	 */
	private void skipToLogin() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(AppStartActivity.this,
						AppLoginRegActivity.class);
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
	}

}