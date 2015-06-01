package cn.com.health.pro;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.com.health.pro.model.InfoDoc;
import cn.com.health.pro.service.ViewForInfoService;
import cn.com.health.pro.util.CommonHttpUtil;
import cn.com.health.pro.util.HttpCallbackListener;

/**
 * 
 * @author pang
 * 
 * @TODO 信息详情
 *
 */
public class InfoDetailActivity extends BaseActivity {

	private Button info_detail_ops_sc, info_detail_ops_comment,
			info_detail_ops_tag, info_detail_ops_ok, info_detail_ops_nook;

	/**
	 * 信息ID
	 */
	private String docId;
	/**
	 * 信息entity
	 */
	private InfoDoc doc;

	/**
	 * 滚动
	 */
	private ScrollView sView;
	private RelativeLayout rlayout;
	private TextView info_content;
	private TextView info_detail_title;
	private TextView info_detail_good;

	/**
	 * 是否已经评论过了
	 */
	private boolean if_view = false;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (doc != null) {
					info_detail_good.setText(doc.getGoodNum());
					info_content.setText(doc.getContent());
					info_detail_title.setText(doc.getTitle());
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_content_detail);
		Intent intent = getIntent();
		docId = intent.getStringExtra("doc_id");

		sView = (ScrollView) findViewById(R.id.infodetail_content_view);
		rlayout = (RelativeLayout) findViewById(R.id.items_ops_rlayout);
		info_content = (TextView) findViewById(R.id.info_content);
		info_detail_title = (TextView) findViewById(R.id.info_detail_title);
		info_detail_good = (TextView) findViewById(R.id.info_detail_good);
		initData(docId);
	}

	private void initBtn() {
		info_detail_ops_sc = (Button) findViewById(R.id.info_detail_ops_sc);
		info_detail_ops_comment = (Button) findViewById(R.id.info_detail_ops_comment);
		info_detail_ops_tag = (Button) findViewById(R.id.info_detail_ops_tag);
		info_detail_ops_ok = (Button) findViewById(R.id.info_detail_ops_ok);
		info_detail_ops_nook = (Button) findViewById(R.id.info_detail_ops_nook);
	}

	/**
	 * 
	 * @tags @param id
	 * @date 2015年4月22日
	 * @todo TODO 读取文档内容
	 * @author pang
	 */
	private void initData(String id) {
		CommonHttpUtil.sendHttpRequest(SystemConst.server_url
				+ "appSourceController/getDoc.do?para={docId:'" + id + "'}",
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						try {
							JSONObject or_obj = new JSONObject(response);
							JSONObject obj = or_obj
									.getJSONObject("basedocument");
							doc = new InfoDoc();
							doc.setId(obj.getString("id"));
							doc.setTitle(obj.getString("title"));
							doc.setContent(obj.getString("content"));
							doc.setGoodNum(obj.getString("goodNum"));
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception e) {
					}
				});

	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月18日
	 * @todo TODO
	 * @author pang
	 */
	public void backoff(View v) {
		finish();
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月22日
	 * @todo 点击操作栏
	 * @author pang
	 */
	public void info_ops_bar(View v) {
		if (v.getId() == R.id.info_detail_ops_sc) {

		} else if (v.getId() == R.id.info_detail_ops_comment) {

		} else if (v.getId() == R.id.info_detail_ops_tag) {

		} else if (v.getId() == R.id.info_detail_ops_ok) {// 点击“点赞”
			// 设置不能第二次点击
			if (if_view) {
				return;
			}
			if_view = true;
			// 设置颜色
			String oldNum = info_detail_good.getText() + "";
			String newNum = "";
			if (oldNum != null && !"".endsWith(oldNum)) {
				newNum = (Integer.parseInt(oldNum) + 1) + "";
				info_detail_good.setText(newNum);
				info_detail_good.setTextColor(Color.parseColor("#FF9D6F"));
			}
			//info_detail_ops_ok.setTextColor(Color.parseColor("#FF9D6F"));
			// 后台任务
			Intent intent = new Intent(InfoDetailActivity.this,
					ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_INFO);
			intent.putExtra("id", docId);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_OK);
			startService(intent);

		} else if (v.getId() == R.id.info_detail_ops_nook) {// 点击“无用”
			if (if_view) {
				return;
			}
			if_view = true;
			//info_detail_ops_nook.setTextColor(Color.parseColor("#FF9D6F"));
			Intent intent = new Intent(InfoDetailActivity.this,
					ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_INFO);
			intent.putExtra("id", docId);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_NO);
			startService(intent);
		}
	}
}
