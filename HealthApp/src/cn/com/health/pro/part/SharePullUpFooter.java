/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package cn.com.health.pro.part;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.health.pro.R;

public class SharePullUpFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	/**
	 * 布局文件的三个布�?
	 */
	/**
	 * 布局
	 */
	private View mContentView;
	/**
	 * 进度�?
	 */
	private View mProgressBar;
	/**
	 * 提示信息
	 */
	private TextView mHintView;

	public SharePullUpFooter(Context context) {
		super(context);
		initView(context);
	}

	public SharePullUpFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 
	 * @tags @param state
	 * @date 2015�?�?6�? * @todo 根据不同的加载状况设置不同的展示状�?
	 * @author pang
	 */
	public void setState(int state) {
		/**
		 * 先都不可�?
		 */
		mHintView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_normal);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * 正常转改
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * 正在加载状�?
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * 不能加载了则隐藏加载�?
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		/**
		 * 高度�?
		 */
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		/**
		 * 高度是文字高�?
		 */
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * 
	 * @tags @param context
	 * @date 2015�?�?6�? * @todo 初始化footer，主要是加载布局文件
	 * @author pang
	 */
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.share_pull_up_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);
	}

}
