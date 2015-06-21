package cn.com.health.pro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.health.pro.FriendSearchResultActivity;
import cn.com.health.pro.MainPageLayoutMeFocusMeActivity;
import cn.com.health.pro.MainPageLayoutMeMyFocusActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.model.Tag;
import cn.com.health.pro.model.UserItem;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 添加标签适配器
 *
 */
public class TagAdapter extends BaseAdapter {
	private List<Tag> dataSourceList = new ArrayList<Tag>();
	private HolderView holder;
	private Context context;

	public TagAdapter(Context context, List<Tag> dataSourceList) {
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
			convertView = View.inflate(context, R.layout.tag_item, null);
			holder.tag_id = (TextView) convertView.findViewById(R.id.tag_id);
			holder.tag_name = (TextView) convertView
					.findViewById(R.id.tag_name);
			convertView.setTag(holder);
		}
		Tag tag = dataSourceList.get(position);
		holder.tag_id.setText(tag.getId());
		holder.tag_name.setText(tag.getDisplayName());

		convertView.setClickable(true);
		return convertView;
	}

	private class HolderView {
		private TextView tag_id, tag_name;
	}

}
