package cn.com.hzzc.health.pro.util;

public interface HttpCallbackListener {

	void onFinish(String response);

	void onError(Exception e);
}
