package cn.com.hzzc.health.pro.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.CommentEntity;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.util.CommonDateUtil;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 评论适配器
 *
 */
public class CommentAdapter extends BaseAdapter {
	private List<CommentEntity> dataSourceList = new ArrayList<CommentEntity>();
	private HolderView holder;
	private Context context;

	public CommentAdapter(Context context, List<CommentEntity> dataSourceList) {
		super();
		this.dataSourceList = dataSourceList;
		this.context = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = new HolderView();
		if (convertView != null) {
			holder = (HolderView) convertView.getTag();
		} else {
			convertView = View.inflate(context, R.layout.comment_item, null);
			holder.tag_id = (TextView) convertView.findViewById(R.id.c_id);
			holder.c_content = (TextView) convertView
					.findViewById(R.id.c_content);
			holder.c_username = (TextView) convertView
					.findViewById(R.id.c_username);
			holder.share_c_photo = (CircularImage) convertView
					.findViewById(R.id.share_c_photo);
			holder.c_date = (TextView) convertView.findViewById(R.id.c_date);
			convertView.setTag(holder);
		}
		CommentEntity ce = dataSourceList.get(position);
		holder.tag_id.setText(ce.getId());
		holder.c_content.setText(ce.getContent());
		Date cd = ce.getCommentDate();
		holder.c_date.setText(getDate(cd));
		String userId = ce.getUserId();
		if (HealthApplication.getUserId().equals(userId)) {
			holder.c_username.setText("我");
		} else {
			holder.c_username.setText(ce.getUserName());
		}
		if (userId != null && !"".equals(userId)) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getHeadImgByUserId
					+ "?para={userId:'" + userId + "'}";
			ImageLoader.getInstance().displayImage(pic_url,
					holder.share_c_photo,
					HealthApplication.getDisplayImageOption());
		} else {
			String imageUri = "drawable://" + R.drawable.head_default;
			ImageLoader.getInstance().displayImage(imageUri,
					holder.share_c_photo);
		}

		return convertView;
	}

	private class HolderView {
		private TextView tag_id, c_content, c_username, c_date;
		private CircularImage share_c_photo;
	}

	private String getDate(Date cd) {
		int c_month = CommonDateUtil.getMonth(cd);
		int c_day = CommonDateUtil.getDay(cd);
		if (!CommonDateUtil.isToday(cd)) {// 如果是同一天
			int hour = CommonDateUtil.getHour(cd);
			int minut = CommonDateUtil.getMinut(cd);
			return hour + ":" + minut;
		} else {// 如果不是同一天
			return c_month + "-" + c_day;
		}
	}

}
