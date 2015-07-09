package cn.com.health.pro;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import cn.com.health.pro.util.LoginUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/**
 * @todo 用户反馈
 * @author pang
 *
 */
public class AppFeedbackActivity extends BaseActivity {

	private EditText login_user_edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_feedback);
		init();
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年7月9日
	 * @todo:初始化组件
	 * @return:void
	 */
	private void init() {
		// login_user_edit = (EditText) findViewById(R.id.login_user_edit);
	}

	public void login_back(View v) {
		this.finish();
	}

}
