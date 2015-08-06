package cn.com.hzzc.health.pro.util;

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
	 * �½��ַ���
	 */
	private StringBuffer mStringBuffer = new StringBuffer("");
	/**
	 * ɨ�����б�
	 */
	public List<ScanResult> listResult = new ArrayList<ScanResult>();
	/**
	 * ���������
	 */
	private ScanResult mScanResult;
	/**
	 * WiFi�������
	 */
	private WifiManager mWifiManager;
	/**
	 * WiFi��Ϣ
	 */
	private WifiInfo mWifiInfo;
	/**
	 * ���������б�
	 */
	private List<WifiConfiguration> wifiConfigList = new ArrayList<WifiConfiguration>();
	/**
	 * ����һ����
	 */
	WifiLock mWifiLock;
	/**
	 * �������ӹ�����
	 */
	private ConnectivityManager connManager;
	private Context mContext;
	/**
	 * WiFi�����б�
	 */
	private List<WifiConfiguration> wifiConfigedSpecifiedList = new ArrayList<WifiConfiguration>();

	private State state;
	/**
	 * �Ƿ��Ѿ�����
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
	 * @date 2015��4��27��
	 * @todo ���»�õ�ǰWiFi������Ϣ
	 * @author pang
	 */
	public void againGetWifiInfo() {
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo �ж��û��Ƿ���WiFi��
	 * @author pang
	 */
	public boolean isNetCardFriendly() {
		return mWifiManager.isWifiEnabled();
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo �жϵ�ǰϵͳ�Ƿ���������WiFi
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
	 * @date 2015��4��27��
	 * @todo �жϵ�ǰϵͳ�Ƿ��Ѿ�������WiFi
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
	 * @date 2015��4��27��
	 * @todo �жϵ�ǰ����״̬
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
	 * @date 2015��4��27��
	 * @todo �������úõ����磨����������粢���ú������룩
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
	 * @date 2015��4��27��
	 * @todo ��ȡWiFi�����б�
	 * @author pang
	 */
	public List<WifiConfiguration> getWifiConfigedSpecifiedList() {
		return wifiConfigedSpecifiedList;
	}

	/**
	 * 
	 * @tags
	 * @date 2015��4��27��
	 * @todo ����
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
	 * @date 2015��4��27��
	 * @todo �ر���
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
	 * @date 2015��4��27��
	 * @todo ɨ���ܱ�����
	 * @author pang
	 */
	public void scan() {
		mWifiManager.startScan();
		listResult = mWifiManager.getScanResults();
		wifiConfigList = mWifiManager.getConfiguredNetworks();
		if (listResult != null) {
			Log.i(TAG, "��ǰ�������������");
		} else {
			Log.i(TAG, "��ǰ����û��������");
		}

	}

	/**
	 * 
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo ����ɨ����
	 * @author pang
	 */
	public List<ScanResult> getListResult() {
		return listResult;
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo �õ�ɨ����
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
	 * @date 2015��4��27��
	 * @todo ����ָ������
	 * @author pang
	 */
	public void connect() {
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * 
	 * @tags
	 * @date 2015��4��27��
	 * @todo �Ͽ���ǰ��������
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
	 * @date 2015��4��27��
	 * @todo ��鵱ǰ����״̬
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
	 * @date 2015��4��27��
	 * @todo ��ȡ����ID
	 * @author pang
	 */
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * ��ȡIP��ַ
	 * 
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo TODO
	 * @author pang
	 */
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	/**
	 * 
	 * @tags
	 * @date 2015��4��27��
	 * @todo ��WiFilock
	 * @author pang
	 */
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	/**
	 * 
	 * @tags
	 * @date 2015��4��27��
	 * @todo ����WiFilock
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
	 * @date 2015��4��27��
	 * @todo ������
	 * @author pang
	 */
	public WifiLock createWifilock() {
		return mWifiManager.createWifiLock("Test");
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo ������úõ�����wpa_supplicant.conf�е����ݣ������Ƿ�����������
	 * @author pang
	 */
	public List<WifiConfiguration> getConfiguration() {
		return wifiConfigList;
	}

	/**
	 * 
	 * @tags @param index
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo ָ�����úõ������������
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
	 * @date 2015��4��27��
	 * @todo ��ȡMac��ַ
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
	 * @date 2015��4��27��
	 * @todo ��ȡWiFiinfo������Ϣ��
	 * @author pang
	 */
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	/**
	 * 
	 * @tags @param wcg
	 * @tags @return
	 * @date 2015��4��27��
	 * @todo ���һ�����粢�� * @author pang
	 */
	public int addNetwork(WifiConfiguration wcg) {
		int wcgId = mWifiManager.addNetwork(wcg);
		mWifiManager.enableNetwork(wcgId, true);
		return wcgId;
	}
}
