package cn.com.hzzc.health.pro.topic;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.config.HealthApplication;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public abstract class BaseFragment extends Fragment {

	private VelocityTracker vt;// 生命变量
	/**
	 * Fragment中，注册 接收MainActivity的Touch回调的对象
	 * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
	 */
	private HomeFrameActivity.MyTouchListener mTouchListener = new HomeFrameActivity.MyTouchListener() {
		@Override
		public void onTouchEvent(MotionEvent event) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (vt == null) {
					// 初始化velocityTracker的对象 vt 用来监测motionevent的动作
					vt = VelocityTracker.obtain();
				} else {
					vt.clear();
				}
				vt.addMovement(event);
				break;
			case MotionEvent.ACTION_MOVE:
				vt.addMovement(event);
				break;
			case MotionEvent.ACTION_UP:
				vt.addMovement(event);
				vt.computeCurrentVelocity(1000, Float.MAX_VALUE);
				resetTitleStatus(vt.getYVelocity());
				vt.recycle();
				vt = null;
				break;
			}
		}

	};

	/**
	 * 用户ID
	 */
	public String userId;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		/**
		 * 获取用户ID
		 */
		userId = HealthApplication.getUserId();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((HomeFrameActivity) this.getActivity())
				.registerMyTouchListener(mTouchListener);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年9月4日
	 * @todo:重置滚动栏状态
	 * @return:void
	 */
	public abstract void resetTitleStatus(float v);

	/**
	 * 
	 * @return
	 * @user:pang
	 * @data:2015年7月20日
	 * @todo:是否登录使用APP
	 * @return:boolean
	 */
	public boolean isLogin() {
		if (userId == null || "".equals(userId)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @tags @param contenxt
	 * @tags @return
	 * @date 2015年5月28日
	 * @todo 判断是否可以联网
	 * @author pang
	 */
	public boolean isNetWorkConnected() {
		ConnectivityManager cm = (ConnectivityManager) HealthApplication
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param url
	 *            地址
	 * @param p
	 *            参数
	 * @param rcb
	 *            回调函数
	 * @user:pang
	 * @data:2015年7月13日
	 * @todo:发送普通的POST http请求
	 * @return:void
	 */
	public void send_normal_request(String url, Map<String, String> p,
			RequestCallBack<?> rcb) {
		if (!isNetWorkConnected()) {
			Toast.makeText(HealthApplication.getContext(), "没有网络咯！",
					Toast.LENGTH_SHORT).show();
			return;
		}
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
		// Toast.makeText(this, "【测试代码】刚进行了http请求", Toast.LENGTH_SHORT).show();
	}

	/**************************************** 关于非登录用户需要提示的popwindow ********************************************/

}
