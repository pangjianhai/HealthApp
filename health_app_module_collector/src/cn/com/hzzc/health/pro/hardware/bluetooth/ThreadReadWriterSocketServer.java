package cn.com.hzzc.health.pro.hardware.bluetooth;

import java.net.Socket;

import android.content.Context;

public class ThreadReadWriterSocketServer implements Runnable {
	private Socket client = null;
	private Context context = null;

	public ThreadReadWriterSocketServer(Context context, Socket client) {
		this.context = context;
		this.client = client;
	}

	@Override
	public void run() {
		Receive();
	}

	private void Receive() {
	}
	// 处理

}
