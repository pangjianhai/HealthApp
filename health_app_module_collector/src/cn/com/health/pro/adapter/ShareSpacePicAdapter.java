package cn.com.health.pro.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.com.health.pro.CommonPicJazzActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 分享信息gridview的适配器
 *
 */
public class ShareSpacePicAdapter extends BaseAdapter {
	/**
	 * 健康分享图片的ID
	 */
	private List<String> imgIdList = new ArrayList();

	/**
	 * 控件上下文
	 */
	private Context context;

	public ShareSpacePicAdapter(Context context, List<String> imgIdList) {
		super();
		this.context = context;
		this.imgIdList = imgIdList;
	}

	/**
	 * 每张图片默认的尺寸
	 */
	LayoutParams params = new AbsListView.LayoutParams(220, 250);

	@Override
	public int getCount() {
		return imgIdList.size();
	}

	@Override
	public Object getItem(int position) {
		return imgIdList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 返回 的是适配器每
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ImageView(context);
			((ImageView) convertView).setScaleType(ScaleType.CENTER_CROP);
			convertView.setLayoutParams(params);
		}
		/**
		 * 构造连接加载图片
		 */
		if (imgIdList.get(position) != null
				&& !"".equals(imgIdList.get(position))) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getShareImgById
					+ "?para={imgId:'" + imgIdList.get(position) + "'}";

			// WindowManager wm = (WindowManager) HealthApplication.getContext()
			// .getSystemService(Context.WINDOW_SERVICE);
			// Display display = wm.getDefaultDisplay();
			// System.out.println(display.getWidth() + "<=====================>"
			// + display.getHeight());
			// Point size = new Point();
			// display.getSize(size);
			// int screenWidth = size.x;
			// int screenHeight = size.y;
			DisplayMetrics metric = new DisplayMetrics();
			WindowManager wm = (WindowManager) HealthApplication.getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(metric);
			int screenWidth = metric.widthPixels;
			int screenHeight = metric.heightPixels;

			float density = metric.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
			int width = (int) (metric.widthPixels * density + 0.5f); // 屏幕宽（px，如：480px）
			int height = (int) (metric.heightPixels * density + 0.5f); // 屏幕高（px，如：800px）

			int use_scree_width = screenWidth - 40 - 30;// 布局文件用去了40，图片gridview的space用去了30
			System.out.println(screenWidth + "-----" + screenHeight);
			System.out.println(width + "<----->" + height);
			ImageView iv = (ImageView) convertView;
			LayoutParams para;
			para = iv.getLayoutParams();
			para.height = use_scree_width / 3;
			para.width = use_scree_width / 3;
			iv.setLayoutParams(para);
			iv.setScaleType(ScaleType.MATRIX);
			iv.setAdjustViewBounds(true);
			/**
			 * 添加option参数，cache到disk
			 */
			ImageLoader.getInstance().displayImage(pic_url, iv,
					HealthApplication.getDisplayImageOption());
		}
		/**
		 * 添加点击事件
		 */
		((ImageView) convertView).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(context, CommonPicJazzActivity.class);
				it.putExtra("imgIdList", (Serializable) imgIdList);
				it.putExtra("clickImg", imgIdList.get(position));
				context.startActivity(it);
			}
		});
		return convertView;
	}
}
