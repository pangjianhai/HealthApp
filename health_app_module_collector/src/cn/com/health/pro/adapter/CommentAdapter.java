package cn.com.health.pro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.health.pro.R;
import cn.com.health.pro.model.CommentEntity;

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
			holder.tag_name = (TextView) convertView
					.findViewById(R.id.c_content);
			convertView.setTag(holder);
		}
		CommentEntity ce = dataSourceList.get(position);
		holder.tag_id.setText(ce.getId());
		holder.tag_name.setText(ce.getContent());

		return convertView;
	}

	private class HolderView {
		private TextView tag_id, tag_name;
	}

}
