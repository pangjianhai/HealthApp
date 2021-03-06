package cn.com.hzzc.health.pro.model;

import java.util.Date;

/**
 * @todo 评论entity
 * @author pang
 *
 */
public class CommentEntity {

	private String id;
	// 评论内容
	private String content;
	// 评论日期
	private Date commentDate;

	/**
	 * 评论者信息
	 */
	private String userId;
	private String userName;

	// 回复某人的评论
	private String atUserId;
	private String atUserName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public String getAtUserId() {
		return atUserId;
	}

	public void setAtUserId(String atUserId) {
		this.atUserId = atUserId;
	}

	public String getAtUserName() {
		return atUserName;
	}

	public void setAtUserName(String atUserName) {
		this.atUserName = atUserName;
	}

}
