package cn.com.hzzc.health.pro.topic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.abstracts.ParentTopicCommentActivity;
import cn.com.hzzc.health.pro.task.UploadFileTask;
import cn.com.hzzc.health.pro.task.UploadTopicCommentFileTask;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 添加主题评论
 * @author pang
 *
 */
public class TopicCommentAddActivity extends ParentTopicCommentActivity {

	private EditText topic_comment_content;
	private ProgressBar topic_comment_bar;
	/** 关联主题 ***/
	private String topicId;
	private String topicName;
	/** 初始化的内容 ***/
	String str = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_comment_add);
		topic_comment_content = (EditText) findViewById(R.id.topic_comment_content);
		topic_comment_bar = (ProgressBar) findViewById(R.id.topic_comment_bar);
		gridview = (GridView) findViewById(R.id.topic_comment_gridview);
		adapter = new GridAdapter();
		gridview.setAdapter(adapter);
		initSinglePhotoShow();

		topicId = getIntent().getStringExtra("topicId");
		topicName = getIntent().getStringExtra("topicName");
		str = "#" + topicName + "#";

		// 文本内容
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), 0,
				ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		topic_comment_content.setText(ss);
		topic_comment_content.setSelection(str.length());
	}

	public void backShare(View view) {
		finish();
	}

	@SuppressWarnings("unchecked")
	public void saveShare(View view) {
		String content = topic_comment_content.getText().toString();
		if (content == null || "".equals(content.trim())) {
			cntent_no_alert();
			return;
		}
		String url = SystemConst.server_url + SystemConst.TopicUrl.addTopicPost;
		List<File> files = new ArrayList<File>();
		for (int i = 0; i < selectedPicture.size(); i++) {
			files.add(new File(selectedPicture.get(i)));
		}
		try {
			Map map = new HashMap();

			Map textPram = new HashMap();
			JSONObject obj = new JSONObject();
			obj.put("topicId", topicId);
			obj.put("userId", userId);
			obj.put("comment", content);
			obj.put("replyUserId", "");
			textPram.put(SystemConst.json_param_name, obj.toString());
			map.put(UploadFileTask.text_param, textPram);
			map.put(UploadFileTask.file_param, files);
			new UploadTopicCommentFileTask(TopicCommentAddActivity.this, url)
					.execute(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendSuccess() {
		Intent intent = new Intent(TopicCommentAddActivity.this,
				ShowTopicDetailActivity.class);
		intent.putExtra("hasNew", true);
		intent.putExtra("topicId", topicId);
		startActivity(intent);
		finish();
	}

}
