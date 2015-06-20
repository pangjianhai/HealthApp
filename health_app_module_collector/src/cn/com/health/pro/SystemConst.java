package cn.com.health.pro;

/**
 * 
 * @author pang
 * @todo 常量
 *
 */
public class SystemConst {

	public static final String mobile_local_dir = "G_HEALTH";

	public static final String mobile_local_dir_for_pic = mobile_local_dir
			+ "/PIC_COLLECTION";
	/**
	 * 本地文件的名字
	 */
	public static final String share_doc_name = "health_doc";

	/**
	 * 传往后台的参数KEY
	 */
	public static final String json_param_name = "para";

	public static final String server_url = "http://192.168.0.105:8080/IotApp/";

	// public static final String server_url =
	// "http://192.168.0.105:8080/IotApp/";

	/**
	 * 
	 * @author pang
	 * @todo URL常量
	 *
	 */
	public class FunctionUrl {
		/**
		 * 添加分享信息
		 */
		public static final String uploadHealthShare = "appSourceController/addShareEntity.do";

		public static final String uploadHealthShare2 = "loadFiles";

		/**
		 * 根据用户获取其所有的分享
		 */
		public static final String getShareByUserId = "appSourceController/getSharePageByUserId.do";

		/**
		 * 根据用户获取其好友圈的分享分页
		 */
		public static final String getFriendsShareByUserId = "appSourceController/getAttentionUserShare.do";

		/**
		 * 根据图片ID获取分享图片
		 */
		public static final String getShareImgById = "appSourceController/getSentenceInfoImg.do";

		/**
		 * 根据ID获取分享信息详情
		 */
		public static final String getShareDetailById = "appSourceController/getSentenceInfoHash.do";

		/**
		 * 添加新的用戶
		 */
		public static final String addUser = "appSourceController/addUser.do";

		/**
		 * 用户登录
		 */
		public static final String userLogin = "appSourceController/isLogin.do";

		/**
		 * 根据关键词搜索
		 */
		public static final String searchUserByName = "appSourceController/dimUser.do";

		/**
		 * 根据图片ID获取分享图片
		 */
		public static final String getHeadImgById = "appSourceController/getHeadImgByImgId.do";

		/**
		 * 根据用ID获取分享图片
		 */
		public static final String getHeadImgByUserId = "appSourceController/getUserHeadImgByUserId.do";

		/**
		 * 根据用户UUID获取用户信息
		 */
		public static final String getUserById = "appSourceController/getUserEntityById.do";

		/**
		 * 根据UUID编辑用户信息
		 */
		public static final String editUserById = "appSourceController/editUser.do";

		/**
		 * 根据关键词搜索信息
		 */
		public static final String getInfoByKey = "appSourceController/queryList.do";

		/**
		 * 判断一个人是否关注了另一个人
		 */
		public static final String if_some_one_focus_another = "appSourceController/exclickAddAttention.do";

		/**
		 * 一个人关注另外一个人
		 */
		public static final String some_one_focus_another = "appSourceController/clickAddAttention.do";

		/**
		 * 取消一个人关注另外一个人
		 */
		public static final String cancel_some_one_focus_another = "appSourceController/calldelAttention.do";

		/**
		 * 对分享或健康信息点赞
		 */
		public static final String view_for_share_or_healthinfo = "appSourceController/editShareEntitylikeNumAnddisLikeNum.do";

		/**
		 * 评论分享
		 */
		public static final String comment_share = "appSourceController/addCommentsShare.do";

		/**
		 * 评论文章
		 */
		public static final String comment_doc = "appSourceController/addCommentsDocument.do";

		/**
		 * 根据用户ID获取相关的数据
		 */
		public static final String getResultNumberUserById = "appSourceController/getResultNumberUserById.do";

		/**
		 * 获取我关注的好友分页
		 */
		public static final String getMyFocusUser = "appSourceController/getAttentionUser.do";

		/**
		 * 获取关注我的好友分页
		 */
		public static final String getFocusMeUser = "appSourceController/getUserAttention.do";

		/**
		 * 收藏文章或者信息
		 */
		public static final String collect_info_or_sentence = "appSourceController/collectSentenceInfo.do";

		/**
		 * 修改某人的地理位置信息
		 */
		public static final String update_person_location = "appSourceController/editUserPlace.do";

		/**
		 * 获取所有的类型信息
		 */
		public static final String get_all_doc_type = "appSourceController/getMoldall.do";

		/**
		 * 订阅
		 */
		public static final String sub_type = "appSourceController/subscriptionDocumentByTypeId.do";
		/**
		 * 取消订阅
		 */
		public static final String cancel_type = "appSourceController/cancelSubscriptionDocumentByTypeId.do";

		/**
		 * 根据类型获取文档
		 */
		public static final String get_docs_by_type = "appSourceController/queryDocumentByTypeId.do";

		/**
		 * 获取所有的类型信息
		 */
		public static final String get_subscrib_doc_type = "appSourceController/getSubscriptionMoldByUserId.do";

		/**
		 * 刷新最新分享信息的时候
		 */
		public static final String get_refrish_share_sentencs = "appSourceController/getRefurbishLatestSentenceInfoByUserId.do";

		/**
		 * 获取分享信息日排行榜
		 */
		public static final String get_share_order = "appSourceController/getRankinglistSentenceInfoBylikeNum.do";
	}

	/**
	 * 
	 * @author pang
	 * @todo 信息分享类型
	 *
	 */
	public class ShareInfoType {
		public static final String SHARE_TYPE_FOOD = "0";
		public static final String SHARE_TYPE_HEALTH = "1";
		public static final String SHARE_TYPE_SPORTS = "2";
	}
}
