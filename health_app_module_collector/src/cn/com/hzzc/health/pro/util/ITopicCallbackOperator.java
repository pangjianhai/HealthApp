package cn.com.hzzc.health.pro.util;

/**
 * 
 * @author pang
 * @todo 健康分享各种空间（个人空间，圈空间，他人空间）相关事件的回调接口
 *
 */
public interface ITopicCallbackOperator {

	/**
	 * 
	 * @param topicId
	 * @param index
	 * @user:pang
	 * @data:2015年9月4日
	 * @todo:TODO
	 * @return:void
	 */
	public void afterClickTopic(String topicId, int index);

}
