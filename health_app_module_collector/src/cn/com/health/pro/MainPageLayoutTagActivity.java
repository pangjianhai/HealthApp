package cn.com.health.pro;

import android.os.Bundle;
import android.view.Window;
import cn.com.health.pro.abstracts.ParentMainActivity;

/**
 * 
 * @author pang
 * @todo 首页我的排行
 *
 */
public class MainPageLayoutTagActivity extends ParentMainActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main_page_layout_tag);

	}

}
