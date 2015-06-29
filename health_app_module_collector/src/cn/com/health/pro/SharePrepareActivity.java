package cn.com.health.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * 
 * @todo 提供可以分享的信息的类型
 * @author pang
 *
 */
public class SharePrepareActivity extends BaseActivity implements
		OnClickListener {

	/**
	 * 所有按钮
	 */
	private Button share_prepare_eating_btn, share_prepare_go_btn,
			share_prepare_info_btn, share_prepare_back_btn;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_send_prepare);
		init();
	}

	private void init() {
		share_prepare_eating_btn = (Button) findViewById(R.id.share_prepare_eating);
		share_prepare_go_btn = (Button) findViewById(R.id.share_prepare_go);
		share_prepare_info_btn = (Button) findViewById(R.id.share_prepare_info);
		share_prepare_back_btn = (Button) findViewById(R.id.share_prepare_back);

		share_prepare_eating_btn.setOnClickListener(this);
		share_prepare_go_btn.setOnClickListener(this);
		share_prepare_info_btn.setOnClickListener(this);
		share_prepare_back_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_prepare_go:
			Intent sport_intent = new Intent(SharePrepareActivity.this,
					ShareSportsActivity.class);
			startActivity(sport_intent);
			break;
		case R.id.share_prepare_eating:
			Intent eat_intent = new Intent(SharePrepareActivity.this,
					ShareEatActivity.class);
			startActivity(eat_intent);
			break;
		case R.id.share_prepare_info:
			Intent intent = new Intent(SharePrepareActivity.this,
					ShareHealthActivity.class);
			startActivity(intent);
			break;
		case R.id.share_prepare_back:
			Intent i = new Intent(SharePrepareActivity.this,
					MainPageLayoutSpaceActivity.class);
			startActivity(i);
			finish();
			break;
		default:
			break;
		}
	}

}
