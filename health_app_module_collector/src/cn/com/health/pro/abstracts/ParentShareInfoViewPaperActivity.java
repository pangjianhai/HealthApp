package cn.com.health.pro.abstracts;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import cn.com.health.pro.BaseActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.adapter.TagAdapter;
import cn.com.health.pro.model.Tag;

/**
 * 
 * @author pang
 * @todo 关于页面切换
 *
 */
public abstract class ParentShareInfoViewPaperActivity extends BaseActivity {

	/**
	 * 切换页面的配置
	 */
	ViewPager pager = null;
	TabHost tabHost = null;
	TextView t1, t2, t3;

	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private ImageView cursor;
	public View leftView = null;
	public View rightView = null;
	public int leftViewId;

	private List<String> titles = new ArrayList<String>();
	private ArrayList<View> list = new ArrayList<View>();

	/**
	 * 右侧标签页面的东西
	 */
	private EditText share_send_commont_tags_input;
	private ListView search_tags_listview;
	private List<Tag> dataSource = new ArrayList<Tag>();
	private TagAdapter adapter = null;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_send_common);

		setLeftViewId(leftViewId);
		InitImageView();
		initTextView();
		initPagerViewer();

		initTagInput();
	}

	public abstract void setLeftViewId(int id);

	private void initTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));

	}

	private void initPagerViewer() {
		pager = (ViewPager) findViewById(R.id.viewpage);
		titles.add("left");
		titles.add("center");
		LayoutInflater layoutInflater = getLayoutInflater();
		leftView = layoutInflater.inflate(leftViewId, null);
		rightView = layoutInflater.inflate(R.layout.share_send_tags, null);
		list.add(leftView);
		list.add(rightView);
		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		// offset = (screenW / 3 - bmpW) / 2;
		offset = (screenW / 2 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	public class MyPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;
		int two = one * 2;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				animation = new TranslateAnimation(one, 0, 0, 0);
				t1.setTextColor(0xffffffff);
				t2.setTextColor(0xff808080);
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
					t2.setTextColor(0xffffffff);
					t1.setTextColor(0xff808080);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					t2.setTextColor(0xffffffff);
					// t3.setTextColor(0xff808080);
				}
				break;
			case 2:

				animation = new TranslateAnimation(one, two, 0, 0);
				// t3.setTextColor(0xffffffff);
				t2.setTextColor(0xff808080);

				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);
		}
	};

	/**
	 * 
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:初始化输入框
	 * @return:void
	 */
	public void initTagInput() {
		share_send_commont_tags_input = (EditText) rightView
				.findViewById(R.id.share_send_commont_tags_input);
		share_send_commont_tags_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start, int before,
					int count) {
				// text 输入框中改变后的字符串信息
				// start 输入框中改变后的字符串的起始位置
				// before 输入框中改变前的字符串的位置 默认为0
				// count 输入框中改变后的一共输入字符串的数量
				System.out.println("text:" + text);

			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable edit) {
				testList();

			}
		});
		// testList();

		search_tags_listview = (ListView) rightView
				.findViewById(R.id.search_tags_listview);
		adapter = new TagAdapter(this, dataSource);
		search_tags_listview.setAdapter(adapter);
	}

	public void testList() {
		List<Tag> l = new ArrayList<Tag>();
		for (int i = 0; i < 5; i++) {
			Tag t = new Tag();
			t.setDisplayName("dsi" + i);
			t.setId("1" + i);
			l.add(t);
		}
		//dataSource.clear();
		dataSource.addAll(l);
		adapter.notifyDataSetChanged();
	}

}
