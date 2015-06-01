package cn.com.health.pro.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import cn.com.health.pro.R;

public class PicUtil {

	public void showPopWin(Activity v, View anchor) {
		LayoutInflater inflater = (LayoutInflater) v
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.share_pop_window, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		PopupWindow window = new PopupWindow(view, 300,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// 在底部显示
		window.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
		window.setAnimationStyle(R.style.PopDownMenu);
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");

			}
		});
	}
}
