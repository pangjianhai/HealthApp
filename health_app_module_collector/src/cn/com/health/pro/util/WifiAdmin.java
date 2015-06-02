package cn.com.health.pro.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

public class WifiAdmin {

	private final static String TAG = "wifiAdmin";

	/**
	 * 新建字符串缓存
	 */
	private StringBuffer mStringBuffer = new StringBuffer("");
	/**
	 * 扫描结果列表
	 */
	public List<ScanResult> listResult = new ArrayList<ScanResult>();
	/**
	 * 创建结果类
	 */
	private ScanResult mScanResult;
	/**
	 * WiFi管理对象
	 */
	private WifiManager mWifiManager;
	/**
	 * WiFi信息
	 */
	private WifiInfo mWifiInfo;
	/**
	 * 网络连接列表
	 */
	private List<WifiConfiguration> wifiConfigList = new ArrayList<WifiConfiguration>();
	/**
	 * 创建一个锁
	 */
	WifiLock mWifiLock;
	/**
	 * 创建连接管理器
	 */
	private ConnectivityManager connManager;
	private Context mContext;
	/**
	 * WiFi配置列表
	 */
	private List<WifiConfiguration> wifiConfigedSpecifiedList = new ArrayList<WifiConfiguration>();

	private State state;
	/**
	 * 是否已经连接
	 */
	private boolean isConnectioned = false;

	public WifiAdmin(Context context) {
		mContext = context;
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
		connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 重新获得当前WiFi连接信息
	 * @author pang
	 */
	public void againGetWifiInfo() {
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 判断用户是否开启WiFi网卡
	 * @author pang
	 */
	public boolean isNetCardFriendly() {
		return mWifiManager.isWifiEnabled();
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 判断当前系统是否正在连上WiFi
	 * @author pang
	 */
	public boolean isConnectioning() {
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (State.CONNECTING == state) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 判断当前系统是否已经连接上WiFi
	 * @author pang
	 */
	public boolean isConnectioned() {
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (State.CONNECTED == state) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 判断当前网络状态
	 * @author pang
	 */
	public State getCurrentState() {
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		return state;
	}

	/**
	 * 
	 * @tags @param ssid
	 * @date 2015年4月27日
	 * @todo 设置配置好的网络（有密码的网络并配置好了密码）
	 * @author pang
	 */
	public void setWifiConfigedSpecifiedList(String ssid) {
		wifiConfigedSpecifiedList.clear();
		if (wifiConfigList != null) {
			for (WifiConfiguration item : wifiConfigList) {
				if (item.SSID.equalsIgnoreCase("\"" + ssid + "\"")
						&& item.preSharedKey != null) {
					wifiConfigedSpecifiedList.add(item);
				}
			}
		}
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 获取WiFi设置列表
	 * @author pang
	 */
	public List<WifiConfiguration> getWifiConfigedSpecifiedList() {
		return wifiConfigedSpecifiedList;
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 打开网卡
	 * @author pang
	 */
	public void openNetCard() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 关闭网卡
	 * @author pang
	 */
	public void closeNetCard() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 扫描周边网络
	 * @author pang
	 */
	public void scan() {
		mWifiManager.startScan();
		listResult = mWifiManager.getScanResults();
		wifiConfigList = mWifiManager.getConfiguredNetworks();
		if (listResult != null) {
			Log.i(TAG, "当前区域存在无线网");
		} else {
			Log.i(TAG, "当前区域没有无线网");
		}

	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 返回扫描结果
	 * @author pang
	 */
	public List<ScanResult> getListResult() {
		return listResult;
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 得到扫描结果
	 * @author pang
	 */
	public String getScanResult() {
		if (mStringBuffer != null) {
			mStringBuffer = new StringBuffer();
		}
		scan();
		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			for (int i = 0; i < listResult.size(); i++) {
				mScanResult = listResult.get(i);
				mStringBuffer = mStringBuffer.append("NO.").append(i + 1)
						.append(":").append(mScanResult.SSID).append("->")
						.append(mScanResult.BSSID).append("->")
						.append(mScanResult.capabilities).append("->")
						.append(mScanResult.frequency).append("->")
						.append(mScanResult.level).append("->")
						.append(mScanResult.describeContents()).append("\n\n");
			}
			Log.i(TAG, mStringBuffer.toString());
		}
		return mStringBuffer.toString();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 连接指定网络
	 * @author pang
	 */
	public void connect() {
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 断开当前网络连接
	 * @author pang
	 */
	public void disconnectWifi() {
		int netId = getNetworkId();
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
		mWifiInfo = null;
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 检查当前网络状态
	 * @author pang
	 */
	public boolean checkNetWorkState() {
		if (mWifiInfo != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @tags
	 * @date 2015年4月27日
	 * @todo 获取连接ID
	 * @author pang
	 */
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * 获取IP地址
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo TODO
	 * @author pang
	 */
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 锁定WiFilock
	 * @author pang
	 */
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月27日
	 * @todo 解锁WiFilock
	 * @author pang
	 */
	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 创建锁
	 * @author pang
	 */
	public WifiLock createWifilock() {
		return mWifiManager.createWifiLock("Test");
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 获得配置好的网络wpa_supplicant.conf中的内容，不论是否有配置密码
	 * @author pang
	 */
	public List<WifiConfiguration> getConfiguration() {
		return wifiConfigList;
	}

	/**
	 * 
	 * @tags @param index
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 指定配置好的网络进行连接
	 * @author pang
	 */
	public boolean connectConfiguration(int index) {
		if (index >= wifiConfigList.size()) {
			return false;
		} else {
			return mWifiManager.enableNetwork(
					wifiConfigedSpecifiedList.get(index).networkId, true);
		}
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 获取Mac地址
	 * @author pang
	 */
	public String getMacAddress() {
		return (mWifiInfo == null) ? "" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "" : mWifiInfo.getBSSID();
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 获取WiFiinfo所有信息包
	 * @author pang
	 */
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	/**
	 * 
	 * @tags @param wcg
	 * @tags @return
	 * @date 2015年4月27日
	 * @todo 添加一个网络并连 * @author pang
	 */
	public int addNetwork(WifiConfiguration wcg) {
		int wcgId = mWifiManager.addNetwork(wcg);
		mWifiManager.enableNetwork(wcgId, true);
		return wcgId;
	}
}
