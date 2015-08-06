package cn.com.hzzc.health.pro.model;

/**
 * @todo app版本entity
 * @author pang
 *
 */
public class VersionEntity {

	/**
	 * 版本名称
	 */
	private String versionName;

	/**
	 * 版本号
	 */
	private String versionNum;

	/**
	 * 下载链接
	 */
	private String downLoadUrl;

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public String getDownLoadUrl() {
		return downLoadUrl;
	}

	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}

}
