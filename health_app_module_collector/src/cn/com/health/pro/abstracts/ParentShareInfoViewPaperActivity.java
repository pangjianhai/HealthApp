package cn.com.health.pro.abstracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.health.pro.BaseActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.adapter.TagAdapter;
import cn.com.health.pro.model.ShareSentenceEntity;
import cn.com.health.pro.model.Tag;
import cn.com.health.pro.util.ShareSentenceUtil;
import cn.com.health.pro.util.TagUtils;

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

	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private ImageView cursor;// 动画图片
	public View leftView = null;
	public View rightView = null;
	public int leftViewId;

	private List<String> titles = new ArrayList<String>();
	private ArrayList<View> list = new ArrayList<View>();// Tab页面列表

	/**
	 * 右侧标签页面的东西
	 */
	private EditText share_send_commont_tags_input;// 搜索框
	private ListView search_tags_listview;// listview
	private List<Tag> dataSource = new ArrayList<Tag>();// 标签源
	private TagAdapter adapter = null;// 适配器

	private LinearLayout selected_tag_linearlayout = null;
	private List<Tag> tags_selected = new ArrayList<Tag>();// 已经选中的标签
	private String will_del_tag_id = "";
	/*
	 * 标签分页
	 */
	private String key = "";
	private int page = 0;
	private int size = 5;
	private View footer;
	// 加载更多
	Button search_loadmore_btn = null;
	// 进度条
	ProgressBar load_progress_bar = null;

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
		initTagView();
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

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:初始动画
	 * @return:void
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		// offset = (screenW / 3 - bmpW) / 2;
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
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
			int black_color = Color.parseColor("#000000");
			int gray_color = Color.parseColor("#BEBEBE");
			Animation animation = null;
			switch (arg0) {
			case 0://
				animation = new TranslateAnimation(one, 0, 0, 0);
				t1.setTextColor(black_color);
				t2.setTextColor(gray_color);
				break;
			case 1://
				if (currIndex == 0) {//
					animation = new TranslateAnimation(offset, one, 0, 0);
					t2.setTextColor(black_color);
					t1.setTextColor(gray_color);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					t2.setTextColor(black_color);
				}
				break;
			case 2:

				animation = new TranslateAnimation(one, two, 0, 0);
				t2.setTextColor(black_color);

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
		selected_tag_linearlayout = (LinearLayout) rightView
				.findViewById(R.id.selected_tag_linearlayout);
		share_send_commont_tags_input = (EditText) rightView
				.findViewById(R.id.share_send_commont_tags_input);
		share_send_commont_tags_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int count, int after) {

			}

			/**
			 * 每次重置搜索条件
			 */
			@Override
			public void afterTextChanged(Editable edit) {
				key = edit.toString();// 关键词
				page = 0;// 重新搜索
				dataSource.clear();// 关键词换了，列表清空
				if (key != null && !"".equals(key.trim())) {
					freshDataForListView();
				}

			}
		});

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:初始化标签ListView
	 * @return:void
	 */
	public void initTagView() {

		search_tags_listview = (ListView) rightView
				.findViewById(R.id.search_tags_listview);
		/**
		 * 加载按钮的操作view
		 */
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);
		search_tags_listview.addFooterView(footer);
		adapter = new TagAdapter(this, dataSource);
		search_tags_listview.setAdapter(adapter);

		search_tags_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Tag tag = dataSource.get(position);
				afterTagSelected(tag);
			}
		});

		/**
		 * 获取“加载更多数据”和“进度条”的view，通过footer获取
		 */
		search_loadmore_btn = (Button) footer
				.findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) footer
				.findViewById(R.id.load_progress_bar);
		/**
		 * 加载更多事件
		 */
		search_loadmore_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				freshDataForListView();
			}
		});
	}

	/**
	 * 
	 * @param tag
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:选中一个标签之后更新UI
	 * @return:void
	 */
	private void afterTagSelected(Tag tag) {
		if (tags_selected != null && tags_selected.size() >= 4) {
			Toast.makeText(getApplicationContext(), "最多只能添加4个标签",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 如果没有被选中过则添加
		if (!ifChosen(tag)) {
			tags_selected.add(tag);// 将选择的tag放入list中
			selected_tag_linearlayout.setVisibility(View.VISIBLE);
			Button btn2 = createButton(tag);
			selected_tag_linearlayout.addView(btn2);
		}
	}

	/**
	 * 
	 * @param tag
	 * @return
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:判断选中的标签是否已经被选中过了，如果是则忽略
	 * @return:boolean
	 */
	private boolean ifChosen(Tag tag) {
		String tagId = tag.getId();
		boolean b = false;
		if (tags_selected != null && !tags_selected.isEmpty()) {
			for (Tag t : tags_selected) {
				String id = t.getId();
				if (id.equals(tagId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param tag
	 * @return
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:构造每一个选中的标签
	 * @return:Button
	 */
	private Button createButton(Tag tag) {
		final String tId = tag.getId();
		String tName = tag.getDisplayName();
		Button btn2 = new Button(this);
		btn2.setText(tName);
		Drawable d = getApplication().getResources().getDrawable(
				R.drawable.tag6);
		btn2.setBackgroundDrawable(d);
		btn2.setTextSize(14);
		btn2.setHeight(10);
		btn2.setMinHeight(11);// 构造完毕button
		/**
		 * 添加监听事件，可以让用户删除标签
		 */
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				will_del_tag_id = tId;// 点击标签允许其进行删除
				/**
				 * 注意构造builder的参数是rightView.getContext()
				 */
				AlertDialog.Builder builder = new AlertDialog.Builder(rightView
						.getContext()).setTitle("提示").setMessage("要删除吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								repaintUI(will_del_tag_id);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								will_del_tag_id = "";
							}
						});
				builder.show();

			}
		});
		return btn2;
	}

	/**
	 * 
	 * @param willDelId
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:确定删除一个标签
	 * @return:void
	 */
	public void repaintUI(String willDelId) {
		List<Tag> new_selected_list = new ArrayList<Tag>();
		if (tags_selected != null && !tags_selected.isEmpty()) {
			selected_tag_linearlayout.removeAllViews();// 请控所有的
			for (Tag tag : tags_selected) {
				final String tId = tag.getId();
				if (!tId.equals(willDelId)) {// 把选中的排除在外
					new_selected_list.add(tag);// 把留下来的tag放入新的list
					Button btn2 = createButton(tag);
					selected_tag_linearlayout.addView(btn2);// 重绘linearlayout
				}
			}
			tags_selected.clear();
			tags_selected.addAll(new_selected_list);
		}

	}

	/**
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:刷新
	 * @return:void
	 */
	public void freshDataForListView() {
		try {
			page = page + 1;
			JSONObject d = new JSONObject();
			d.put("tagName", key);
			d.put("page", page);
			d.put("rows", size);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<Tag> list = TagUtils
							.parseJsonAddToList(responseInfo.result);
					if (list != null && !list.isEmpty()) {
						dataSource.addAll(list);
						adapter.notifyDataSetChanged();
					}
					if (list == null || list.size() < size) {
						load_progress_bar.setVisibility(View.GONE);
						search_loadmore_btn.setVisibility(View.GONE);
					} else {
						load_progress_bar.setVisibility(View.GONE);
						search_loadmore_btn.setVisibility(View.VISIBLE);
					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					load_progress_bar.setVisibility(View.GONE);
					search_loadmore_btn.setVisibility(View.GONE);
				}
			};
			load_progress_bar.setVisibility(View.VISIBLE);
			search_loadmore_btn.setVisibility(View.GONE);
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_tag_by_key, map, rcb);
		} catch (Exception e) {
		}
	}
}
