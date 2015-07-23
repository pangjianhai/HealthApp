package cn.com.health.pro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pang
 * @todo 手机图片文件夹
 *
 */
public class ImageFloder {
	/**
	 * 文件夹路径
	 */
	private String dir;
	/**
	 * 文件夹名字
	 */
	private String name;

	/**
	 * 第一章图片的路径
	 */
	private String firstImagePath;

	/**
	 * 所有图片
	 */
	public List<ImageItem> images = new ArrayList<ImageItem>();

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.name = this.dir.substring(lastIndexOf);
	}

	public String getFirstImagePath() {
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath) {
		this.firstImagePath = firstImagePath;
	}

	public String getName() {
		return name;
	}
}
