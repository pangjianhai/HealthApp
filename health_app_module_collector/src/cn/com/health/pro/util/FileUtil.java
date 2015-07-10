package cn.com.health.pro.util;

import java.io.File;

import android.os.Environment;
import cn.com.health.pro.SystemConst;

public class FileUtil {

	/**
	 * @return
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:获取app下载后的存放路径
	 * @return:String
	 */
	public static String getDownloadFileDir() {
		File p = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		String parent = p.getParent();
		String pth = parent + "/"
				+ SystemConst.mobile_local_dir_for_download_app;
		System.out.println("---->pth:" + pth);
		return pth;
	}
}
