package cn.com.hzzc.health.pro.part;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.Tag;
import cn.com.hzzc.health.pro.util.TagUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 自定义的ScrollView，在其中动态地对图片进行添加。
 * 
 * @author guolin
 */
public class MyScrollView extends ScrollView implements OnTouchListener {

	/**
	 * 每页要加载的图片数量
	 */
	public static final int PAGE_SIZE = 15;

	/**
	 * 记录当前已加载到第几页
	 */
	private int page;

	/**
	 * 每一列的宽度
	 */
	private int columnWidth;

	/**
	 * 当前第一列的高度
	 */
	private int firstColumnHeight;

	/**
	 * 当前第二列的高度
	 */
	private int secondColumnHeight;

	/**
	 * 当前第三列的高度
	 */
	private int thirdColumnHeight;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;

	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumn;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumn;

	/**
	 * 第三列的布局
	 */
	private LinearLayout thirdColumn;

	/**
	 * MyScrollView下的直接子布局。
	 */
	private static View scrollLayout;

	/**
	 * MyScrollView布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;

	/********************** 和搜索相关 *********************************/
	private String data_key = "";
	private int data_begin = 1;
	private int data_limit = 20;
	private boolean hasData = true;
	private BtnOps btnOps;

	/**
	 * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			MyScrollView myScrollView = (MyScrollView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// 如果当前的滚动位置和上次相同，表示已停止滚动
			if (scrollY == lastScrollY) {
				// 当滚动的最底部，并且还可以继续取，开始加载下一页的图片
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
						&& hasData) {
					myScrollView.loadMoreImages();
				}
			} else {
				lastScrollY = scrollY;
				Message message = new Message();
				message.obj = myScrollView;
				// 5毫秒后再次对滚动位置进行判断
				handler.sendMessageDelayed(message, 1);
			}
		};

	};

	/**
	 * MyScrollView的构造函数。
	 * 
	 * @param context
	 * @param attrs
	 */
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}

	/**
	 * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			scrollViewHeight = getHeight();
			scrollLayout = getChildAt(0);
			firstColumn = (LinearLayout) findViewById(R.id.first_column);
			secondColumn = (LinearLayout) findViewById(R.id.second_column);
			thirdColumn = (LinearLayout) findViewById(R.id.third_column);
			columnWidth = firstColumn.getWidth();
			loadOnce = true;
			loadMoreImages();
		}
	}

	/**
	 * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Message message = new Message();
			message.obj = this;
			handler.sendMessageDelayed(message, 1);
		}
		return false;
	}

	/**
	 * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
	 */
	public void loadMoreImages() {
		if (hasData) {
			freshDataForListView();
		} else {
			Toast.makeText(getContext(), "已没有更多标签", Toast.LENGTH_SHORT).show();
		}
	}

	public void send_normal_request(String url, Map<String, String> p,
			RequestCallBack<?> rcb) {
		RequestParams params = new RequestParams();
		if (p != null) {
			Iterator<Map.Entry<String, String>> it = p.entrySet().iterator();
			/**
			 * 添加参数
			 */
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				params.addBodyParameter(entry.getKey(), entry.getValue());
			}
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, rcb);
	}

	public void freshDataForListView() {
		Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
		try {
			JSONObject d = new JSONObject();
			d.put("tagName", data_key);
			d.put("page", data_begin);
			d.put("rows", data_limit);
			data_begin = data_begin + 1;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					List<Tag> list = TagUtils
							.parseJsonAddToList(responseInfo.result);
					if (list != null && !list.isEmpty()) {
						for (Tag tag : list) {
							addTag(tag);
						}
					}
					if (list == null || list.size() < data_limit) {
						Toast.makeText(getContext(), "已没有更多标签",
								Toast.LENGTH_SHORT).show();
						hasData = false;
						data_begin = 1;
					} else {
					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					hasData = false;
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_tag_by_key, map, rcb);
		} catch (Exception e) {
		}
	}

	private void addTag(final Tag tag) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 100);
		params.height = 77 * (new Random().nextInt(2) + 1);
		params.setMargins(1, 1, 1, 1);
		Button btn = new Button(getContext());
		btn.setLayoutParams(params);
		btn.setText(tag.getDisplayName());
		btn.setBackgroundResource(getRandomPic());
		btn.setTextSize(14);
		//
		android.view.ViewGroup.LayoutParams btnPara = btn.getLayoutParams();
		findColumnToAdd(btnPara.height).addView(btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnOps.afterClick(tag);
			}
		});
	}

	/**
	 * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
	 * 
	 * @param imageHeight
	 * @return 应该添加图片的一列
	 */
	private LinearLayout findColumnToAdd(int imageHeight) {
		if (firstColumnHeight <= secondColumnHeight) {
			if (firstColumnHeight <= thirdColumnHeight) {
				firstColumnHeight += imageHeight;
				return firstColumn;
			}
			thirdColumnHeight += imageHeight;
			return thirdColumn;
		} else {
			if (secondColumnHeight <= thirdColumnHeight) {
				secondColumnHeight += imageHeight;
				return secondColumn;
			}
			thirdColumnHeight += imageHeight;
			return thirdColumn;
		}
	}

	private int getRandomPic() {
		int[] pics = new int[] { R.drawable.tag_color_one,
				R.drawable.tag_color_tow, R.drawable.tag_color_three,
				R.drawable.tag_color_four, R.drawable.tag_color_five };
		int number = new Random().nextInt(pics.length);
		return pics[number];
	}

	public String getData_key() {
		return data_key;
	}

	/**
	 * 
	 * @param data_key
	 * @user:pang
	 * @data:2015年8月4日
	 * @todo:重置搜索关键词之后能够搜索
	 * @return:void
	 */
	public void setData_key(String data_key) {
		this.data_key = data_key;
		hasData = true;
		data_begin = 1;
	}

	public int getData_begin() {
		return data_begin;
	}

	public void setData_begin(int data_begin) {
		this.data_begin = data_begin;
	}

	public boolean isHasData() {
		return hasData;
	}

	public void setHasData(boolean hasData) {
		this.hasData = hasData;
	}

	/**
	 * 
	 * @param key
	 * @user:pang
	 * @data:2015年8月4日
	 * @todo:根据关键词重新搜索
	 * @return:void
	 */
	public void restartNewKeySearch(String key) {
		this.data_key = key;
		hasData = true;
		data_begin = 1;
		firstColumn.removeAllViews();
		secondColumn.removeAllViews();
		thirdColumn.removeAllViews();
		loadMoreImages();
	}

	public BtnOps getBtnOps() {
		return btnOps;
	}

	public void setBtnOps(BtnOps btnOps) {
		this.btnOps = btnOps;
	}

	public interface BtnOps {
		public void afterClick(Tag tag);
	}

}