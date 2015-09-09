package cn.com.hzzc.health.pro.topic;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.abstracts.ParentTopicCommentActivity;
import cn.com.hzzc.health.pro.abstracts.ParentTopicCommentActivity.GridAdapter;

/**
 * @todo 添加主题评论
 * @author pang
 *
 */
public class TopicCommentAddActivity extends ParentTopicCommentActivity {

	private EditText topic_comment_content;
	private ProgressBar topic_comment_bar;
	private String topicId;
	private String topicName;

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
		topic_comment_content.setText("#" + topicName + "#");

	}

	public void backShare(View view) {
		finish();
	}

	@SuppressWarnings("unchecked")
	public void saveShare(View view) {
		// String content = share_send_health_content.getText().toString();
		// if (content == null || "".equals(content.trim())) {
		// cntent_no_alert();
		// return;
		// }
		// // if (tags_selected == null || tags_selected.isEmpty()) {
		// // select_no_tag_alert();
		// // return;
		// // }
		// try {
		// Map map = new HashMap();
		//
		// Map textPram = new HashMap();
		// List<File> files = new ArrayList<File>();
		// JSONObject obj = new JSONObject();
		// obj.put("content", share_send_health_content.getText().toString());
		// obj.put("type", SystemConst.ShareInfoType.SHARE_TYPE_HEALTH);
		// obj.put("userId", userId);
		// obj.put("tagId", getSelectedTagIds());
		// textPram.put(SystemConst.json_param_name, obj.toString());
		// for (int i = 0; i < selectedPicture.size(); i++) {
		// files.add(new File(selectedPicture.get(i)));
		// }
		// map.put(UploadFileTask.text_param, textPram);
		// map.put(UploadFileTask.file_param, files);
		// new UploadFileTask(ShareHealthActivity.this, SystemConst.server_url
		// + SystemConst.FunctionUrl.uploadHealthShare).execute(map);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

}
