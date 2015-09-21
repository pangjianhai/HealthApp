package cn.com.hzzc.health.pro.hardware.bluetooth;

import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ReceiveThread extends Service {

	private Socket socket;
	private String workStatus;// 当前工作状况，null表示正在处理，success表示处理成功，failure表示处理失败
	public static Boolean mainThreadFlag = true;; // 状态

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void doListen() {
		Log.d("chl", "doListen()");
		// 开始监听
		while (mainThreadFlag) {
			// 开始监听数据
			new Thread(new ThreadReadWriterSocketServer(ReceiveThread.this,
					socket));
		}
	}

}
