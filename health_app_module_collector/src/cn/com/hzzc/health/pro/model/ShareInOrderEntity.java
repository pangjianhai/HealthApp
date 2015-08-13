package cn.com.hzzc.health.pro.model;

import java.util.Date;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="ShareInOrderEntity")
public class ShareInOrderEntity {

	/**
	 * 本地数据库存储所属ID
	 */
	@Id
	private String localCacheId;

	/**
	 * 本地数据库存储日期——当前分享属于某一天的榜单
	 */
	@Column
	private String localCacheBelongTopDate;

	/**
	 * 通用字段
	 */
	@Column
	private String id;

	/**
	 * 分享类型
	 */
	@Column
	private String type;
	/**
	 * 作者ID
	 */
	@Column
	private String userId;
	/**
	 * 作者
	 */
	@Column
	private String author;

	/**
	 * 分享内容
	 */
	@Column
	private String content;

	/**
	 * 阅读人数
	 */
	@Column
	private String readNum;
	/**
	 * 点赞人数
	 */
	@Column
	private String goodNum;
	/**
	 * 差评人数
	 */
	@Column
	private String badNum;

	/**
	 * 评论人数
	 */
	@Column
	private String commentNum;

	/**
	 * 图片的ID
	 */
	@Column
	private String img0, img1, img2, img3, img4, img5, img6, img7;
	/**
	 * 创建日期
	 */
	@Column
	private Date createDate;
	@Column
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
	@Column
	private String material;
	/**
	 * 功能
	 */
	@Column
	private String function;

	/**
	 * 标签
	 */
	@Column
	private String tags;

	public String getLocalCacheId() {
		return localCacheId;
	}

	public void setLocalCacheId(String localCacheId) {
		this.localCacheId = localCacheId;
	}

	public String getLocalCacheBelongTopDate() {
		return localCacheBelongTopDate;
	}

	public void setLocalCacheBelongTopDate(String localCacheBelongTopDate) {
		this.localCacheBelongTopDate = localCacheBelongTopDate;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
