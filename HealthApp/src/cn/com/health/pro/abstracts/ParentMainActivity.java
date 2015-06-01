package cn.com.health.pro.abstracts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cn.com.health.pro.BaseActivity;
import cn.com.health.pro.MainPageLayoutInfoActivity;
import cn.com.health.pro.MainPageLayoutMeActivity;
import cn.com.health.pro.MainPageLayoutSpaceActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.SharePrepareActivity;
import cn.com.health.pro.persist.SharedPreInto;

/**
 * 
 * @author pang
 * @todo 登陆后主页面的父类
 *
 */
public abstract class ParentMainActivity extends BaseActivity {
	/**
	 * 底部操作栏
	 */
	View bottom;

	private Button main_page_layout_home_btn, main_page_layout_info_btn,
			main_page_layout_share_btn, main_page_layout_order_btn,
			main_page_layout_me_btn;

	/**
	 * 用户ID
	 */
	public String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userId = new SharedPreInto(ParentMainActivity.this)
				.getSharedFieldValue("id");
		bottom = getLayoutInflater().inflate(
				R.layout.main_page_layout_common_bottom, null);
		initBottom();
	}

	/**
	 * @tags
	 * @date 2015年5月15日
	 * @todo 初始化底部按钮
	 * @author pang
	 */
	public void initBottom() {
		main_page_layout_home_btn = (Button) bottom
				.findViewById(R.id.main_page_layout_home_btn);
		main_page_layout_info_btn = (Button) bottom
				.findViewById(R.id.main_page_layout_info_btn);
		main_page_layout_share_btn = (Button) bottom
				.findViewById(R.id.main_page_layout_share_btn);
		main_page_layout_order_btn = (Button) bottom
				.findViewById(R.id.main_page_layout_order_btn);
		main_page_layout_me_btn = (Button) bottom
				.findViewById(R.id.main_page_layout_me_btn);
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月15日
	 * @todo 底部按钮点击事件
	 * @author pang
	 */
	public void share_btn(View v) {
		/**
		 * 如果点击的是分享按钮
		 */
		if (v.getId() == R.id.main_page_layout_share_btn) {
			Intent intent = new Intent(ParentMainActivity.this,
					SharePrepareActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.main_page_layout_me_btn) {
			Intent intent = new Intent(ParentMainActivity.this,
					MainPageLayoutMeActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.main_page_layout_home_btn) {
			Intent intent = new Intent(ParentMainActivity.this,
					MainPageLayoutSpaceActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.main_page_layout_info_btn) {
			Intent intent = new Intent(ParentMainActivity.this,
					MainPageLayoutInfoActivity.class);
			startActivity(intent);
		}
		finish();
	}

}