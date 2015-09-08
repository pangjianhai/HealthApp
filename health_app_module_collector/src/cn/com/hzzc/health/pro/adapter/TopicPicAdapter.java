package cn.com.hzzc.health.pro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.com.hzzc.health.pro.ImagePagerActivity;
import cn.com.hzzc.health.pro.ImageTopicPagerActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.config.HealthApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 健康主题评论gridview的适配器
 *
 */
public class TopicPicAdapter extends BaseAdapter {
	/**
	 * 健康分享图片的ID
	 */
	private List<String> imgIdList = new ArrayList();

	/**
	 * 控件上下文
	 */
	private Context context;

	public TopicPicAdapter(Context context, List<String> imgIdList) {
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
		LayoutParams params = inner_image.getLayoutParams();
		params.height = 250;
		params.width = 250;
		inner_image.setScaleType(ScaleType.CENTER_CROP);
		inner_image.setLayoutParams(params);
		/**
		 * 构造连接加载图片
		 */
		if (imgIdList.get(position) != null
				&& !"".equals(imgIdList.get(position))) {
			String pic_url = SystemConst.server_url
					+ SystemConst.TopicUrl.getTopicPostImgById
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
		Intent intent = new Intent(mContext, ImageTopicPagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
}
