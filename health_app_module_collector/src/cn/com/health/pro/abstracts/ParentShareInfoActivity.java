package cn.com.health.pro.abstracts;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.com.health.pro.AppLoginLoadingActivity;
import cn.com.health.pro.BaseActivity;
import cn.com.health.pro.MainPageLayoutSpaceActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.ShareSelectPicActivity;
import cn.com.health.pro.persist.SharedPreInto;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 分享健康信息的父类
 *
 */
public abstract class ParentShareInfoActivity extends BaseActivity {

	/**
	 * 标签
	 */
	public String tags = "";

	/**
	 * 待填写的标签
	 */
	public EditText tag1, tag2, tag3, tag4;

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
		LayoutParams params = new AbsListView.LayoutParams(220, 250);

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

	public void initTags(View view) {
		tag1 = (EditText) view.findViewById(R.id.tag1);
		tag2 = (EditText) view.findViewById(R.id.tag2);
		tag3 = (EditText) view.findViewById(R.id.tag3);
		tag4 = (EditText) view.findViewById(R.id.tag4);
		if (tags != null && !"".equals(tags)) {
			String[] ts = tags.split(",");
			for (int i = 0; i < ts.length; i++) {
				String value = ts[i];
				if (i == 0) {
					tag1.setText(value);
				} else if (i == 1) {
					tag2.setText(value);
				} else if (i == 2) {
					tag3.setText(value);
				} else if (i == 3) {
					tag4.setText(value);
				}
			}
		}
	}

	/**
	 * 
	 * @tags @param view
	 * @date 2015年4月28日
	 * @todo 添加标签
	 * @author pang
	 */
	public void shareAddTags(View view) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.share_tags_dialog,
				null);
		initTags(textEntryView);
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("填写标签")
				.setView(textEntryView)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String tag_text_1 = tag1.getText().toString();
						String tag_text_2 = tag2.getText().toString();
						String tag_text_3 = tag3.getText().toString();
						String tag_text_4 = tag4.getText().toString();
						if (tag_text_1 != null && !"".equals(tag_text_1)) {
							tags = tag_text_1;
						}
						if (tag_text_2 != null && !"".equals(tag_text_2)) {
							tags = tags + "," + tag_text_2;
						}
						if (tag_text_3 != null && !"".equals(tag_text_3)) {
							tags = tags + "," + tag_text_3;
						}
						if (tag_text_4 != null && !"".equals(tag_text_4)) {
							tags = tags + "," + tag_text_4;
						}
						displayTags();
					}
				})
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
		dialog.show();
	}

	public abstract void displayTags();

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
}
