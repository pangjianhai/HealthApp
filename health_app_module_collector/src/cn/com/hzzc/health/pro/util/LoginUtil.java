package cn.com.hzzc.health.pro.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import cn.com.hzzc.health.pro.AppLoginStartActivity;
import cn.com.hzzc.health.pro.AppRegActivity;
import cn.com.hzzc.health.pro.R;

/**
 * @todo 登陆工具类
 * @author pang
 *
 */
public class LoginUtil {

	/**
	 * 
	 * @param data
	 * @return
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:解析找回密码http请求返回的结果
	 * @return:String
	 */
	public static String parseEmailResult(String data) {
		try {
			JSONObject or_obj = new JSONObject(data);
			return or_obj.getString("flag");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "error";
	}

	public static void no_login_alter(View v, final Activity ctx) {
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View noLoginAlter = inflater.inflate(R.layout.app_nologin_alter,
				null, false);
		final PopupWindow popWindow = new PopupWindow(noLoginAlter, 400,
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		ImageView close_nologin_alert_image = (ImageView) noLoginAlter
				.findViewById(R.id.close_nologin_alert_image);
		close_nologin_alert_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}
		});

		Button alert_login = (Button) noLoginAlter
				.findViewById(R.id.alert_login);
		alert_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(ctx, AppLoginStartActivity.class);
				ctx.startActivity(intent);
				ctx.finish();// 必须删除当前singleTask的activity
			}

		});
		Button alert_reg = (Button) noLoginAlter.findViewById(R.id.alert_reg);
		alert_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(ctx, AppRegActivity.class);
				ctx.startActivity(intent);
				ctx.finish();// 必须删除当前singleTask的activity
			}

		});
		popWindow.setFocusable(true);
		backgroundAlpha(0.7f, ctx);
		popWindow.setOnDismissListener(new PoponDismissListener(ctx));
		popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	public static void backgroundAlpha(float bgAlpha, Activity ctx) {
		WindowManager.LayoutParams lp = ctx.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		ctx.getWindow().setAttributes(lp);
	}

	static class PoponDismissListener implements PopupWindow.OnDismissListener {

		private Activity ctx;

		public static PoponDismissListener newInstance(Activity ctx) {
			return new PoponDismissListener(ctx);
		}

		public PoponDismissListener(Activity ctx) {
			super();
			this.ctx = ctx;
		}

		@Override
		public void onDismiss() {
			backgroundAlpha(1f, ctx);
		}

	}
}
