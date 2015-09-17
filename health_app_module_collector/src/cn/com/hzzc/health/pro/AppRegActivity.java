package cn.com.hzzc.health.pro;

import java.util.HashMap;
import java.util.Random;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * @author pang
 * @todo 注册activity
 *
 */
public class AppRegActivity extends BaseActivity implements OnClickListener {
	private Button app_email_reg, app_iphone_reg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.app_reg);
		init();

	}

	private void init() {
		app_email_reg = (Button) findViewById(R.id.app_email_reg);
		app_iphone_reg = (Button) findViewById(R.id.app_iphone_reg);
		app_email_reg.setOnClickListener(this);
		app_iphone_reg.setOnClickListener(this);

	}

	public void goback(View v) {
		this.finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.app_email_reg) {
			Intent it = new Intent(AppRegActivity.this,
					AppEmailRegActivity.class);
			startActivity(it);
		} else {
			RegisterPage registerPage = new RegisterPage();
			registerPage.setRegisterCallback(new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					System.out.println("result:" + result);
					// 解析注册结果
					if (result == SMSSDK.RESULT_COMPLETE) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
						String country = (String) phoneMap.get("country");
						String phone = (String) phoneMap.get("phone");
						// 提交用户信息
						registerUser(country, phone);
					} else if (result == SMSSDK.RESULT_ERROR) {
						try {
							Throwable throwable = (Throwable) data;
							throwable.printStackTrace();
							JSONObject object = new JSONObject(throwable
									.getMessage());
							String des = object.optString("detail");// 错误描述
							int status = object.optInt("status");// 错误代码
							System.out.println("status:" + status);

						} catch (Exception e) {
							// do something
						}
					}
				}
			});
			registerPage.show(this);
		}
	}

	// 提交用户信息
	//country:86-----phone:18001179714
	private void registerUser(String country, String phone) {
		System.out.println("country:" + country + "-----phone:" + phone);
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		String nickName = "SmsSDK_User_" + uid;
		SMSSDK.submitUserInfo(uid, "默认", null, country, phone);
	}
}
