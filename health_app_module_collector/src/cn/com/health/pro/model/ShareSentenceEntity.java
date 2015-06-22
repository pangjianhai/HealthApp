package cn.com.health.pro.model;

import java.util.Date;
import java.util.List;

import cn.com.health.pro.abstracts.ParentShareSentenceEntity;

/**
 * 
 * @author pang
 * @todo 分享内容bean
 *
 */
public class ShareSentenceEntity extends ParentShareSentenceEntity {
	/**
	 * 通用字段
	 */

	private String id;

	/**
	 * 分享类型
	 */
	private String type;
	/**
	 * 作者ID
	 */
	private String userId;
	/**
	 * 作者
	 */
	private String author;

	/**
	 * 分享内容
	 */
	private String content;
	/**
	 * 分享图片
	 */
	private List<String> imgsIds;
	/**
	 * 阅读人数
	 */
	private String readNum;
	/**
	 * 点赞人数
	 */
	private String goodNum;
	/**
	 * 差评人数
	 */
	private String badNum;

	/**
	 * 评论人数
	 */
	private String commentNum;

	/**
	 * 图片的ID
	 */
	private String img0, img1, img2, img3, img4, img5, img6, img7;
	/**
	 * 创建日期
	 */
	private Date createDate;

	private String cDate;

	/**
	 * 个性字段
	 * 
	 * @tags @return
	 * @date 2015年5月6日
	 * @todo TODO
	 * @author pang
	 */
	/**
	 * 材料
	 */
	private String material;
	/**
	 * 功能
	 */
	private String function;

	/**
	 * 标签
	 */
	private String tags;

	public ShareSentenceEntity() {
		super();
		this.ops = ParentShareSentenceEntity.NO_OPS;
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}

	public String getBadNum() {
		return badNum;
	}

	public void setBadNum(String badNum) {
		this.badNum = badNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImgsIds() {
		return imgsIds;
	}

	public void setImgsIds(List<String> imgsIds) {
		this.imgsIds = imgsIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getImg4() {
		return img4;
	}

	public void setImg4(String img4) {
		this.img4 = img4;
	}

	public String getImg5() {
		return img5;
	}

	public void setImg5(String img5) {
		this.img5 = img5;
	}

	public String getImg6() {
		return img6;
	}

	public void setImg6(String img6) {
		this.img6 = img6;
	}

	public String getImg7() {
		return img7;
	}

	public void setImg7(String img7) {
		this.img7 = img7;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
