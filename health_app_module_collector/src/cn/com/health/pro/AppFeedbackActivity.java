package cn.com.health.pro;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.com.health.pro.util.FileUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 用户反馈
 * @author pang
 *
 */
public class AppFeedbackActivity extends BaseActivity {

	/**
	 * 意见反馈
	 */
	private EditText feed_back_text;
	/**
	 * 提交按钮
	 */
	private Button feedback_btn;

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
		feed_back_text = (EditText) findViewById(R.id.feed_back_text);
		feedback_btn = (Button) findViewById(R.id.feedback_btn);
		feedback_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitFeedback();
			}
		});
	}

	private void submitFeedback() {
		try {
			String content = feed_back_text.getText().toString();
			if (content == null || "".equals(content)) {
				afterFeedback("反馈意见不能为空");
				return;
			}
			JSONObject d = new JSONObject();
			d.put("userUuId", userId);
			d.put("content", content);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					System.out.println("---data:" + data);
					boolean isc = FileUtil.ifFeedbackSuccess(data);
					if (isc) {
						afterFeedback("反馈成功，谢谢\n欢迎参与平台讨论组\n一起打造您的专属体验");
					} else {
						afterFeedback("反馈失败\n请重新操作");
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.user_add_view, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param content
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:TODO
	 * @return:void
	 */
	private void afterFeedback(String content) {
		new AlertDialog.Builder(AppFeedbackActivity.this).setTitle("反馈提示")
				.setIcon(R.drawable.feedback0).setMessage(content)
				.setPositiveButton("确定", null).show();
	}

	/**
	 * @param v
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:反馈
	 * @return:void
	 */
	public void login_back(View v) {
		this.finish();
	}

}
