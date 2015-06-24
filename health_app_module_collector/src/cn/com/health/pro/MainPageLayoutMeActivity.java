package cn.com.health.pro;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.health.pro.abstracts.ParentMainActivity;
import cn.com.health.pro.config.HealthApplication;
import cn.com.health.pro.model.SelfNum;
import cn.com.health.pro.model.UserItem;
import cn.com.health.pro.persist.SharedPreInto;
import cn.com.health.pro.task.GetSelfInfoAsyncTask;
import cn.com.health.pro.task.GetSelfInfoNumAsyncTask;
import cn.com.health.pro.util.ActivityCollector;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 登陆成功后-我activity
 *
 */
public class MainPageLayoutMeActivity extends ParentMainActivity {

	private ImageView main_page_me_photo;
	private TextView main_page_me_name, main_page_me_sentence, me_my_dangan,
			me_my_focus, me_my_focusme, me_my_msg, me_my_shoucang;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main_page_layout_me);
		initPart();
		initData();
	}

	public void initPart() {
		main_page_me_photo = (ImageView) findViewById(R.id.main_page_me_photo);
		main_page_me_name = (TextView) findViewById(R.id.main_page_me_name);
		main_page_me_sentence = (TextView) findViewById(R.id.main_page_me_sentence);
		me_my_dangan = (TextView) findViewById(R.id.me_my_dangan);
		me_my_focus = (TextView) findViewById(R.id.me_my_focus);
		me_my_focusme = (TextView) findViewById(R.id.me_my_focusme);
		me_my_msg = (TextView) findViewById(R.id.me_my_msg);
		me_my_shoucang = (TextView) findViewById(R.id.me_my_shoucang);
	}

	public void initData() {
		try {
			//
			JSONObject data = new JSONObject();
			data.put("Id", userId);
			new GetSelfInfoAsyncTask(MainPageLayoutMeActivity.this)
					.execute(data.toString());

			//
			JSONObject data2 = new JSONObject();
			data2.put("userId", userId);
			new GetSelfInfoNumAsyncTask(MainPageLayoutMeActivity.this)
					.execute(data2.toString());

			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getHeadImgByUserId
					+ "?para={userId:'" + userId + "'}";
			ImageLoader.getInstance().displayImage(pic_url, main_page_me_photo,
					HealthApplication.getDisplayImageOption());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月20日
	 * @todo 退出登陆，删除本地用户个人信息
	 * @author pang
	 */
	public void logout(View v) {
		new SharedPreInto(MainPageLayoutMeActivity.this).initAccountAfterReg(
				"", "", "");
		HealthApplication.setUserId("");
		ActivityCollector.finishAll();

	}

	/**
	 * 
	 * @tags @param ui
	 * @date 2015年5月27日
	 * @todo 渠道个人信息
	 * @author pang
	 */
	public void getOver(UserItem ui) {
		main_page_me_name.setText(ui.getUserName());
		main_page_me_sentence.setText(ui.getSentence());
	}

	/**
	 * 
	 * @tags @param n
	 * @date 2015年5月27日
	 * @todo 取得数字信息
	 * @author pang
	 */
	public void getNum(SelfNum n) {
		me_my_dangan.setText(me_my_dangan.getText() + "(" + n.getShareNum()
				+ ")");
		me_my_focus.setText(me_my_focus.getText() + "(" + n.getMyFocusNum()
				+ ")");
		me_my_focusme.setText(me_my_focusme.getText() + "(" + n.getFocusMyNum()
				+ ")");
	}

	public void show_me_about(View v) {
		if (R.id.main_page_me_dangan == v.getId()) {
			Intent intent = new Intent(MainPageLayoutMeActivity.this,
					MineSpaceActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.main_page_me_myfocus == v.getId()) {// 我的关注
			Intent intent = new Intent(MainPageLayoutMeActivity.this,
					MainPageLayoutMeMyFocusActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.main_page_me_focuseme == v.getId()) {// 关注我的
			Intent intent = new Intent(MainPageLayoutMeActivity.this,
					MainPageLayoutMeFocusMeActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.main_page_me_shoucang == v.getId()) {// 我的收藏
			Intent intent = new Intent(MainPageLayoutMeActivity.this,
					MainPageLayoutMeCollectionActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		}
	}

	public void backoff(View v) {
		to_home_page();
		finish();
	}
}
