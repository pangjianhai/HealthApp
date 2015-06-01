package cn.com.health.pro;

import android.os.Bundle;
import android.view.Window;

public class TestActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_pop_window);

	}
}
