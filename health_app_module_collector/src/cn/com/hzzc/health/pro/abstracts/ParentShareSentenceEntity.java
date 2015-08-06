package cn.com.hzzc.health.pro.abstracts;

/**
 * 
 * @author pang
 * @todo 分析健康信息的能够有的操作
 *
 */
public class ParentShareSentenceEntity {
	/**
	 * 操作类型
	 */
	public static final int NO_OPS = 0;// 无操作
	public static final int OK = 1;// 赞同
	public static final int NO_OK = 2;// 否定
	public static final int COMMENT = 3;// 评论
	public int ops;

	public int getOps() {
		return ops;
	}

	public void setOps(int ops) {
		this.ops = ops;
	}

}
