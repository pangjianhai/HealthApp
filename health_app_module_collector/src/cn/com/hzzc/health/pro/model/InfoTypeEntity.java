package cn.com.hzzc.health.pro.model;

/**
 * @TODO 文档库类型
 * @author pang
 *
 */
public class InfoTypeEntity {

	// 分类ID
	private String id;
	// 分类名字
	private String name;
	// 是否已经关注
	private String ifFocus;// Y || N

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

	public String getIfFocus() {
		return ifFocus;
	}

	public void setIfFocus(String ifFocus) {
		this.ifFocus = ifFocus;
	}

}
