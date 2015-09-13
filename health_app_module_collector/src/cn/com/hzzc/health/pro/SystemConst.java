package cn.com.hzzc.health.pro;

/**
 * 
 * @author pang
 * @todo 常量
 *
 */
public class SystemConst {
	/**
	 * 普通一页多少航
	 */
	public static final int page_size = 20;

	public static final String mobile_local_dir = "G_HEALTH";

	/**
	 * 图片缓存地址
	 */
	public static final String mobile_local_dir_for_pic = mobile_local_dir
			+ "/PIC_COLLECTION";

	/**
	 * app下载地址
	 */
	public static final String mobile_local_dir_for_download_app = mobile_local_dir
			+ "/APP_DOWNLOAD";
	/**
	 * 本地文件的名字
	 */
	public static final String share_doc_name = "health_doc";

	/**
	 * 传往后台的参数KEY
	 */
	public static final String json_param_name = "para";

	// public static final String server_url =
	// "http://www.kx360.com.cn/iotapp/";

	public static final String server_url = "http://192.168.0.105:8080/IotApp/";

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
		 * 非登录用户看到的信息
		 */
		public static final String noLoginReadSpace = "appSourceController/getRankinglistBycurrentDate.do";

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

		/**
		 * 根据关键词搜索标签
		 */
		public static final String get_tag_by_key = "appSourceController/queryTagBytagName.do";

		/**
		 * 根据标签获取分享信息
		 */
		public static final String get_share_by_tag = "appSourceController/getShareTagsByTagId.do";

		/**
		 * 给用户添加标签
		 */
		public static final String add_tag_to_user = "appSourceController/addUserTagsByUserId.do";

		/**
		 * 根据用户ID获取已经自定义的标签
		 */
		public static final String get_tags_by_user = "appSourceController/getUserTagsByUserId.do";

		/**
		 * 根据用户ID获取用户收藏
		 */
		public static final String get_collection_by_id = "appSourceController/getCollectsByuserId.do";

		/**
		 * 根据分享ID获取评论
		 */
		public static final String get_share_comment_by_id = "appSourceController/getPageShareCommentByShareId.do";

		/**
		 * 根据邮件找回密码
		 */
		public static final String login_get_pwd_by_email = "appSourceController/getPasswordSendEmal.do";

		/**
		 * 检测app版本
		 */
		public static final String scan_app_version_num = "appSourceController/onloadApp.do";

		/**
		 * 用户反馈意见
		 */
		public static final String user_add_view = "appSourceController/addViews.do";

		/**
		 * 成功下载之后数量统计
		 */
		public static final String after_down_load_success = "appSourceController/onloadSuccess.do";

		/**
		 * 获取给注册用户推荐的好友名单
		 */
		public static final String get_top_user_list = "appSourceController/getUsersRankinglstBySharelikeNum.do";

		/**
		 * 判断有没有必要推荐用户
		 */
		public static final String if_need_to_push_top_user = "appSourceController/getLoginNumAndLoginDateByUuId.do";

		/**
		 * 根据分享ID获取发布人ID
		 */
		public static final String getUserIdByShareId = "appSourceController/getUserIdByShareId.do";

		/**
		 * 判断登录ID是否存在
		 */
		public static final String ifUserLoginIdExist = "appSourceController/valiUser.do";

		/**
		 * 判断注册邮箱是否存在
		 */
		public static final String ifUserEmailExist = "appSourceController/valiUserEmail.do";

		/**
		 * 贡献标签
		 */
		public static final String shareTagToPlatform = "appSourceController/addTagByUserId.do";

		/**
		 * 判断一个人对某个分享信息是否好评或者差评
		 */
		public static final String judgeIflikeNumOrdislikeNum = "appSourceController/judgeIflikeNumOrdislikeNum.do";

		/**
		 * 微信分享根据图片ID获取分享图片
		 */
		public static final String weixin_getShareImgById = "appSourceController/getSentenceInfoImgForWeixin.do";

		/**
		 * 微信分享根据分享ID获取分享内容
		 */
		public static final String weixin_getShareById = "appSourceController/queryShareByShareIdIndexWeixin.do";

		/**
		 * 将转发到第三方平台的信息传递到后台，让后台提醒对方
		 */
		public static final String share_to_3part_platform = "appSourceController/tranSpondSentenceHash.do";

	}

	/**
	 * @todo 和主题相关的链接
	 * @author pang
	 *
	 */
	public class TopicUrl {
		/**
		 * 获取所有健康主题的分页信息
		 */
		public static final String get_page_topic = "topicController/queryTopicPageall.do";

		/**
		 * 获取主题的详情信息
		 */
		public static final String get_topic_info_by_id = "topicController/getBaseTopicBypicId.do";

		/**
		 * 根据用ID获取主题图片
		 */
		public static final String getTopicImgByImgId = "topicController/getTopicImgByImgId.do";

		/**
		 * 参与主题
		 */
		public static final String getInTopic = "topicController/addParticipationTopicByUserId.do";
		/**
		 * 退出主题
		 */
		public static final String getOutTopic = "topicController/delParticipationTopicByUserId.do";

		/**
		 * 判断某人是否参与某个主题
		 */
		public static final String isInTopic = "topicController/judgePicUserId.do";

		/**
		 * 根据主题获取评论
		 */
		public static final String getCommentByTopic = "topicController/queryTopicPostPageallByPicId.do";

		/**
		 * 根据图片ID获取主题评论图片
		 */
		public static final String getTopicPostImgById = "topicController/getTopicImgByImgId.do";

		/**
		 * 获取用户已经参与的主题的活动
		 */
		public static final String getTopicForParticipation = "topicController/queryTopicallByUserIdParticipation.do";

		/**
		 * 获取用户已经未参与的主题的活动
		 */
		public static final String getTopicForUnParticipation = "topicController/queryTopicallByUserIdNoneParticipation.do";

		/**
		 * 添加主题评论
		 */
		public static final String addTopicPost = "topicController/addTopicPost.do";

		/**
		 * 获取主题参与人数以及评论总是
		 */
		public static final String querypicPostParticipationNumAndCommentNum = "topicController/querypicPostParticipationNumAndCommentNum.do";

		/**
		 * 点赞主题评论
		 */
		public static final String clickPostCommentGoodNum = "topicController/clickGoodNum.do";

		/**
		 * 获取具体的主题评论的信息
		 */
		public static final String getTopicPostBytopicPostId = "topicController/getTopicPostBytopicPostId.do";

		/**
		 * 判断用户是否点赞评论
		 */
		public static final String judgeFavoritePicByUserId = "topicController/judgePicByUserId.do";

		/**
		 * 获取主题下的参与人分页
		 */
		public static final String queryTopicUserByTopicId = "topicController/queryTopicUserByTopicId.do";

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

	/**
	 * @todo 收藏的类型
	 * @author pang
	 *
	 */
	public class CollectionType {
		public static final String COLLECTION_TYPE_SHARE = "0";
		public static final String COLLECTION_TYPE_DOC = "1";
	}
}
