package cn.com.hzzc.health.pro.model;

/**
 * 
 * @author pang
 * @todo 用户entity
 *
 */
public class CollectionItem {

	/**
	 * 分享实体ID
	 */
	private String id;
	/**
	 * 分享类型
	 */
	private String type;

	/**
	 * 分享标题
	 */
	private String title;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
