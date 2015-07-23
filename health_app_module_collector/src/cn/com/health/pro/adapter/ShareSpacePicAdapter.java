package cn.com.health.pro.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import cn.com.health.pro.R;
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
	LayoutParams params = new AbsListView.LayoutParams(130, 250);

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
		ImageView inner_image = null;
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) HealthApplication
					.getContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gridview_pic_item, null);

		}
		inner_image = (ImageView) convertView
				.findViewById(R.id.gridview_pic_item_iv);
		/**
		 * 构造连接加载图片
		 */
		if (imgIdList.get(position) != null
				&& !"".equals(imgIdList.get(position))) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getShareImgById
					+ "?para={imgId:'" + imgIdList.get(position) + "'}";

			/**
			 * 添加option参数，cache到disk
			 */
			ImageLoader.getInstance().displayImage(pic_url, inner_image,
					HealthApplication.getDisplayImageOption());
		}
		/**
		 * 添加点击事件
		 */
		inner_image.setOnClickListener(new OnClickListener() {

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
