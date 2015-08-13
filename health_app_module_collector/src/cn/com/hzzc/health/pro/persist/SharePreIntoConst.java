package cn.com.hzzc.health.pro.persist;

/**
 * @todo 存储在本地的key的名字
 * @author pang
 *
 */
public interface SharePreIntoConst {

	/**
	 * @todo 和“我”页面相关的key
	 * @author pang
	 *
	 */
	public interface MainMeConst {
		public static final String if_need_reload = "Main_Me_Reload";
		public static final String last_set_reload_date = "Main_Last_Set_Reload_Date";
		public static final String my_share_num = "Main_Me_Share_Num";
		public static final String my_myfocus_num = "Main_Me_Myfocus_Num";
		public static final String my_focusme_num = "Main_Me_Focus_Num";
	}

	/**
	 * @todo 和“登录用户”相关的key
	 * @author pang
	 *
	 */
	public interface LoginUserConst {
		public static final String if_need_reload = "Main_Me_Reload";
		public static final String last_set_reload_date = "Main_Last_Set_Reload_Date";
		public static final String uuid = "Main_User_Uuid";
		public static final String userid = "Main_User_UserId";
		public static final String username = "Main_User_UserName";
		public static final String tags = "Main_User_Tags";
		public static final String sex = "Main_User_Sex";
		public static final String img = "Main_User_Img";
		public static final String birthday = "Main_User_Birthday";
		public static final String email = "Main_User_Email";
		public static final String sentence = "Main_User_Sentence";
	}
}
