package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.com.hzzc.health.pro.FirstLoginTopUserListLayout;
import cn.com.hzzc.health.pro.FriendSeachOpsActivity;
import cn.com.hzzc.health.pro.MainPageLayoutSpaceActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SharePrepareActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.TagsForUserActivity;
import cn.com.hzzc.health.pro.TagsForUserDefActivity;
import cn.com.hzzc.health.pro.config.GlobalUserVariable;
import cn.com.hzzc.health.pro.model.PushBean;
import cn.com.hzzc.health.pro.util.UserUtils;

public class HomeAllShowActivity extends ParentFragmentActivity implements
		OnCheckedChangeListener {

	public final static int num = 3;

	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	private RadioGroup radioGroup;

	Fragment homeFragment = new HomeMainPageFragment();
	Fragment sortFragment = new HomeMainTagFragment();
	Fragment orderFragment = new HomeMainOrderFragment();
	Fragment personFragment = new HomeMainMeFragment();

	// 三个选项卡
	private RadioButton radio0;
	private RadioButton radio1;
	private RadioButton radio2;
	private RadioButton radio3;

	//
	ImageButton home_ops_home, home_ops_tag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.home_all_show);
		fragmentManager = getSupportFragmentManager();
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radio0 = ((RadioButton) findViewById(R.id.radio0));
		radio1 = ((RadioButton) findViewById(R.id.radio1));
		radio2 = ((RadioButton) findViewById(R.id.radio2));
		radio3 = ((RadioButton) findViewById(R.id.radio3));

		radio0.setOnCheckedChangeListener(this);
		radio1.setOnCheckedChangeListener(this);
		radio2.setOnCheckedChangeListener(this);
		radio3.setOnCheckedChangeListener(this);

		home_ops_home = ((ImageButton) findViewById(R.id.home_ops_home));
		home_ops_tag = ((ImageButton) findViewById(R.id.home_ops_tag));

		radio0.performClick();// 此处设置默认第三个选项卡对应的fragment显示
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			// 用户当前浏览的选项卡
			int checkedWidgetId = buttonView.getId();
			radio0.setChecked(checkedWidgetId == R.id.radio0);
			radio1.setChecked(checkedWidgetId == R.id.radio1);
			radio2.setChecked(checkedWidgetId == R.id.radio2);
			radio3.setChecked(checkedWidgetId == R.id.radio3);
			showFragment(checkedWidgetId);
		} else {
			// 此处记录了用户上次浏览的选项卡
			String unCheckFragmentTag = getTagById(buttonView.getId());
			Fragment unCheckFragment = (Fragment) getSupportFragmentManager()
					.findFragmentByTag(unCheckFragmentTag);
			if (unCheckFragment != null) {
				// 隐藏上次显示到fragment,确保fragment不会重叠
				getSupportFragmentManager().beginTransaction()
						.hide(unCheckFragment).commit();
			}
		}
	}

	/**
	 * 显示对应的fragment
	 * 
	 * @param checkedRadioBtnId
	 */
	private void showFragment(int checkedRadioBtnId) {
		changeYButton(checkedRadioBtnId);
		String tag = getTagById(checkedRadioBtnId);
		Fragment mainFragment = (Fragment) getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (mainFragment == null) {
			// 如果没有找到对应的fragment则生成一个新的fragment，并添加到容器中
			Fragment newFragment = getFragmentByViewId(checkedRadioBtnId);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content, newFragment, tag).commit();
		} else {
			// 如果找到了fragment则显示它
			getSupportFragmentManager().beginTransaction().show(mainFragment)
					.commit();
		}
	}

	private Fragment getFragmentByViewId(int checkedWidgetId) {
		if (checkedWidgetId == R.id.radio0) {
			return homeFragment;
		} else if (checkedWidgetId == R.id.radio1) {
			return sortFragment;
		} else if (checkedWidgetId == R.id.radio2) {
			return orderFragment;
		} else if (checkedWidgetId == R.id.radio3) {
			return personFragment;
		}
		return null;
	}

	/**
	 * 为三个fragment分别取三个不同到tag名
	 * 
	 * @param widgetId
	 * @return
	 */
	private String getTagById(int widgetId) {
		if (widgetId == R.id.radio0) {
			return "home";
		} else if (widgetId == R.id.radio1) {
			return "tag";
		} else if (widgetId == R.id.radio2) {
			return "order";
		} else {
			return "me";
		}
	}

	/**
	 * 
	 * @param checkedWidgetId
	 * @user:pang
	 * @data:2015年9月13日
	 * @todo:不同的页签显示不同的按钮
	 * @return:void
	 */
	private void changeYButton(int checkedWidgetId) {
		if (checkedWidgetId == R.id.radio0) {
			home_ops_home.setVisibility(View.VISIBLE);
			home_ops_tag.setVisibility(View.GONE);
		} else if (checkedWidgetId == R.id.radio1) {
			home_ops_home.setVisibility(View.GONE);
			home_ops_tag.setVisibility(View.VISIBLE);
		} else if (checkedWidgetId == R.id.radio2) {
			home_ops_home.setVisibility(View.VISIBLE);
			home_ops_tag.setVisibility(View.GONE);
		} else if (checkedWidgetId == R.id.radio3) {
			home_ops_home.setVisibility(View.VISIBLE);
			home_ops_tag.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年9月13日
	 * @todo:分享信息
	 * @return:void
	 */
	public void addShare(View v) {
		if (!isLogin()) {
			no_login_alter(v);
			return;
		}
		Intent intent = new Intent(this, SharePrepareActivity.class);
		startActivity(intent);
	}

	/**
	 * 标签也签选择按钮
	 */
	public void showPopWin(View v) {
		if (!isLogin()) {
			no_login_alter(v);
			return;
		}
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.tag_space_ops_window, null);

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
		Button tag_space_ops_share_tag = (Button) view
				.findViewById(R.id.tag_space_ops_share_tag);
		tag_space_ops_share_tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				to_self_def(v);
			}

		});
		Button tag_space_ops_self_def = (Button) view
				.findViewById(R.id.tag_space_ops_self_def);
		tag_space_ops_self_def.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add_my_tag(v);
			}

		});
		/**
		 * 让popwindow消失
		 */
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");

			}
		});
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年9月14日
	 * @todo:用户贡献标签
	 * @return:void
	 */
	public void to_self_def(View v) {
		Intent it = new Intent(getApplicationContext(),
				TagsForUserDefActivity.class);
		startActivity(it);
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年6月23日
	 * @todo:添加自定义标签
	 * @return:void
	 */
	public void add_my_tag(View v) {
		Intent it = new Intent(getApplicationContext(),
				TagsForUserActivity.class);
		startActivity(it);
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年9月13日
	 * @todo:添加好友
	 * @return:void
	 */
	public void add_friends(View v) {
		if (isLogin()) {// 只有登陆用户才能由此操作
			Intent intent = new Intent(this, FriendSeachOpsActivity.class);
			startActivity(intent);
		} else {
			no_login_alter(v);
		}
	}

	/*********************************************************************************************/
	/**
	 * 回调接口
	 */
	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	/*
	 * 保存MyTouchListener接口的列表
	 */
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<HomeAllShowActivity.MyTouchListener>();

	/**
	 * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void registerMyTouchListener(MyTouchListener listener) {
		myTouchListeners.add(listener);
	}

	/**
	 * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void unRegisterMyTouchListener(MyTouchListener listener) {
		myTouchListeners.remove(listener);
	}

	/**
	 * 分发触摸事件给所有注册了MyTouchListener的接口
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (isLogin() && GlobalUserVariable.if_need_to_push_top_user) {// 说明起码在一次登录周期内没有推荐过
			GlobalUserVariable.setIf_need_to_push_top_user(false);// 置为不需要推荐
			if_need_to_push_top_user();
		}

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年7月13日
	 * @todo:判断有没有必要推荐用户
	 * @return:void
	 */
	private void if_need_to_push_top_user() {
		try {
			JSONObject d = new JSONObject();
			d.put("userUuid", userId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					PushBean pb = UserUtils.parseJsonAddToPushBean(data);
					if (pb != null) {
						Intent intent = new Intent(HomeAllShowActivity.this,
								FirstLoginTopUserListLayout.class);
						startActivity(intent);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.if_need_to_push_top_user, map,
					rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
