package cn.com.hzzc.health.pro.model;

import java.util.List;

/**
 * @todo 发的帖子
 * @author pang
 *
 */
public class TopicPostEntity {

	public static int GOOD_ALREADY = 1;
	public static int GOOD_NO = 0;

	private String id;
	/* 内容简介 */
	private String shortMsg;
	private String rddComment;
	/* 发布人 */
	private String userId;
	private String userName;
	/* 发布时间 */
	private String postDate;
	/* 点赞人数 */
	private int goodNum;
	/* 图片信息 */
	private String img0;
	private String img1;
	private String img2;
	private String img3;
	private List<String> imgs;
	private int isGood;// 是否点赞 没有：0 赞过：1

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortMsg() {
		return shortMsg;
	}

	public void setShortMsg(String shortMsg) {
		this.shortMsg = shortMsg;
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

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public int getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(int goodNum) {
		this.goodNum = goodNum;
	}

	public String getImg0() {
		return img0;
	}

	public void setImg0(String img0) {
		this.img0 = img0;
	}

	public String getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		this.img1 = img1;
	}

	public String getImg2() {
		return img2;
	}

	public void setImg2(String img2) {
		this.img2 = img2;
	}

	public String getImg3() {
		return img3;
	}

	public void setImg3(String img3) {
		this.img3 = img3;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	public int getIsGood() {
		return isGood;
	}

	public void setIsGood(int isGood) {
		this.isGood = isGood;
	}

	public String getRddComment() {
		return rddComment;
	}

	public void setRddComment(String rddComment) {
		this.rddComment = rddComment;
	}

}
