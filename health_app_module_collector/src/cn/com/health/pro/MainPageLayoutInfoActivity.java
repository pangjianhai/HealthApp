package cn.com.health.pro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.com.health.pro.abstracts.ParentMainActivity;
import cn.com.health.pro.adapter.InfoAdapter;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.entity.InfoEntity;
import cn.com.health.pro.part.MyFloatView;
import cn.com.health.pro.task.SearchInfoAsyncTask;

/**
 * 
 * @author pang
 * @todo 主页空间消息
 *
 */
public class MainPageLayoutInfoActivity extends ParentMainActivity {

	/**
	 * 搜索关键词
	 */
	private String key = "";
	private int rowNum = 0;
	private int pageCount = 20;

	/**
	 * 显示空间
	 */
	ListView listview;

	/**
	 * 数据源
	 */
	private List<InfoEntity> infoList = new ArrayList<InfoEntity>();

	// 关键词填写框子
	private EditText home_page_search_key;
	// 搜索按钮
	ImageButton search_info_btn_menu = null;

	// 搜索按钮
	ImageButton home_page_fenlei_btn = null;
	// 加载更多
	Button search_loadmore_btn = null;
	// 进度条
	ProgressBar load_progress_bar = null;
	// 适配器
	InfoAdapter adapter = null;
	// footer视图
	View footer;

	/**
	 * 窗口
	 */
	private WindowManager wm = null;
	private WindowManager.LayoutParams wmParams = null;
	private MyFloatView myFV = null;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_page_layout_info);
		listview = (ListView) findViewById(R.id.home_info_list);
		footer = getLayoutInflater().inflate(R.layout.info_search_more, null);

		listview.addFooterView(footer, null, false);
		adapter = new InfoAdapter(MainPageLayoutInfoActivity.this,
				R.layout.info_search_item, infoList);
		listview.setAdapter(adapter);

		/**
		 * footer
		 */
		search_loadmore_btn = (Button) findViewById(R.id.search_loadmore_btn);
		load_progress_bar = (ProgressBar) findViewById(R.id.load_progress_bar);

		home_page_search_key = (EditText) findViewById(R.id.home_page_search_key);
		search_info_btn_menu = (ImageButton) findViewById(R.id.home_page_search_btn);
		home_page_fenlei_btn = (ImageButton) findViewById(R.id.home_page_fenlei_btn);
		loadMoreData();
		addListener();

		// createView();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月18日
	 * @todo 添加事件
	 * @author pang
	 */
	private void addListener() {
		/**
		 * 点击搜索时间
		 */
		search_info_btn_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 开始搜索
				rowNum = 0;
				key = home_page_search_key.getText().toString();
				infoList.clear();
				loadMoreData();
			}
		});

		/**
		 * 根据分类查看
		 */
		home_page_fenlei_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainPageLayoutInfoActivity.this,
						InfoTypeListviewActivity.class);
				startActivity(intent);
			}
		});

		/**
		 * 加载更多事件
		 */
		search_loadmore_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreData();
			}
		});
		/**
		 * listview 选中条目事件
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				InfoEntity entity = (InfoEntity) infoList.get(position);
				String docId = entity.getId();
				Intent intent = new Intent(MainPageLayoutInfoActivity.this,
						InfoDetailActivity.class);
				intent.putExtra("doc_id", docId);
				startActivity(intent);
			}
		});
	}

	/**
	 * 添加更多
	 */
	private void loadMoreData() {
		rowNum = rowNum + 1;
		try {
			JSONObject j = new JSONObject();
			j.put("keywordsName", key);
			j.put("rows", pageCount);
			j.put("page", rowNum);
			new SearchInfoAsyncTask(MainPageLayoutInfoActivity.this).execute(j
					.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @tags
	 * @date 2015年5月18日
	 * @todo 开始搜索前的动作
	 * @author pang
	 */
	public void beforeSearch() {
		load_progress_bar.setVisibility(View.VISIBLE);//
		search_loadmore_btn.setVisibility(View.GONE);//
	}

	/**
	 * 
	 * @tags @param c
	 * @date 2015年5月18日
	 * @todo 搜索结束后的动作
	 * @author pang
	 */
	public void searchOver(List<InfoEntity> c) {
		infoList.addAll(c);
		adapter.notifyDataSetChanged();
		search_loadmore_btn.setVisibility(View.VISIBLE);
		load_progress_bar.setVisibility(View.GONE);
	}

	public void backoff(View v) {
		finish();
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月12日
	 * @todo:创建浮框，并且显示
	 * @return:void
	 */
	private void createView() {
		myFV = new MyFloatView(MainPageLayoutInfoActivity.this);
		myFV.setImageResource(R.drawable.fenlei1);
		wm = (WindowManager) getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		// 设置LayoutParams(全局变量）相关参数
		wmParams = ((HealthApplication) getApplication()).getMywmParams();

		wmParams.type = LayoutParams.TYPE_PHONE; // 设置window type
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = 200;
		wmParams.y = 310;
		// 设置悬浮窗口长宽数据
		wmParams.width = 100;
		wmParams.height = 100;
		// 显示myFloatView图像
		wm.addView(myFV, wmParams);

		myFV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "toast",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void toNewPage() {
		Toast.makeText(HealthApplication.getContext(), "toast",
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(MainPageLayoutInfoActivity.this,
				InfoTypeListviewActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}