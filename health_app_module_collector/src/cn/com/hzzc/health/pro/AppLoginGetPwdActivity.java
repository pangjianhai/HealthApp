package cn.com.hzzc.health.pro;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.com.hzzc.health.pro.part.LineEditText;
import cn.com.hzzc.health.pro.util.LoginUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 找回密码
 * @author pang
 *
 */
public class AppLoginGetPwdActivity extends BaseActivity {

	private LineEditText login_user_edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_login_get_pwd);
		init();
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:初始化组件
	 * @return:void
	 */
	private void init() {
		login_user_edit = (LineEditText) findViewById(R.id.login_user_edit);
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:找回密码
	 * @return:void
	 */
	public void getPwd(View v) {
		String account = login_user_edit.getText().toString();
		if (account == null || "".equals(account)) {
			new AlertDialog.Builder(AppLoginGetPwdActivity.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("找回错误").setMessage("请先输入账号！").create().show();
			return;
		} else {
			getPwd(account);
		}
	}

	/**
	 * 
	 * @param account
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:真正找回密码
	 * @return:void
	 */
	private void getPwd(String account) {
		try {
			JSONObject j = new JSONObject();
			j.put("userId", account);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String r = responseInfo.result;
					String data = LoginUtil.parseEmailResult(r);
					System.out.println("data:" + data);
					if ("notexist".equals(data)) {
						account_msg(0);
					} else if ("success".equals(data)) {
						account_msg(1);
						// to_login();// 进入登陆页面
					} else if ("fail".equals(data)) {
						account_msg(2);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					System.out.println(error + "---" + msg);
				}
			};
			Map map = new HashMap();
			map.put("para", j.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.login_get_pwd_by_email, map, rcb);
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @param type
	 *            0：不能存在 1：邮件发送成功 2：邮件发送失败
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:账号不存在
	 * @return:void
	 */
	private void account_msg(int type) {
		String title = "找回错误";
		String content = "";
		if (type == 0) {
			content = "该账号不存在！";
		} else if (type == 1) {
			title = "找回密码";
			content = "密码发至您的邮箱  \n 找回成功，请登录！";
		} else if (type == 2) {
			content = "找回失败！";
		}
		new AlertDialog.Builder(AppLoginGetPwdActivity.this)
				.setIcon(
						getResources().getDrawable(
								R.drawable.tabbar_message_center_highlighted))
				.setTitle(title).setMessage(content).create().show();
	}

	/**
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:进入登陆页面
	 * @return:void
	 */
	public void to_login() {
		Intent intent = new Intent();
		intent.setClass(AppLoginGetPwdActivity.this,
				AppLoginStartActivity.class);
		startActivity(intent);
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:返回登陆页面
	 * @return:void
	 */
	public void login_back(View v) {
		this.finish();
	}
}
