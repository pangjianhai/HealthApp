package cn.com.health.pro;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import cn.com.health.pro.adapter.InfoTypeAdapter;
import cn.com.health.pro.model.InfoTypeEntity;

/**
 * @todo 信息类型查看
 * @author pang
 *
 */
public class InfoTypeListviewActivity extends BaseActivity {

	ListView types;
	InfoTypeAdapter adapter = null;
	private List<InfoTypeEntity> infoList = new ArrayList<InfoTypeEntity>();

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_type_listview);
		init();
	}

	public void init() {
		types = (ListView) findViewById(R.id.info_type_listview);
		adapter = new InfoTypeAdapter(InfoTypeListviewActivity.this,
				R.layout.type_item, infoList);
		types.setAdapter(adapter);
	}
}
