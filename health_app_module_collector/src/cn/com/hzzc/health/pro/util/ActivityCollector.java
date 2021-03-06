package cn.com.hzzc.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.persist.SharedPreInto;
import android.app.Activity;

/**
 * @todo activity管理器，所有的activity都需要加入此List
 * @author pang
 *
 */
public class ActivityCollector {

	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	/**
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:退出时用到
	 * @return:void
	 */
	public static void finishAll() {
		/**
		 * 退出之后删除本地账号数据
		 */
		new SharedPreInto(HealthApplication.getContext())
				.clearAccountAfterLoginOut();
		/**
		 * 推出后因为返回一个特定页面，所以将application的全局用户ID置为空
		 */
		HealthApplication.setUserId("");
		for (Activity activity : activities) {
			if (activity != null && !activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
