package cn.com.health.pro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pang
 * @todo �ļ���
 *
 */
public class ImageFloder {
	/**
	 * ͼƬ���ļ���·��
	 */
	private String dir;
	/**
	 * �ļ��е����ƣ�ͨ����ȡ·��DIR��ȡ
	 */
	private String name;

	/**
	 * ��һ��ͼƬ��·��
	 */
	private String firstImagePath;

	/**
	 * �ļ�������ͼƬ�����·�� ��һ���ļ����ж��ͼƬ
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
