package cn.com.hzzc.health.pro.model;

/**
 * @todo 主题entity
 * @author pang
 *
 */
public class TopicEntity {

	private String id;

	private String name;

	private int userNum;

	private int postNum;

	private String imgId;

	private String desc;

	private String createDate;
	/**
	 * 是否已经参与
	 */
	private boolean isEnter;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getPostNum() {
		return postNum;
	}

	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public boolean isEnter() {
		return isEnter;
	}

	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
	}

}
