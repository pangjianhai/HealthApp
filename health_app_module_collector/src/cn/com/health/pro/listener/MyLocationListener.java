package cn.com.health.pro.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * @todo 地理位置变化监听器
 * @author pang
 *
 */
public class MyLocationListener implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
		System.out.println("location--------------");
		if (location != null) {
			String time = location.getTime();
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			String addr = location.getAddrStr();
			System.out.println(time + "--" + lat + "--" + lon + "--" + addr);
		}
	}
}
