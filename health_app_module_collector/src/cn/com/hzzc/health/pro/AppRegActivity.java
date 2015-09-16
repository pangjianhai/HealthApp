package cn.com.hzzc.health.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

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

		}
	}
}
