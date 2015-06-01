package cn.com.health.pro.model;

/**
 * 
 * @author pang TODO 信息库实体bean
 *
 */
public class InfoDoc {

	/**
	 * id
	 */
	private String id;
	/**
	 * 作者
	 */
	private String author;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 好评
	 */
	private String goodNum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}

}
