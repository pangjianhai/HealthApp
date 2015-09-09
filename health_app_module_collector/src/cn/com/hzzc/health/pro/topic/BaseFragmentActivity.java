package cn.com.hzzc.health.pro.topic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import cn.com.hzzc.health.pro.AppLoginStartActivity;
import cn.com.hzzc.health.pro.AppRegActivity;
import cn.com.hzzc.health.pro.MainPageLayoutMeActivity;
import cn.com.hzzc.health.pro.MainPageLayoutOrderActivity;
import cn.com.hzzc.health.pro.MainPageLayoutSpaceActivity;
import cn.com.hzzc.health.pro.MainPageLayoutTagActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SharePrepareActivity;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.util.ActivityCollector;

public class BaseFragmentActivity extends FragmentActivity {

	/**
	 * 用户ID
	 */
	public String userId;

	/**
	 * 底部操作栏
	 */
	View bottom;

	public Button main_page_layout_home_btn, main_page_layout_info_btn,
			main_page_layout_share_btn, main_page_layout_order_btn,
			main_page_layout_me_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 获取用户ID
		 */
		userId = HealthApplication.getUserId();
		/**
		 * activity放入收集器
		 */
		ActivityCollector.addActivity(this);
		bottom = getLayoutInflater().inflate(
				R.layout.main_page_layout_common_bottom, null);
		ActivityCollector.addActivity(this);
		initBottom();
	}

	/**
	 * @tags
	 * @date 2015年5月15日
	 * @todo 初始化底部按钮
	 * @author pang
	 */
	public void initBottom() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		String className = info.topActivity.getClassName(); // 完整类名
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

		Drawable topDrawable = getResources().getDrawable(
				R.drawable.tabbar_home_highlighted);
		topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
				topDrawable.getMinimumHeight());
		main_page_layout_home_btn.setCompoundDrawables(null, null, null,
				topDrawable);

	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月15日
	 * @todo 底部按钮点击事件
	 * @author pang
	 */
	public void share_btn(View v) {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String curent_activity_name = cn.getClassName();
		/**
		 * 如果用户没有登录，则提醒用户可以登录或者注册
		 */
		if (!HealthApplication.isLogin()) {
			no_login_alter(v);
			return;
		}
		/**
		 * 如果点击的是分享按钮
		 */
		if (v.getId() == R.id.main_page_layout_share_btn) {// 进入分享页面
			Intent intent = new Intent(this, SharePrepareActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.main_page_layout_me_btn) {// 查看关于我
			if (!curent_activity_name.equals(MainPageLayoutMeActivity.class
					.getName())) {
				Intent intent = new Intent(this, MainPageLayoutMeActivity.class);
				startActivity(intent);
			}
		} else if (v.getId() == R.id.main_page_layout_home_btn) {// 查看首页
			if (!curent_activity_name.equals(MainPageLayoutSpaceActivity.class
					.getName())) {
				Intent intent = new Intent(this, HomeFrameActivity.class);
				startActivity(intent);
			}
		} else if (v.getId() == R.id.main_page_layout_info_btn) {// 查看标签
			if (!curent_activity_name.equals(MainPageLayoutTagActivity.class
					.getName())) {
				Intent intent = new Intent(this,
						MainPageLayoutTagActivity.class);
				startActivity(intent);
			}
		} else if (v.getId() == R.id.main_page_layout_order_btn) {// 查看排行
			if (!curent_activity_name.equals(MainPageLayoutOrderActivity.class
					.getName())) {
				Intent intent = new Intent(this,
						MainPageLayoutOrderActivity.class);
				startActivity(intent);
			}
		}
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月24日
	 * @todo:返回首页
	 * @return:void
	 */
	public void to_home_page() {
		/***
		 * 如果放开注释则会进入首页的同时将排行榜重新刷新
		 */
		// Intent intent = new Intent(getApplicationContext(),
		// MainPageLayoutSpaceActivity.class);
		// startActivity(intent);
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年7月20日
	 * @todo:右上角弹出框
	 * @return:void
	 */
	public void showPopWin(View v) {
		if (HealthApplication.isLogin()) {

		} else {
			no_login_alter(v);
		}
	}

	public void initNoLoginOption(View v) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.share_space_window, null);

		PopupWindow window = new PopupWindow(view,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		// window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAsDropDown(v);
		/**
		 * popwindow按钮地方法
		 */
		Button space_win_login = (Button) view
				.findViewById(R.id.space_win_login);
		space_win_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						AppLoginStartActivity.class);
				startActivity(intent);
			}

		});
		Button space_win_reg = (Button) view.findViewById(R.id.space_win_reg);
		space_win_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AppRegActivity.class);
				startActivity(intent);
			}

		});
		/**
		 * 让popwindow消失
		 */
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});
	}

	/**
	 * 
	 * @return
	 * @user:pang
	 * @data:2015年7月20日
	 * @todo:是否登录使用APP
	 * @return:boolean
	 */
	public boolean isLogin() {
		if (userId == null || "".equals(userId)) {
			return false;
		}
		return true;
	}

	/**************************************** 关于非登录用户需要提示的popwindow ********************************************/

	public void no_login_alter(View v) {
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View noLoginAlter = inflater.inflate(R.layout.app_nologin_alter,
				null, false);
		final PopupWindow popWindow = new PopupWindow(noLoginAlter, 400,
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		ImageView close_nologin_alert_image = (ImageView) noLoginAlter
				.findViewById(R.id.close_nologin_alert_image);
		close_nologin_alert_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}
		});

		Button alert_login = (Button) noLoginAlter
				.findViewById(R.id.alert_login);
		alert_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						AppLoginStartActivity.class);
				startActivity(intent);
			}

		});
		Button alert_reg = (Button) noLoginAlter.findViewById(R.id.alert_reg);
		alert_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AppRegActivity.class);
				startActivity(intent);
			}

		});
		popWindow.setFocusable(true);
		backgroundAlpha(0.7f);
		popWindow.setOnDismissListener(new PoponDismissListener());
		popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	class PoponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

}
