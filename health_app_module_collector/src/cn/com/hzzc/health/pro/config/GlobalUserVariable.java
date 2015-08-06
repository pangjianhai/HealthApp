package cn.com.hzzc.health.pro.config;

/**
 * @TODO 用户全局变量
 * @author pang
 *
 */
public class GlobalUserVariable {

	/**
	 * 是否有必要向刚祖册的用户推荐人
	 */
	public static boolean if_need_to_push_top_user = true;

	public static boolean isIf_need_to_push_top_user() {
		return if_need_to_push_top_user;
	}

	public static void setIf_need_to_push_top_user(
			boolean if_need_to_push_top_user) {
		GlobalUserVariable.if_need_to_push_top_user = if_need_to_push_top_user;
	}

}
