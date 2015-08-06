package cn.com.hzzc.health.pro;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * 
 * @author pang
 * @todo �ҵķ���һ��
 *
 */
public class ShareMineActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout tBottomLay, mBottomLay, mContentLay;

	private boolean flag = false;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_mine_pages);
		init();
	}

	private void init() {
		mContentLay = (RelativeLayout) findViewById(R.id.content_layout);
		tBottomLay = (RelativeLayout) findViewById(R.id.top_layout);
		mBottomLay = (RelativeLayout) findViewById(R.id.bottom_layout);
		mContentLay.setOnClickListener(this);
		mBottomLay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// �������¼�������ģ�⣬ʵ��Ч�����ʵ���Ϲ�(����)���أ�������ʾ
		case R.id.content_layout:
			if (!flag) {
				Animation animation = AnimationUtils.loadAnimation(this,
						R.anim.slide_out_to_bottom);
				Animation t_animation = AnimationUtils.loadAnimation(this,
						R.anim.slide_out_to_top);
				mBottomLay.startAnimation(animation);
				tBottomLay.startAnimation(t_animation);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mBottomLay.setVisibility(View.GONE);
						tBottomLay.setVisibility(View.GONE);
					}
				}, 500);
				flag = true;
			} else {
				Animation animation = AnimationUtils.loadAnimation(this,
						R.anim.slide_in_from_bottom);
				Animation t_animation = AnimationUtils.loadAnimation(this,
						R.anim.slide_in_from_top);
				mBottomLay.startAnimation(animation);
				tBottomLay.startAnimation(t_animation);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						mBottomLay.setVisibility(View.VISIBLE);
						tBottomLay.setVisibility(View.VISIBLE);
					}
				}, 500);
				flag = false;
			}
			break;
		case R.id.bottom_layout:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
