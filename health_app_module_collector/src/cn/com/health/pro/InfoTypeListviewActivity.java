package cn.com.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.health.pro.adapter.InfoTypeAdapter;
import cn.com.health.pro.model.InfoTypeEntity;
import cn.com.health.pro.util.ModelUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @todo 信息类型查看
 * @author pang
 *
 */
public class InfoTypeListviewActivity extends BaseActivity {

	ListView types;
	InfoTypeAdapter adapter = null;
	private List<InfoTypeEntity> infoList = new ArrayList<InfoTypeEntity>();

	/**
	 * 页数
	 */
	public int pageNum = 1;
	/**
	 * 页容量
	 */
	public int pageCount = 10;

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_type_listview);
		init();
		loadData();
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月14日
	 * @todo:初始化
	 * @return:void
	 */
	public void init() {
		types = (ListView) findViewById(R.id.info_type_listview);
		adapter = new InfoTypeAdapter(InfoTypeListviewActivity.this,
				R.layout.type_item, infoList);
		types.setAdapter(adapter);
	}

	/**
	 * @user:pang
	 * @data:2015年6月14日
	 * @todo:加载数据
	 * @return:void
	 */
	public void loadData() {
		try {
			JSONObject j = new JSONObject();
			j.put("pageNum", pageNum);
			j.put("pageCount", pageCount);
			Map map = new HashMap();
			map.put("para", j.toString());
			loadData(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月18日
	 * @todo TODO
	 * @author pang
	 */
	public void goback(View v) {
		finish();
	}

	public void loadData(Map<String, String> p) {
		RequestCallBack<String> r = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String rs = responseInfo.result;
				List<InfoTypeEntity> lst = ModelUtil.parseInfo(rs);
				infoList.addAll(lst);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
			}
		};
		String URL = SystemConst.server_url
				+ SystemConst.FunctionUrl.get_all_doc_type;
		super.send_normal_request(URL, p, r);

	}

	/**
	 * 
	 * @param sub
	 * @user:pang
	 * @data:2015年6月14日
	 * @todo:订阅TRUE或者取消订阅FALSE
	 * @return:void
	 */
	public void subOrCancel(String tid, boolean sub) {
		try {
			JSONObject j = new JSONObject();
			j.put("currentId", userId);
			j.put("typeId", tid);
			Map map = new HashMap();
			map.put("para", j.toString());
			RequestCallBack<String> r = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String rs = responseInfo.result;
					Toast.makeText(getApplicationContext(), rs,
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			String URL = null;
			if (sub) {
				URL = SystemConst.server_url + SystemConst.FunctionUrl.sub_type;
			} else {
				URL = SystemConst.server_url
						+ SystemConst.FunctionUrl.cancel_type;
			}
			super.send_normal_request(URL, map, r);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param tId
	 * @user:pang
	 * @data:2015年6月14日
	 * @todo:根据类型获取文档
	 * @return:void
	 */
	public void checkDocsByType(String tId) {

	}
}
