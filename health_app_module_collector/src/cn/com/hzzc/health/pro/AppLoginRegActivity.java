package cn.com.hzzc.health.pro;

import cn.com.hzzc.health.pro.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * 
 * @author pang
 * @todo 注册或者登陆选择去向activity
 *
 */
public class AppLoginRegActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_login_or_reg);
	}

	public void welcome_login(View v) {
		Intent intent = new Intent();
		intent.setClass(AppLoginRegActivity.this, AppLoginStartActivity.class);
		startActivity(intent);
	}

	public void welcome_register(View v) {
		Intent intent = new Intent();
		intent.setClass(AppLoginRegActivity.this, AppRegActivity.class);
		startActivity(intent);
	}
}
