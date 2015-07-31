package cn.com.health.pro;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import cn.com.health.pro.config.HealthApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 查看分享图片activity
 *
 */
public class CommonPicJazzActivity extends BaseActivity {
	/**
	 * 当前分享所有的图片
	 */
	List<String> ls = new ArrayList<String>();
	/**
	 * 选中的图片
	 */
	String clickImg = "";
	private MyJazzyViewPager mViewPager;

	private TextView show_pic_viewpager_title;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_jazz_pic_layout);
		show_pic_viewpager_title = (TextView) findViewById(R.id.show_pic_viewpager_title);
		/**
		 * 获取参数
		 */
		Intent it = getIntent();
		ls = (List<String>) it.getSerializableExtra("imgIdList");
		clickImg = it.getStringExtra("clickImg");
		mViewPager = (MyJazzyViewPager) findViewById(R.id.id_viewPager);
		show_pic_viewpager_title.setText("查看图片(" + (1) + "/" + ls.size() + ")");
		/**
		 * 添加适配器
		 */
		mViewPager.setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ImageView imageView = new ImageView(CommonPicJazzActivity.this);
				String pic_url = SystemConst.server_url
						+ SystemConst.FunctionUrl.getShareImgById
						+ "?para={imgId:'" + ls.get(position) + "'}";
				ImageLoader.getInstance().displayImage(pic_url, imageView);

				imageView.setScaleType(ScaleType.CENTER_CROP);
				container.addView(imageView);
				mViewPager.setObjectForPosition(imageView, position);

				/**
				 * 添加事件，点击关闭
				 */
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						close_page(v);

					}
				});
				return imageView;
			}

			@Override
			public int getCount() {
				return ls.size();
			}
		});

		/**
		 * 添加滚动时间
		 */
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * 滚动结束后看当前页数
			 */
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == 2) {
					int c = mViewPager.getCurrentItem();
					show_pic_viewpager_title.setText("查看图片(" + (c + 1) + "/"
							+ ls.size() + ")");
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {

			}
		});

	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月19日
	 * @todo 关闭当前窗口
	 * @author pang
	 */
	public void close_page(View v) {
		finish();
	}

}
