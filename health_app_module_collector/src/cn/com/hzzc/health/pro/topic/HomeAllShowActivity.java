package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.com.hzzc.health.pro.R;

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

		radio0.performClick();// 此处设置默认第三个选项卡对应的fragment显示

		home_ops_home = ((ImageButton) findViewById(R.id.home_ops_home));
		home_ops_tag = ((ImageButton) findViewById(R.id.home_ops_tag));
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

}
