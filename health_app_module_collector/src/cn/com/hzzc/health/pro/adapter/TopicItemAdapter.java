package cn.com.hzzc.health.pro.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;

/**
 * 空间健康分享适配
 */
public class TopicItemAdapter extends BaseAdapter {
	private List<TopicEntity> dataSourceList = new ArrayList<TopicEntity>();
	private HolderView holder;
	private Context context;
	private IShareCallbackOperator callback;

	public TopicItemAdapter(Context context, IShareCallbackOperator callback,
			List<TopicEntity> dataSourceList) {
		super();
		this.context = context;
		this.callback = callback;
		this.dataSourceList = dataSourceList;
	}

	@Override
	public int getCount() {
		return dataSourceList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataSourceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		holder = new HolderView();
		if (convertview == null) {
			convertview = View
					.inflate(context, R.layout.share_topic_item, null);
			holder.topic_photo = (CircularImage) convertview
					.findViewById(R.id.topic_photo);
			holder.topic_name = (TextView) convertview
					.findViewById(R.id.topic_name);
			holder.topic_desc = (TextView) convertview
					.findViewById(R.id.topic_desc);
			convertview.setTag(holder);
		} else {
			holder = (HolderView) convertview.getTag();
		}
		TopicEntity te = dataSourceList.get(position);
		holder.topic_name.setText(te.getName());
		holder.topic_desc.setText(te.getDesc());
		String imgId = te.getImgId();
		if (imgId != null && !"".equals(imgId)) {

		} else {
			String imageUri = "drawable://" + R.drawable.visitor_me_cover;
			ImageLoader.getInstance()
					.displayImage(imageUri, holder.topic_photo);
		}
		return convertview;
	}

	private class HolderView {
		private TextView topic_name, topic_desc;

		private CircularImage topic_photo;
	}

}
