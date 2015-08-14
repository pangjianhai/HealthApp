package cn.com.hzzc.health.pro.part;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @todo 分享详情页面的图片gridview
 * @author pang
 *
 */
public class SentenceGridView extends GridView {
	public SentenceGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}
}