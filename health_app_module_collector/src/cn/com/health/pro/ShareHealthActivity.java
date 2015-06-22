package cn.com.health.pro;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.health.pro.abstracts.ParentShareInfoActivity;
import cn.com.health.pro.task.UploadFileTask;

import com.nostra13.universalimageloader.core.ImageLoader;

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
		init();
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

	public void init() {
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				ImageView showImg = new ImageView(ShareHealthActivity.this);
				showImg.setScaleType(ImageView.ScaleType.CENTER);
				showImg.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				File mediaFile = new File(selectedPicture.get(position));
				Uri uri = Uri.fromFile(mediaFile);
				showImg.setImageURI(uri);
				Dialog dialog = new AlertDialog.Builder(
						ShareHealthActivity.this)
						.setIcon(R.drawable.ic_back_light)
						.setTitle("查看图片")
						.setView(showImg)
						.setNegativeButton("删除",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										selectedPicture.remove(selectedPicture
												.get(position));
										adapter.notifyDataSetChanged();
									}
								})
						.setPositiveButton("关闭",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create();
				// dialog.setContentView(R.layout.picture_dialog_layout);
				Window dialogWindow = dialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				lp.x = 100; // 新位置X坐标
				lp.y = 100; // 新位置Y坐标
				lp.width = 300; // 宽度
				lp.height = 500; // 高度
				lp.alpha = 0.7f; // 透明度
				dialogWindow.setAttributes(lp);
				dialog.show();

			}
		});
	}

	@SuppressWarnings("unchecked")
	public void saveShare(View view) {
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
