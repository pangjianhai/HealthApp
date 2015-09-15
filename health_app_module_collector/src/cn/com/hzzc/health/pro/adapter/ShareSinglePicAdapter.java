package cn.com.hzzc.health.pro.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.com.hzzc.health.pro.CommonPicJazzActivity;
import cn.com.hzzc.health.pro.ImagePagerActivity;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.config.HealthApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 查看分享具体详情的时候图片展示
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
	LayoutParams params = new AbsListView.LayoutParams(250, 250);

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
				// Intent it = new Intent(context, CommonPicJazzActivity.class);
				// it.putExtra("imgIdList", (Serializable) imgIdList);
				// it.putExtra("clickImg", imgIdList.get(position));
				// context.startActivity(it);
				ArrayList urls = new ArrayList();
				for (String id : imgIdList) {
					urls.add(id);
				}
				imageBrower(context, position, urls);
			}
		});
		return convertView;
	}

	protected void imageBrower(Context mContext, int position,
			ArrayList<String> urls2) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

}
