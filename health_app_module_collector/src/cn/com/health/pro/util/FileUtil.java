package cn.com.health.pro.util;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.model.VersionEntity;

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
		return pth;
	}

	/**
	 * 
	 * @param data
	 * @return
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:解析最新版本检测结果
	 * @return:VersionEntity
	 */
	public static VersionEntity parseVersionEntity(String data) {
		try {
			JSONObject or_obj = new JSONObject(data);
			JSONObject real_obj = or_obj.getJSONObject("versionEntity");
			if (real_obj.has("versionNum")) {
				String vName = real_obj.getString("versionName");
				String vNum = real_obj.getString("versionNum");
				String vUrl = real_obj.getString("versionUrl");
				VersionEntity ve = new VersionEntity();
				ve.setVersionName(vName);
				ve.setVersionNum(vNum);
				ve.setDownLoadUrl(vUrl);
				return ve;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
