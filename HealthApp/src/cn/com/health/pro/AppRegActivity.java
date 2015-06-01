package cn.com.health.pro;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import cn.com.health.pro.persist.SharedPreInto;
import cn.com.health.pro.task.RegisterAccountTask;

/**
 * @author pang
 * @todo 注册activity
 *
 */
public class AppRegActivity extends BaseActivity {
	/**
	 * 注册选项
	 */
	private EditText reg_username, reg_password, reg_password_again,
			reg_password_email;

	String str_reg_username;
	String str_reg_password;
	String str_reg_password_again;
	String str_reg_password_email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.app_reg);
		init();

	}

	private void init() {
		reg_username = (EditText) findViewById(R.id.reg_username);
		reg_password = (EditText) findViewById(R.id.reg_password);
		reg_password_again = (EditText) findViewById(R.id.reg_password_again);
		reg_password_email = (EditText) findViewById(R.id.reg_password_email);

	}

	private void error(String content) {
		new AlertDialog.Builder(AppRegActivity.this)
				.setIcon(
						getResources().getDrawable(R.drawable.login_error_icon))
				.setTitle("注册错误").setMessage(content).create().show();
	}

	private boolean validate() {
		Drawable dr = getResources().getDrawable(R.drawable.input_error);
		dr.setBounds(0, 0, 35, 35); // 必须设置大小，否则不显示
		str_reg_username = reg_username.getText().toString();
		str_reg_password = reg_password.getText().toString();
		str_reg_password_again = reg_password_again.getText().toString();
		str_reg_password_email = reg_password_email.getText().toString();
		if (str_reg_username == null || "".equals(str_reg_username.trim())) {
			reg_username.setError("用户名不能为空", dr);
			reg_username.setCursorVisible(false);
			reg_username.setSelectAllOnFocus(true);
			reg_username.requestFocus(); // 请求获取焦点
			reg_username.clearFocus(); // 清除焦点
			return false;
		}
		if (str_reg_password == null || "".equals(str_reg_password.trim())) {
			reg_password.setError("密码不能为空", dr);
			return false;
		}
		if (str_reg_password_again == null
				|| "".equals(str_reg_password_again.trim())) {
			reg_password_again.setError("密码不能为空", dr);
			return false;
		}
		if (str_reg_password_email == null
				|| "".equals(str_reg_password_email.trim())) {
			reg_password_email.setError("邮箱不能为空", dr);
			return false;
		}
		if (!str_reg_password.equals(str_reg_password_again)) {
			reg_password_again.setError("密码不能为空", dr);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月11日
	 * @todo 注册账号
	 * @author pang
	 */
	public void getAccount(View v) {
		if (validate()) {
			try {
				JSONObject j = new JSONObject();
				j.put("userid", str_reg_username);
				j.put("password", str_reg_password);
				j.put("email", str_reg_password_email);
				String url = SystemConst.server_url
						+ SystemConst.FunctionUrl.addUser;
				Map<String, String> map = new HashMap<String, String>();
				map.put("para", j.toString());
				new RegisterAccountTask(AppRegActivity.this).execute(j
						.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @tags @param result
	 * @date 2015年5月11日
	 * @todo 注册成功
	 * @author pang
	 */
	public void regOver(String result) {
		try {
			JSONObject data = new JSONObject(result);
			/**
			 * 解析出新的用户的ID
			 */
			String uuid = data.get("flag") + "";
			if (uuid == null || "".equals(uuid) || "fail".equals(uuid)) {
				regFail();
				return;
			}
			/**
			 * 保存到本地空间
			 */
			boolean is = new SharedPreInto(AppRegActivity.this)
					.initAccountAfterReg(userId, str_reg_username,
							str_reg_password);
			/**
			 * 保存失败
			 */
			if (!is) {
				regFail();
				return;
			}
			/**
			 * 注册成功，跳转到登陆页面
			 */
			Toast.makeText(AppRegActivity.this, "注册成功", Toast.LENGTH_SHORT)
					.show();
			regSuccess();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @tags
	 * @date 2015年5月11日
	 * @todo 注册成功
	 * @author pang
	 */
	private void regSuccess() {
		Intent intent = new Intent(AppRegActivity.this,
				AppLoginStartActivity.class);
		startActivity(intent);
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月11日
	 * @todo 注册失败
	 * @author pang
	 */
	private void regFail() {
		Toast.makeText(AppRegActivity.this, "注册失败，请重新提交哦", Toast.LENGTH_SHORT)
				.show();
	}

	public void goback(View v) {
		this.finish();
	}
}
