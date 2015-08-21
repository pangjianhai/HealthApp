package cn.com.hzzc.health.pro.part;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import cn.com.hzzc.health.pro.R;

/**
 * @todo 自定义EditText 下划线样式的输入框
 * @author pang
 *
 */
public class LineEditText extends EditText {
	private final static String TAG = "LineEditText";
	private Drawable imgInable;
	private Drawable imgAble;
	private Context mContext;

	public LineEditText(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public LineEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		imgInable = mContext.getResources().getDrawable(R.drawable.delete_gray);
		imgAble = mContext.getResources().getDrawable(R.drawable.delete);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
		setDrawable();
	}

	// 设置删除图片
	private void setDrawable() {
		if (length() < 1)
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
		else
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
	}

	// 处理删除事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
			int eventX = (int) event.getRawX();
			int eventY = (int) event.getRawY();
			Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
			Rect rect = new Rect();
			getGlobalVisibleRect(rect);
			rect.left = rect.right - 50;
			if (rect.contains(eventX, eventY))
				setText("");
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
