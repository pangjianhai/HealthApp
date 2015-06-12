package cn.com.health.pro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cn.com.health.pro.R;
import cn.com.health.pro.entity.InfoEntity;

public class InfoAdapter extends ArrayAdapter<InfoEntity> {

	HolderView holder;
	private int resourceId;

	public InfoAdapter(Context context, int textViewResourceId,
			List<InfoEntity> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = new HolderView();
		InfoEntity entity = getItem(position);
		if (convertView != null) {
			holder = (HolderView) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(getContext()).inflate(resourceId,
					null);
			holder.type = (TextView) convertView.findViewById(R.id.info_type);
			holder.title = (TextView) convertView.findViewById(R.id.info_title);
			convertView.setTag(holder);
		}
		holder.type.setText("[" + entity.getType() + "]");
		holder.title.setText(entity.getTitle());

		holder.type.setTextSize(16);
		holder.title.setTextSize(16);
		return convertView;
	}

	private class HolderView {
		private TextView type, title;
	}

}
