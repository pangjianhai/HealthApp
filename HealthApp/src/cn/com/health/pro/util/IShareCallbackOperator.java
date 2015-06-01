package cn.com.health.pro.util;

/**
 * 
 * @author pang
 * @todo 健康分享各种空间（个人空间，圈空间，他人空间）相关事件的回调接口
 *
 */
public interface IShareCallbackOperator {

	/**
	 * 
	 * @tags @param shareId
	 * @date 2015�?�?�?
	 * @todo 点击回复之后
	 * @author pang
	 */
	public void afterClickReply(String shareId);

	/**
	 * 
	 * @tags @param shareId
	 * @date 2015�?�?�?
	 * @todo 点击内容事件
	 * @author pang
	 */
	public void afterClickContent(String shareId);

	/**
	 * 
	 * @tags @param shareId
	 * @date 2015�?�?�?
	 * @todo 点击作�?事件
	 * @author pang
	 */
	public void afterClickAuthor(String shareId);

	/**
	 * ]
	 * 
	 * @tags @param shareId
	 * @date 2015年5月20日
	 * @todo 点击OK
	 * @author pang
	 */
	public void afterClickOk(String shareId);

	/**
	 * 
	 * @tags @param shareId
	 * @date 2015年5月20日
	 * @todo 点击NOOK
	 * @author pang
	 */
	public void afterClickNook(String shareId);

}
