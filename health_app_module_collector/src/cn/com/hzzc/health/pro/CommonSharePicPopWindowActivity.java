package cn.com.hzzc.health.pro;

import cn.com.hzzc.health.pro.R;
import android.os.Bundle;
import android.view.Window;

public class CommonSharePicPopWindowActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.common_share_pic_pop_window);
	}
}
