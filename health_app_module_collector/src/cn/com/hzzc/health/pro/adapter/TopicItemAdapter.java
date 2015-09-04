package cn.com.hzzc.health.pro.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.abstracts.ParentShareSentenceEntity;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.model.TopicEntity;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 空间健康分享适配
 */
public class TopicItemAdapter extends BaseAdapter {
	private List<TopicEntity> dataSourceList = new ArrayList<TopicEntity>();
	private HolderView holder;
	private Context context;
	private IShareCallbackOperator callback;

	private String today = "";
	private String yesteday = "";
	private int this_year;

	public TopicItemAdapter(Context context, IShareCallbackOperator callback,
			List<TopicEntity> dataSourceList) {
		super();
		this.context = context;
		this.callback = callback;
		this.dataSourceList = dataSourceList;
		initDate();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月22日
	 * @todo 初始化日期
	 * @author pang
	 */
	private void initDate() {
		today = CommonDateUtil.formatDate(new Date());
		yesteday = CommonDateUtil
				.formatDate(CommonDateUtil.preDate(new Date()));
		this_year = CommonDateUtil.getYear(new Date());
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
			holder.share_photo = (CircularImage) convertview
					.findViewById(R.id.share_photo);
			holder.share_id = (TextView) convertview
					.findViewById(R.id.share_id);
			holder.share_name = (TextView) convertview
					.findViewById(R.id.share_username);
			holder.share_content = (TextView) convertview
					.findViewById(R.id.share_content);
			holder.picGridView = (GridView) convertview
					.findViewById(R.id.share_space_gridview);
			holder.share_type = (TextView) convertview
					.findViewById(R.id.share_type);
			holder.share_reply = (TextView) convertview
					.findViewById(R.id.share_reply);

			holder.share_bottom_ok = (TextView) convertview
					.findViewById(R.id.share_bottom_ok);
			holder.share_bottom_nook = (TextView) convertview
					.findViewById(R.id.share_bottom_nook);
			holder.share_time = (TextView) convertview
					.findViewById(R.id.share_time);
			convertview.setTag(holder);
		} else {
			holder = (HolderView) convertview.getTag();
		}

		return convertview;
	}

	private class HolderView {
		private TextView share_id, share_name, share_type, share_reply,
				share_content, share_bottom_ok, share_bottom_nook, share_time;
		private GridView picGridView;

		private CircularImage share_photo;
	}

}
