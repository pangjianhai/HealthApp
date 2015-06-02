package cn.com.health.pro.adapter;

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
import android.widget.Toast;
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
			ImageView iv = (ImageView) convertView;
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
