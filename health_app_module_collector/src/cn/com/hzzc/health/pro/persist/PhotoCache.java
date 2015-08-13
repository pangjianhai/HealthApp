package cn.com.hzzc.health.pro.persist;

import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.config.HealthApplication;

import com.nostra13.universalimageloader.utils.DiskCacheUtils;

/**
 * @todo 用户头像缓存工具
 * @author pang
 *
 */
public class PhotoCache {

	public static boolean delUserPhotoCache(String userId) {
		String pic_url = SystemConst.server_url
				+ SystemConst.FunctionUrl.getHeadImgByUserId
				+ "?para={userId:'" + userId + "'}";
		/*** 保存成功后，清空本地缓存的用户图片 ***/
		return DiskCacheUtils.removeFromCache(pic_url,
				HealthApplication.getImageDiskCache());
	}
}
