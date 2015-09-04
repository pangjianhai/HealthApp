package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.FriendSeachOpsActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.adapter.HomeFrameAdapter;

public class HomeFrameActivity extends BaseFragmentActivity {

	private ViewPager viewPager;
	private ImageView imageView;
	private List<Fragment> lists = new ArrayList<Fragment>();
	private HomeFrameAdapter myAdapter;
	private Bitmap cursor;
	private int offSet;
	private int currentItem;
	private Matrix matrix = new Matrix();
	private int bmWidth;
	private Animation animation;
	private TextView home_fragment_parent_space, home_fragment_parent_topic;

	private LinearLayout home_ops_title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_fragment);

		/**
		 * 初始化页面元素
		 */
		imageView = (ImageView) findViewById(R.id.cursor);
		home_fragment_parent_space = (TextView) findViewById(R.id.home_fragment_parent_space);
		home_fragment_parent_topic = (TextView) findViewById(R.id.home_fragment_parent_topic);

		Fragment sspace = new ShareSpaceFragment();
		Fragment tspace = new TopicSpaceFragment();
		lists.add(sspace);
		lists.add(tspace);

		/**
		 * 设置导航图片的样式
		 */
		initeCursor();

		myAdapter = new HomeFrameAdapter(getSupportFragmentManager(), lists);
		/**
		 * 初始化viewpaper
		 */
		viewPager = (ViewPager) findViewById(R.id.home_fragment_parent_viewpager);
		viewPager.setAdapter(myAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		/**
		 * 给导航字添加监听事件
		 */
		initTextView();
		home_ops_title = (LinearLayout) findViewById(R.id.home_ops_title);

	}

	private void initeCursor() {
		cursor = BitmapFactory
				.decodeResource(getResources(), R.drawable.roller);
		bmWidth = cursor.getWidth();

		DisplayMetrics dm;
		dm = getResources().getDisplayMetrics();
		/**
		 * this.getResources() 获取资源 this.getResources().getDisplayMetrics()
		 * 获取屏幕信息 this.getResources().getDisplayMetrics().widthPixels 获取屏幕宽度
		 * this.getResources().getDisplayMetrics().widthPixels*2/3 偏移量是屏幕宽度的2/3
		 */

		offSet = (dm.widthPixels - 2 * bmWidth) / 4;
		matrix.setTranslate(offSet, 0);
		imageView.setImageMatrix(matrix);
		currentItem = 0;
	}

	private void initTextView() {
		home_fragment_parent_space
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						viewPager.setCurrentItem(0);
					}
				});

		home_fragment_parent_topic
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						viewPager.setCurrentItem(1);
					}
				});
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offSet * 2 + bmWidth;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) { // arg0:点击的第几页
			Animation animation = new TranslateAnimation(one * currentItem, one
					* arg0, 0, 0);// 显然这个比较简洁，只有一行代码。
			currentItem = arg0;

			animation.setDuration(500);
			animation.setFillAfter(true);
			imageView.startAnimation(animation);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年7月20日
	 * @todo:搜索并添加好友
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

	/********************* 页面 **************************/
	/**
	 * 回调接口
	 */
	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	/*
	 * 保存MyTouchListener接口的列表
	 */
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<HomeFrameActivity.MyTouchListener>();

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

	public void showTitle() {
		home_ops_title.setVisibility(View.VISIBLE);
	}

	public void hideTitle() {
		home_ops_title.setVisibility(View.GONE);
	}

}
