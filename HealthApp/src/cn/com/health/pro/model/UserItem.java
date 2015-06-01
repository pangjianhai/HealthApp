package cn.com.health.pro.model;

/**
 * 
 * @author pang
 * @todo 用户entity
 *
 */
public class UserItem {

	/**
	 * 数据库uuid
	 */
	private String uuid;

	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用户展示的汉子名字
	 */
	private String userName;
	/**
	 * 标签
	 */
	private String tags;
	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 图像
	 */
	private String img;

	/**
	 * 生日
	 */
	private String birthday;

	/**
	 * Email
	 */
	private String email;

	/**
	 * 个人签名
	 */
	private String sentence;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
}
