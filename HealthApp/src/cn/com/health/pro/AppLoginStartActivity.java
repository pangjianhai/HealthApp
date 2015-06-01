package cn.com.health.pro;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import cn.com.health.pro.persist.SharedPreInto;
import cn.com.health.pro.task.LoginAccountTask;

/**
 * @author pang
 * @todo 登陆框
 *
 */
public class AppLoginStartActivity extends BaseActivity {

	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框

	/**
	 * 保存到手机本地的账号信息
	 */
	private String saveUUID, saveID, savePWD;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_login);
		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);

	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月11日
	 * @todo 开始验证登陆
	 * @author pang
	 */
	public void loginHealthApp(View v) {
		// loginOk();
		try {
			String UserId = mUser.getText().toString();
			String pdw = mPassword.getText().toString();
			/**
			 * 如果用户名密码为空则不能登录
			 */
			if (UserId == null || "".equals(UserId) || pdw == ""
					|| "".equals(pdw)) {
				loginFail();
				return;
			}
			JSONObject j = new JSONObject();
			j.put("UserId", UserId);
			j.put("Password", pdw);
			new LoginAccountTask(AppLoginStartActivity.this).execute(j
					.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @tags @param result
	 * @date 2015年5月11日
	 * @todo 登陆请求结束
	 * @author pang
	 */
	public void loginOver(String result) {
		try {
			JSONObject data = new JSONObject(result);
			String uuid = data.get("flag") + "";
			// 登陆成功
			if (uuid != null && !"".equals(uuid) && !"fail".equals(uuid)) {
				saveUUID = uuid;
				saveID = mUser.getText().toString();
				savePWD = mPassword.getText().toString();
				loginOk();
			} else {// 登陆失败
				loginFail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月11日
	 * @todo 登录成功
	 * @author pang
	 */
	private void loginOk() {
		/**
		 * 登陆成功保存本地
		 */
		new SharedPreInto(AppLoginStartActivity.this).initAccountAfterReg(
				saveUUID, saveID, savePWD);
		Intent intent = new Intent();
		intent.setClass(AppLoginStartActivity.this,
				AppLoginLoadingActivity.class);
		startActivity(intent);
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月11日
	 * @todo 登录失败
	 * @author pang
	 */
	private void loginFail() {
		new AlertDialog.Builder(AppLoginStartActivity.this)
				.setIcon(
						getResources().getDrawable(R.drawable.login_error_icon))
				.setTitle("登录错误").setMessage("输入有误，\n请重新输入后再登录！").create()
				.show();
	}

	public void login_back(View v) { // 标题栏 返回按钮
		this.finish();
	}
}
