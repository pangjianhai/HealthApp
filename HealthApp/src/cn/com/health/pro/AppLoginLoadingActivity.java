package cn.com.health.pro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

/**
 * 
 * @author pang
 * @todo 登陆成功
 *
 */
public class AppLoginLoadingActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_login_loading);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(AppLoginLoadingActivity.this,
						MainPageLayoutSpaceActivity.class);
				startActivity(intent);
				AppLoginLoadingActivity.this.finish();
				Toast.makeText(getApplicationContext(), "登录成功",
						Toast.LENGTH_SHORT).show();
			}
		}, 200);
	}

}
