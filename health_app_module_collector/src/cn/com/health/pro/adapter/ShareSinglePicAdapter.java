package cn.com.health.pro.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import cn.com.health.pro.CommonPicJazzActivity;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 图片展示
 *
 */
public class ShareSinglePicAdapter extends BaseAdapter {

	private List<String> imgIdList;

	/**
	 * 控件上下文
	 */
	private Context context;

	public ShareSinglePicAdapter(Context context, List<String> imgIdList) {
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
		return position;
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
		if (imgIdList.get(position) != null
				&& !"".equals(imgIdList.get(position))) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getShareImgById
					+ "?para={imgId:'" + imgIdList.get(position) + "'}";
			ImageLoader.getInstance().displayImage(pic_url,
					(ImageView) convertView,
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
