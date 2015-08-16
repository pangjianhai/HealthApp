package cn.com.hzzc.health.pro.model;

import java.util.Date;

/**
 * @todo 评论entity
 * @author pang
 *
 */
public class CommentEntity {

	private String id;
	private String content;
	private Date commentDate;

	/**
	 * 评论者信息
	 */
	private String userId;
	private String userName;

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

}
