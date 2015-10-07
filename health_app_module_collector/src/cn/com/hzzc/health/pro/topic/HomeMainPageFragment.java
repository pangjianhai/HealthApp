package cn.com.hzzc.health.pro.topic;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.config.HealthApplication;

public class HomeMainPageFragment extends ParentHomeFragment {
	Resources resources;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private ImageView ivBottomLine;
	private TextView home_fragment_parent_space, home_fragment_parent_topic;

	private int currIndex = 0;
	private int bottomLineWidth;
	private int offset = 0;
	private int position_one;
	public final static int num = 2;
	Fragment home1;
	Fragment home2;
	/*** 导航栏 ***/
	private LinearLayout home_ops_title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_main_page_fragment, null);
		resources = getResources();
		InitWidth(view);
		InitTextView(view);
		InitViewPager(view);
		TranslateAnimation animation = new TranslateAnimation(position_one,
				offset, 0, 0);
		home_fragment_parent_space.setTextColor(resources
				.getColor(R.color.black));
		animation.setFillAfter(true);
		animation.setDuration(300);
		ivBottomLine.startAnimation(animation);
		return view;
	}

	private void InitTextView(View parentView) {
		home_ops_title = (LinearLayout) parentView
				.findViewById(R.id.home_ops_title);
		home_fragment_parent_space = (TextView) parentView
				.findViewById(R.id.home_fragment_parent_space);
		home_fragment_parent_topic = (TextView) parentView
				.findViewById(R.id.home_fragment_parent_topic);

		home_fragment_parent_space.setOnClickListener(new MyOnClickListener(0));
		home_fragment_parent_topic.setOnClickListener(new MyOnClickListener(1));
	}

	private void InitViewPager(View parentView) {
		mPager = (ViewPager) parentView.findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();

		home1 = new HomeAllSpaceShareFragment(this);
		home2 = new HomeAllSpaceTopicFragment(this);

		fragmentsList.add(home1);
		fragmentsList.add(home2);

		mPager.setAdapter(new HomeFragmentPagerAdapter(
				getChildFragmentManager(), fragmentsList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setCurrentItem(0);

	}

	private void InitWidth(View parentView) {
		ivBottomLine = (ImageView) parentView.findViewById(R.id.iv_bottom_line);
		bottomLineWidth = ivBottomLine.getLayoutParams().width;
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (int) ((screenW / num - bottomLineWidth) / 2);
		int avg = (int) (screenW / num);
		position_one = avg + offset;

	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int index) {
			Animation animation = null;
			switch (index) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, offset, 0,
							0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, position_one, 0,
							0);
				}
				break;
			}

			currIndex = index;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);

			if (index == 0) {
				int orange_color = Color.parseColor("#FFA500");
				int black_color = Color.parseColor("#000000");
				home_fragment_parent_space.setTextColor(orange_color);
				home_fragment_parent_topic.setTextColor(black_color);
			} else if (index == 1) {
				int orange_color = Color.parseColor("#FFA500");
				int black_color = Color.parseColor("#000000");
				home_fragment_parent_topic.setTextColor(orange_color);
				home_fragment_parent_space.setTextColor(black_color);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void screenScroll(float y) {
		if (y > 0) {
			showTitle();
		} else {
			hideTitle();
		}
	}

	public void showTitle() {
		home_ops_title.setVisibility(View.VISIBLE);
	}

	public void hideTitle() {
		home_ops_title.setVisibility(View.GONE);
	}

}
