package cn.com.health.pro.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.health.pro.R;
import cn.com.health.pro.model.InfoTypeEntity;

/**
 * @todo 信息类型适配器
 * @author pang
 *
 */
public class InfoTypeAdapter extends ArrayAdapter<InfoTypeEntity> {

	private int resourceId;
	List<InfoTypeEntity> objects;
	Context context;

	public InfoTypeAdapter(Context c, int textViewResourceId,
			List<InfoTypeEntity> os) {
		super(c, textViewResourceId, os);
		this.context = c;
		resourceId = textViewResourceId;
		this.objects = os;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		InfoTypeEntity entity = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView type = (TextView) view.findViewById(R.id.typename);
		TextView if_focus = (TextView) view.findViewById(R.id.if_focus);
		type.setText("[" + entity.getName() + "]");
		String fc = entity.getIfFocus();
		if ("Y".equals(fc)) {
			if_focus.setText("已订阅");
		} else {
			if_focus.setText("未订阅");
		}

		type.setTextSize(16);
		if_focus.setTextSize(16);
		if_focus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InfoTypeEntity e = objects.get(position);
				TextView t = (TextView) v;
				String value = t.getText().toString();
				if ("已订阅".equals(value)) {
					Toast.makeText(getContext(), "成功取消订阅", Toast.LENGTH_SHORT)
							.show();
					e.setIfFocus("N");
				} else if ("未订阅".equals(value)) {
					Toast.makeText(getContext(), "成功订阅", Toast.LENGTH_SHORT)
							.show();
					e.setIfFocus("Y");
				}
				// ((SubscribeTypeActivity) context).refresh();
			}
		});
		return view;
	}
}
