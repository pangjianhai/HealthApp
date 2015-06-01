package cn.com.health.pro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pang
 * @todo 文件夹
 *
 */
public class ImageFloder {
	/**
	 * 图片的文件夹路径
	 */
	private String dir;
	/**
	 * 文件夹的名称，通过截取路径DIR获取
	 */
	private String name;

	/**
	 * 第一张图片的路径
	 */
	private String firstImagePath;

	/**
	 * 文件夹下面图片详情的路径 ，一个文件夹有多个图片
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
