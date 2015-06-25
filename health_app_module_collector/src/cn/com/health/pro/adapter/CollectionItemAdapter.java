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
import cn.com.health.pro.MainPageLayoutMeCollectionActivity;
import cn.com.health.pro.R;
import cn.com.health.pro.SystemConst;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.model.CollectionItem;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 用户搜索时候的adapter
 *
 */
public class CollectionItemAdapter extends BaseAdapter {
	private List<CollectionItem> dataSourceList = new ArrayList<CollectionItem>();
	private HolderView holder;
	private Context context;

	public CollectionItemAdapter(Context context,
			List<CollectionItem> dataSourceList) {
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
					R.layout.collection_listview_item, null);
			holder.my_collection_title = (TextView) convertView
					.findViewById(R.id.my_collection_title);
			holder.my_collection_id = (TextView) convertView
					.findViewById(R.id.my_collection_id);
			holder.my_collection_type = (TextView) convertView
					.findViewById(R.id.my_collection_type);
			convertView.setTag(holder);
		}
		CollectionItem ci = dataSourceList.get(position);
		holder.my_collection_id.setText(ci.getId());
		holder.my_collection_title.setText(ci.getTitle());
		final String id = ci.getId();
		final String type = ci.getType();
		String typeName = "";
		if (SystemConst.CollectionType.COLLECTION_TYPE_SHARE.equals(type)) {
			typeName = "分享";

		} else if (SystemConst.CollectionType.COLLECTION_TYPE_DOC.equals(type)) {
			typeName = "报道";
		}
		holder.my_collection_type.setText(typeName);
		holder.my_collection_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (context instanceof MainPageLayoutMeCollectionActivity) {
					((MainPageLayoutMeCollectionActivity) context)
							.checkCollection(id, type);
				}
			}
		});
		convertView.setClickable(true);
		return convertView;
	}

	private class HolderView {
		private TextView my_collection_title, my_collection_id,
				my_collection_type;
	}

}
