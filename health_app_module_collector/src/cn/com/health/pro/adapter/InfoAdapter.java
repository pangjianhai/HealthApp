package cn.com.health.pro.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cn.com.health.pro.R;
import cn.com.health.pro.entity.InfoEntity;

public class InfoAdapter extends ArrayAdapter<InfoEntity> {

	private int resourceId;

	public InfoAdapter(Context context, int textViewResourceId,
			List<InfoEntity> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InfoEntity entity = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView type = (TextView) view.findViewById(R.id.info_type);
		TextView title = (TextView) view.findViewById(R.id.info_title);
		type.setText("[" + entity.getType() + "]");
		title.setText(entity.getTitle());

		type.setTextSize(16);
		title.setTextSize(16);

		return view;
	}

}
