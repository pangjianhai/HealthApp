package cn.com.hzzc.health.pro;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.persist.SharedPreInto;
import cn.com.hzzc.health.pro.task.EditUserInfoTask;
import cn.com.hzzc.health.pro.task.GetOneUserForEditAsyncTask;
import cn.com.hzzc.health.pro.task.UploadFileTask;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 编辑个人信息
 *
 */
public class EditUserInfoDetail extends BaseActivity {

	/**
	 * 常量
	 */
	private static final int TAKE_PHOTO = 0;
	private static final int CROP_PHOTO = 1;

	/**
	 * 是否需要上传图片
	 */
	private boolean if_need_upload_photo = false;

	/**
	 * 
	 */
	private Uri imgUri = null;

	/**
	 * 保存字段
	 */
	private EditText edit_user_nickname, edit_user_sex, edit_user_birthday,
			edit_user_signiture;
	private TextView edit_user_id;
	private ImageView edit_user_header;
	/**
	 * 编辑当前用户ID
	 */
	private String uuid;

	/**
	 * 头像路径
	 */
	private String photo_path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_info_detail_edit);
		init();

	}

	private void init() {
		edit_user_header = (ImageView) findViewById(R.id.edit_user_header);
		edit_user_id = (TextView) findViewById(R.id.edit_user_id);
		edit_user_nickname = (EditText) findViewById(R.id.edit_user_nickname);
		edit_user_sex = (EditText) findViewById(R.id.edit_user_sex);
		edit_user_birthday = (EditText) findViewById(R.id.edit_user_birthday);
		edit_user_signiture = (EditText) findViewById(R.id.edit_user_signiture);
		edit_user_sex.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							EditUserInfoDetail.this);
					builder.setIcon(R.drawable.sex_sel3);
					builder.setTitle("请选择性别");
					final String[] sex = { "男", "女" };
					builder.setSingleChoiceItems(sex, 1,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									edit_user_sex.setText(sex[which]);
								}
							});
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									edit_user_sex.clearFocus();
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									edit_user_sex.clearFocus();
								}
							});
					builder.show();
				}
			}
		});
		edit_user_birthday
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							if (imm.isActive()) {
								imm.hideSoftInputFromWindow(
										v.getApplicationWindowToken(), 0);

							}
							Dialog dg = new DatePickerDialog(
									EditUserInfoDetail.this,
									new DatePickerDialog.OnDateSetListener() {

										/**
										 * 点击选择日期之后
										 */
										@Override
										public void onDateSet(DatePicker view,
												int year, int monthOfYear,
												int dayOfMonth) {
											monthOfYear = monthOfYear + 1;
											edit_user_birthday.setText(year
													+ "-" + monthOfYear + "-"
													+ dayOfMonth);
											edit_user_birthday.clearFocus();
										}
									}, 1988, 1, 3);
							dg.show();
						} else {
						}
					}
				});
		Intent intent = getIntent();
		uuid = intent.getStringExtra("uuid");
		try {
			JSONObject data = new JSONObject();
			data.put("Id", uuid);
			new GetOneUserForEditAsyncTask(EditUserInfoDetail.this)
					.execute(data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getOver(UserItem ui) {
		edit_user_id.setText(ui.getUserId());
		edit_user_nickname.setText(ui.getUserName());
		String sex = ui.getSex();
		if ("0".equals(sex)) {
			edit_user_sex.setText("女");
		} else if ("1".equals(sex)) {
			edit_user_sex.setText("男");
		}
		edit_user_birthday.setText(ui.getBirthday());
		edit_user_signiture.setText(ui.getSentence());

		// 初始化头像
		String imgId = ui.getImg();
		if (imgId != null && !"".equals(imgId)) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getHeadImgById
					+ "?para={headImg:'" + imgId + "'}";
			ImageLoader.getInstance().displayImage(pic_url, edit_user_header);
		} else {
			String imageUri = "drawable://" + R.drawable.default_head1;
			ImageLoader.getInstance().displayImage(imageUri, edit_user_header);
		}
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月13日
	 * @todo 打开手机相机
	 * @author pang
	 */
	public void changePhoto(View v) {
		File outputImg = new File(Environment.getExternalStorageDirectory(),
				"fs_health_app_header.jpg");
		try {
			if (outputImg.exists()) {
				outputImg.delete();
			}
			outputImg.createNewFile();
			photo_path = outputImg.getAbsolutePath();
			imgUri = Uri.fromFile(outputImg);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
			startActivityForResult(intent, TAKE_PHOTO);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 拍照或者裁剪相片
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imgUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
				intent.putExtra("aspectX", 1);// 设置剪切框1:1比例的效果
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 200);
				intent.putExtra("outputY", 200);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CROP_PHOTO);

			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Bitmap bm = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(imgUri));
					edit_user_header.setImageBitmap(bm);
					if_need_upload_photo = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		default:
			break;
		}
	}

	/**
	 * 
	 * @tags @param view
	 * @date 2015年5月13日
	 * @todo 保存信息
	 * @author pang
	 */
	@SuppressWarnings("unchecked")
	public void save_userinfo(View view) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();

			Map<String, String> textPram = new HashMap<String, String>();
			List<File> files = new ArrayList<File>();
			JSONObject obj = new JSONObject();
			obj.put("id", uuid);
			obj.put("username", edit_user_nickname.getText().toString());
			String sex_str = edit_user_sex.getText().toString();
			String sex = "";
			if ("女".equals(sex_str)) {
				sex = "0";
			} else if ("男".equals(sex_str)) {
				sex = "1";
			}
			obj.put("sex", sex);
			obj.put("address", "address");
			obj.put("sentence", edit_user_signiture.getText().toString());
			obj.put("birthday", edit_user_birthday.getText().toString());
			textPram.put(SystemConst.json_param_name, obj.toString());
			if (if_need_upload_photo) {
				files.add(new File(photo_path));
			}

			map.put(UploadFileTask.text_param, textPram);
			map.put(UploadFileTask.file_param, files);
			new EditUserInfoTask(EditUserInfoDetail.this,
					SystemConst.server_url
							+ SystemConst.FunctionUrl.editUserById)
					.execute(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:保存成功
	 * @return:void
	 */
	public void afterSaveInfoSuccess() {
		final SharedPreInto spi = new SharedPreInto(this);
		/**
		 * 保存成功后，清空本地缓存的用户信息
		 */
		spi.unvalidUserItem();
		btn_back(null);
	}

	public void btn_back(View view) {
		Intent intent = new Intent(EditUserInfoDetail.this,
				ShowUserInfoDetail.class);
		intent.putExtra("uuid", uuid);
		startActivity(intent);
		this.finish();
	}
}