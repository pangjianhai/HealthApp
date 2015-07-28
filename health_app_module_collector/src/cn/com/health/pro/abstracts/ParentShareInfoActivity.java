package cn.com.health.pro.abstracts;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import cn.com.health.pro.MainPageLayoutSpaceActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.ShareSelectPicActivity;
import cn.com.health.pro.ShareSportsActivity;
import cn.com.health.pro.model.Tag;
import cn.com.health.pro.persist.SharedPreInto;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 分享健康信息的父类
 *
 */
public abstract class ParentShareInfoActivity extends
		ParentShareInfoViewPaperActivity {

	/**
	 * 显示图片的gridview
	 */
	public GridView gridview;

	/**
	 * 存储选择图片的路径
	 */
	public ArrayList<String> selectedPicture = new ArrayList<String>();

	public static final int REQUEST_PICK = 0;

	/**
	 * 适配器
	 */
	public GridAdapter adapter;

	/**
	 * 用户ID
	 */
	public String userId;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		userId = new SharedPreInto(ParentShareInfoActivity.this)
				.getSharedFieldValue("id");
	}

	/**
	 * 
	 * @author pang
	 * @todo 适配器类
	 *
	 */
	public class GridAdapter extends BaseAdapter {

		private Context cx;
		/**
		 * 参数
		 */
		LayoutParams params = new AbsListView.LayoutParams(120, 160);

		@Override
		public int getCount() {
			return selectedPicture.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		/**
		 * 显然图片
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new ImageView(ParentShareInfoActivity.this);
				((ImageView) convertView).setScaleType(ScaleType.CENTER_CROP);
				convertView.setLayoutParams(params);
			}
			ImageLoader.getInstance().displayImage(
					"file://" + selectedPicture.get(position),
					(ImageView) convertView);
			return convertView;
		}

	}

	/**
	 * 
	 * @tags @param view
	 * @date 2015年4月28日
	 * @todo 选择图片
	 * @author pang
	 */
	public void selectPicture(View view) {
		Intent intent = new Intent(this, ShareSelectPicActivity.class);
		intent.putExtra(
				ShareSelectPicActivity.INTENT_SELECTED_PICTURE_FROM_BEGINACTIVITY,
				selectedPicture.size());
		startActivityForResult(intent, REQUEST_PICK);
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			ArrayList<String> newPics = (ArrayList<String>) data
					.getSerializableExtra(ShareSelectPicActivity.INTENT_SELECTED_PICTURE);
			selectedPicture.addAll(newPics);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月28日
	 * @todo 点击结束
	 * @author pang
	 */
	public void backShare(View view) {
		finish();
	}

	/**
	 * ]
	 * 
	 * @tags
	 * @date 2015年5月20日
	 * @todo 上传成功
	 * @author pang
	 */
	public void sendSuccess() {
		Intent intent = new Intent(ParentShareInfoActivity.this,
				MainPageLayoutSpaceActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @return
	 * @user:pang
	 * @data:2015年6月22日
	 * @todo:拼接用户选中的标签
	 * @return:String
	 */
	public String getSelectedTagIds() {
		StringBuffer sb = new StringBuffer("");
		String tags = "";
		if (tags_selected != null && !tags_selected.isEmpty()) {
			for (Tag tag : tags_selected) {
				sb.append(tag.getId()).append(",");
			}
			tags = sb.substring(0, sb.length() - 1);
		}
		return tags;
	}

	public void init() {
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				ImageView showImg = new ImageView(ParentShareInfoActivity.this);
				showImg.setScaleType(ImageView.ScaleType.CENTER);
				showImg.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				File mediaFile = new File(selectedPicture.get(position));
				Uri uri = Uri.fromFile(mediaFile);
				showImg.setImageURI(uri);
				Dialog dialog = new AlertDialog.Builder(ParentShareInfoActivity.this)
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

}
