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
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.com.health.pro.abstracts.ParentShareInfoActivity;
import cn.com.health.pro.task.UploadFileTask;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 健康饮食activity
 *
 */
public class ShareEatActivity extends ParentShareInfoActivity {
	private EditText share_eat_material, share_eat_function,
			share_send_health_content;
	private ProgressBar share_health_bar;
	/**
	 * ��ǩ
	 */
	private TextView share_send_eat_all_tags;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_send_eating);
		gridview = (GridView) findViewById(R.id.share_eat_gridview);

		share_eat_material = (EditText) findViewById(R.id.share_eat_material);
		share_eat_function = (EditText) findViewById(R.id.share_eat_function);
		share_send_health_content = (EditText) findViewById(R.id.share_send_eat_content);

		share_send_eat_all_tags = (TextView) findViewById(R.id.share_send_eat_all_tags);
		adapter = new GridAdapter();
		gridview.setAdapter(adapter);

		share_health_bar = (ProgressBar) findViewById(R.id.share_eat_bar);
		init();
	}

	public void init() {
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				ImageView showImg = new ImageView(ShareEatActivity.this);
				showImg.setScaleType(ImageView.ScaleType.CENTER);
				showImg.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				File mediaFile = new File(selectedPicture.get(position));
				Uri uri = Uri.fromFile(mediaFile);
				showImg.setImageURI(uri);
				Dialog dialog = new AlertDialog.Builder(ShareEatActivity.this)
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
			// share_health_bar.setVisibility(View.VISIBLE);
			Map map = new HashMap();

			Map textPram = new HashMap();
			List<File> files = new ArrayList<File>();
			JSONObject obj = new JSONObject();
			obj.put("material", share_eat_material.getText().toString());
			obj.put("function", share_eat_function.getText().toString());
			obj.put("content", share_send_health_content.getText().toString());
			obj.put("type", SystemConst.ShareInfoType.SHARE_TYPE_FOOD);
			obj.put("userId", userId);
			obj.put("tags", tags);
			textPram.put(SystemConst.json_param_name, obj.toString());
			for (int i = 0; i < selectedPicture.size(); i++) {
				files.add(new File(selectedPicture.get(i)));
			}
			map.put(UploadFileTask.text_param, textPram);
			map.put(UploadFileTask.file_param, files);
			new UploadFileTask(ShareEatActivity.this, SystemConst.server_url
					+ SystemConst.FunctionUrl.uploadHealthShare).execute(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// share_health_bar.setVisibility(View.GONE);
		}

	}

	@Override
	public void displayTags() {
		share_send_eat_all_tags.setText("标签：" + tags);

	}
}
