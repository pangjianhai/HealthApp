package cn.com.health.pro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.health.pro.FirstLoginTopUserListLayout;
import cn.com.health.pro.R;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.model.UserItem;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 用户推荐时候的adapter
 *
 */
public class TopUserItemAdapter extends BaseAdapter {
	private List<UserItem> dataSourceList = new ArrayList<UserItem>();
	private HolderView holder;
	private Context context;

	public TopUserItemAdapter(Context context, List<UserItem> dataSourceList) {
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
			convertView = View.inflate(context, R.layout.top_users_item, null);
			holder.find_search_result_header = (ImageView) convertView
					.findViewById(R.id.find_search_result_header);
			holder.check_someone = (ImageView) convertView
					.findViewById(R.id.check_someone);
			holder.find_search_result_username = (TextView) convertView
					.findViewById(R.id.find_search_result_username);
			holder.find_search_result_tags = (TextView) convertView
					.findViewById(R.id.find_search_result_tags);
			convertView.setTag(holder);
		}
		final UserItem ui = dataSourceList.get(position);
		holder.find_search_result_username.setText(ui.getUserId());
		holder.find_search_result_tags.setText(ui.getSentence());
		String imgId = ui.getImg();
		final String uuid = ui.getUuid();
		final boolean ifAdded = ui.isIfAddedInTopList();
		if (ifAdded) {// 如果没有添加用户过，则进行添加
			holder.check_someone.setImageResource(R.drawable.add_ok);
		}
		if (imgId != null && !"".equals(imgId)) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getHeadImgById
					+ "?para={headImg:'" + imgId + "'}";
			ImageLoader.getInstance().displayImage(pic_url,
					holder.find_search_result_header,
					HealthApplication.getDisplayImageOption());
		} else {
			String imageUri = "drawable://" + R.drawable.default_head0;
			ImageLoader.getInstance().displayImage(imageUri,
					holder.find_search_result_header);
		}
		holder.check_someone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ifAdded) {// 如果没有添加过该人为好友则添加
					ui.setIfAddedInTopList(true);
					if (context instanceof FirstLoginTopUserListLayout) {
						((FirstLoginTopUserListLayout) context)
								.checkSomeOne(uuid);
					}
				}
			}
		});
		convertView.setClickable(true);
		return convertView;
	}

	private class HolderView {
		private ImageView find_search_result_header, check_someone;
		private TextView find_search_result_username;
		private TextView find_search_result_tags;
	}

}
