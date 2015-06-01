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
import cn.com.health.pro.FriendSearchResultActivity;
import cn.com.health.pro.MainPageLayoutMeFocusMeActivity;
import cn.com.health.pro.MainPageLayoutMeMyFocusActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.model.UserItem;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 用户搜索时候的adapter
 *
 */
public class UserItemAdapter extends BaseAdapter {
	private List<UserItem> dataSourceList = new ArrayList<UserItem>();
	private HolderView holder;
	private Context context;

	public UserItemAdapter(Context context, List<UserItem> dataSourceList) {
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
			convertView = View.inflate(context,
					R.layout.friends_search_users_item, null);
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
		UserItem ui = dataSourceList.get(position);
		holder.find_search_result_username.setText(ui.getUserId());
		holder.find_search_result_tags.setText(ui.getTags());
		String imgId = ui.getImg();
		final String uuid = ui.getUuid();
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
				if (context instanceof MainPageLayoutMeMyFocusActivity) {
					((MainPageLayoutMeMyFocusActivity) context)
							.checkSomeOne(uuid);
				} else if (context instanceof FriendSearchResultActivity) {
					((FriendSearchResultActivity) context).checkSomeOne(uuid);
				} else if (context instanceof MainPageLayoutMeFocusMeActivity) {
					((MainPageLayoutMeFocusMeActivity) context)
							.checkSomeOne(uuid);
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
