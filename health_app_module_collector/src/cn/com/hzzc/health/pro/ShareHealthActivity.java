package cn.com.hzzc.health.pro;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import cn.com.hzzc.health.pro.abstracts.ParentShareInfoActivity;
import cn.com.hzzc.health.pro.task.UploadFileTask;

/**
 * 
 * @author pang
 * @todo 健康信息activity
 *
 */
public class ShareHealthActivity extends ParentShareInfoActivity {

	private EditText share_send_health_content;
	private ProgressBar share_health_bar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gridview = (GridView) leftView.findViewById(R.id.share_health_gridview);
		share_send_health_content = (EditText) leftView
				.findViewById(R.id.share_send_health_content);
		adapter = new GridAdapter();
		gridview.setAdapter(adapter);

		share_health_bar = (ProgressBar) leftView
				.findViewById(R.id.share_health_bar);
		initSinglePhotoShow();
	}

	/**
	 * 
	 * @param id
	 * @user:pang
	 * @data:2015年6月21日
	 * @todo:设置左边的分享
	 * @return:void
	 */
	public void setLeftViewId(int id) {
		this.leftViewId = R.layout.share_send_health;
	}

	@SuppressWarnings("unchecked")
	public void saveShare(View view) {
		if (tags_selected == null || tags_selected.isEmpty()) {
			select_no_tag_alert();
			return;
		}
		try {
			Map map = new HashMap();

			Map textPram = new HashMap();
			List<File> files = new ArrayList<File>();
			JSONObject obj = new JSONObject();
			obj.put("content", share_send_health_content.getText().toString());
			obj.put("type", SystemConst.ShareInfoType.SHARE_TYPE_HEALTH);
			obj.put("userId", userId);
			obj.put("tagId", getSelectedTagIds());
			textPram.put(SystemConst.json_param_name, obj.toString());
			for (int i = 0; i < selectedPicture.size(); i++) {
				files.add(new File(selectedPicture.get(i)));
			}
			map.put(UploadFileTask.text_param, textPram);
			map.put(UploadFileTask.file_param, files);
			new UploadFileTask(ShareHealthActivity.this, SystemConst.server_url
					+ SystemConst.FunctionUrl.uploadHealthShare).execute(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
