package cn.com.health.pro;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.health.pro.model.ImageFloder;
import cn.com.health.pro.model.ImageItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 
 * @author pang
 * @todo 查看手机相册及其图片
 *
 */
public class ShareSelectPicActivity extends BaseActivity {
	/**
	 * 最大选择书目
	 */
	private static int MAX_NUM = 8;
	private static final int TAKE_PICTURE = 520;

	public static final String INTENT_MAX_NUM = "intent_max_num";
	public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
	/**
	 * intent
	 */
	public static final String INTENT_SELECTED_PICTURE_FROM_BEGINACTIVITY = "intent_selected_picture_from_beginactivity";
	private int selected_pic_num = 0;

	private Context context;
	private GridView gridview;
	/**
	 * 适配器
	 */
	private PictureAdapter adapter;
	/**
	 * 临时目录
	 */
	private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
	/**
	 * 相册数目
	 */
	private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();

	private ImageLoader loader;
	private DisplayImageOptions options;
	private ContentResolver mContentResolver;
	private Button pic_folder_select, pic_select_ok;
	private ListView listview;
	/**
	 * ���������
	 */
	private FolderAdapter folderAdapter;
	/**
	 * ��ǰѡ�е����
	 */
	private ImageFloder imageAll, currentImageFolder;

	/**
	 * 已经选择的图片路径
	 */
	private ArrayList<String> selectedPicture = new ArrayList<String>();
	private String cameraPath = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_picture);
		MAX_NUM = getIntent().getIntExtra(INTENT_MAX_NUM, 8);
		context = this;
		mContentResolver = getContentResolver();
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		initView();
	}

	public void select(View v) {
		if (listview.getVisibility() == 0) {
			hideListAnimation();
		} else {
			listview.setVisibility(0);
			showListAnimation();
			folderAdapter.notifyDataSetChanged();
		}
	}

	public void showListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1,
				0f);
		ta.setDuration(200);
		listview.startAnimation(ta);
	}

	public void hideListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1,
				1f);
		ta.setDuration(200);
		listview.startAnimation(ta);
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				listview.setVisibility(8);
			}
		});
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年4月24日
	 * @todo 选择确定
	 * @author pang
	 */
	public void ok(View v) {
		Intent data = new Intent();
		data.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
		setResult(RESULT_OK, data);
		this.finish();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月24日
	 * @todo 初始化
	 * @author pang
	 */
	private void initView() {
		/**
		 * 获取上一次已经选择的张数，如果第一次选择则默认是0
		 */
		selected_pic_num = getIntent().getIntExtra(
				INTENT_SELECTED_PICTURE_FROM_BEGINACTIVITY, 0);

		imageAll = new ImageFloder();
		imageAll.setDir("/所有图片");
		currentImageFolder = imageAll;
		mDirPaths.add(imageAll);

		pic_select_ok = (Button) findViewById(R.id.pic_select_ok);
		pic_select_ok.setText("完成" + selected_pic_num + "/" + MAX_NUM);
		pic_folder_select = (Button) findViewById(R.id.pic_folder_select);

		gridview = (GridView) findViewById(R.id.pics_of_one_folder_gridview);
		adapter = new PictureAdapter();
		gridview.setAdapter(adapter);

		/**
		 * 如果是第一张则是打开照相机
		 */
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					goCamare();
				}
			}
		});

		listview = (ListView) findViewById(R.id.pic_folders_listview);
		folderAdapter = new FolderAdapter();
		listview.setAdapter(folderAdapter);
		/**
		 * 选中某一个相册之后的操作
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 将当前相册改变
				 */
				currentImageFolder = mDirPaths.get(position);
				/**
				 * 隐藏相册选择栏目
				 */
				hideListAnimation();
				/**
				 * 更新适配器，根据新相册重新渲染图片列表
				 */
				adapter.notifyDataSetChanged();
				/**
				 * 将新相册的名字修改展示
				 */
				pic_folder_select.setText(currentImageFolder.getName());
			}
		});
		getThumbnail();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年4月24日
	 * @todo 使用相机拍照
	 * @author pang
	 */
	protected void goCamare() {
		if (selectedPicture.size() + 1 > MAX_NUM) {
			Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri imageUri = getOutputMediaFileUri();
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	/**
	 * 
	 * @tags @return
	 * @date 2015年4月24日
	 * @todo 用于拍照时获取输出的Uri
	 * @author pang
	 */
	protected Uri getOutputMediaFileUri() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Night");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		cameraPath = mediaFile.getAbsolutePath();
		return Uri.fromFile(mediaFile);
	}

	/**
	 * 照片拍完后关闭当前activity，返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && cameraPath != null) {
			selectedPicture.add(cameraPath);
			Intent data2 = new Intent();
			data2.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
			setResult(RESULT_OK, data2);
			this.finish();
		}
	}

	public void back(View v) {
		onBackPressed();
	}

	/**
	 * 
	 * @author pang
	 * @todo 相册图片适配器，每次针对一个具体的相册将所有的图片展示出来 所有的适配器都是在接受到notice之后重新执行getView渲染元素
	 */
	class PictureAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return currentImageFolder.images.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.activity_select_picture_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				holder.checkBox = (Button) convertView.findViewById(R.id.check);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.iv
						.setImageResource(R.drawable.pickphotos_to_camera_normal);
				holder.checkBox.setVisibility(View.INVISIBLE);
			} else {
				position = position - 1;
				holder.checkBox.setVisibility(View.VISIBLE);
				final ImageItem item = currentImageFolder.images.get(position);
				/**
				 * 循环加载相册里的每一张图片
				 */
				loader.displayImage("file://" + item.path, holder.iv, options);
				boolean isSelected = selectedPicture.contains(item.path);
				/**
				 * 选中其中的一张图片所做的操作
				 */
				holder.checkBox.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!v.isSelected()
								&& selectedPicture.size() + 1
										+ selected_pic_num > MAX_NUM) {
							Toast.makeText(context, "最多选择" + MAX_NUM + "张",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (selectedPicture.contains(item.path)) {
							selectedPicture.remove(item.path);
						} else {
							selectedPicture.add(item.path);
						}
						pic_select_ok.setEnabled(selectedPicture.size() > 0);
						pic_select_ok.setText("完成"
								+ (selectedPicture.size() + selected_pic_num)
								+ "/" + MAX_NUM);
						v.setSelected(selectedPicture.contains(item.path));
					}
				});
				holder.checkBox.setSelected(isSelected);
			}
			return convertView;
		}
	}

	class ViewHolder {
		ImageView iv;
		Button checkBox;
	}

	/**
	 * @author pang
	 * @todo 点击“所有图片”查看文件夹的时候执行该适配器
	 *
	 */
	class FolderAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDirPaths.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FolderViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.list_dir_item,
						null);
				holder = new FolderViewHolder();
				holder.id_dir_item_image = (ImageView) convertView
						.findViewById(R.id.id_dir_item_image);
				holder.id_dir_item_name = (TextView) convertView
						.findViewById(R.id.id_dir_item_name);
				holder.id_dir_item_count = (TextView) convertView
						.findViewById(R.id.id_dir_item_count);
				holder.choose = (ImageView) convertView
						.findViewById(R.id.choose);
				convertView.setTag(holder);
			} else {
				holder = (FolderViewHolder) convertView.getTag();
			}
			ImageFloder item = mDirPaths.get(position);
			loader.displayImage("file://" + item.getFirstImagePath(),
					holder.id_dir_item_image, options);
			holder.id_dir_item_count.setText(item.images.size() + "张");
			holder.id_dir_item_name.setText(item.getName());
			holder.choose.setVisibility(currentImageFolder == item ? 0 : 8);
			return convertView;
		}
	}

	class FolderViewHolder {
		ImageView id_dir_item_image;
		ImageView choose;
		TextView id_dir_item_name;
		TextView id_dir_item_count;
	}

	/**
	 * 得到所有图片缩略图，第一次初始化的时候调用
	 */
	private void getThumbnail() {
		Cursor mCursor = mContentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.ImageColumns.DATA }, "", null,
				MediaStore.MediaColumns.DATE_ADDED + " DESC");
		if (mCursor.moveToFirst()) {
			int pathColumn = mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			do {
				/**
				 * 获取图片的路径
				 */
				String path = mCursor.getString(pathColumn);
				imageAll.images.add(new ImageItem(path));
				/**
				 * 获取该图片的父路径名
				 */
				File parentFile = new File(path).getParentFile();
				if (parentFile == null) {
					continue;
				}
				ImageFloder imageFloder = null;
				/**
				 * 获取当前图片所属的文件夹
				 */
				String dirPath = parentFile.getAbsolutePath();
				/**
				 * 如果temDir没有装载过该文件夹则构造装载
				 */
				if (!tmpDir.containsKey(dirPath)) {
					imageFloder = new ImageFloder();
					imageFloder.setDir(dirPath);// 文件夹路径
					imageFloder.setFirstImagePath(path);// 文件夹第一张图片路径
					mDirPaths.add(imageFloder);// 将文件夹加入list
					tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));// 将该文件夹路径放入map中防止重复加载
				} else {
					imageFloder = mDirPaths.get(tmpDir.get(dirPath));
				}
				imageFloder.images.add(new ImageItem(path));
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		tmpDir = null;
	}

}
