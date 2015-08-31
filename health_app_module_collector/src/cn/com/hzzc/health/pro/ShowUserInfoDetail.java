package cn.com.hzzc.health.pro;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.persist.SharedPreInto;
import cn.com.hzzc.health.pro.task.GetOneUsersAsyncTask;
import cn.com.hzzc.health.pro.task.IFOfFriendFocusAsyncTask;
import cn.com.hzzc.health.pro.util.FocusUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 查看个人信息详情
 *
 */
public class ShowUserInfoDetail extends BaseActivity {

	/**
	 * 用户ID
	 */
	private String uuid;

	private String loginUUID;
	/**
	 * 是否是同一个人，只有查看到信息不是当前用户的才能添加好友
	 */
	private boolean ifOneUser = false;

	/**
	 * 当前用户是否已经关注对方
	 */
	private boolean ifAdd = false;

	/**
	 * 文字
	 */
	private ImageView show_user_detail_head;

	/**
	 * 个人基本信息
	 */
	private TextView show_user_detail_nickname, show_user_detail_account,
			show_user_detail_sex, show_user_detail_birthday,
			show_user_detail_email;

	/**
	 * 按钮
	 */
	private Button showUserBtu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_info_detail_show);
		Intent intent = getIntent();
		uuid = intent.getStringExtra("uuid");
		loginUUID = HealthApplication.getUserId();
		if (uuid.equals(loginUUID)) {
			ifOneUser = true;
		}
		init();
		try {

			JSONObject data = new JSONObject();
			data.put("Id", uuid);
			new GetOneUsersAsyncTask(ShowUserInfoDetail.this).execute(data
					.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月13日
	 * @todo 初始化
	 * @author pang
	 */
	private void init() {
		show_user_detail_head = (ImageView) findViewById(R.id.show_user_detail_head);
		show_user_detail_nickname = (TextView) findViewById(R.id.show_user_detail_nickname);
		show_user_detail_account = (TextView) findViewById(R.id.show_user_detail_account);
		show_user_detail_sex = (TextView) findViewById(R.id.show_user_detail_sex);
		show_user_detail_birthday = (TextView) findViewById(R.id.show_user_detail_birthday);
		show_user_detail_email = (TextView) findViewById(R.id.show_user_detail_email);
		showUserBtu = (Button) findViewById(R.id.showUserBtu);
		if (ifOneUser) {
			showUserBtu.setText("编辑");
		} else {
			if (!ifAdd)
				showUserBtu.setText("关注");
		}
	}

	/**
	 * 
	 * @tags @param ui
	 * @date 2015年5月13日
	 * @todo 获取数据渲染界面
	 * @author pang
	 */
	public void getOver(UserItem ui) {
		String imgId = ui.getImg();
		if (imgId != null && !"".equals(imgId)) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getHeadImgById
					+ "?para={headImg:'" + imgId + "'}";
			ImageLoader.getInstance().displayImage(pic_url,
					show_user_detail_head);
		} else {
			String imageUri = "drawable://" + R.drawable.visitor_me_cover;
			ImageLoader.getInstance().displayImage(imageUri,
					show_user_detail_head);
		}
		show_user_detail_account.setText(ui.getUserId());
		show_user_detail_nickname.setText(ui.getUserName());
		String sex_str = "未知";
		if ("0".equals(ui.getSex())) {
			sex_str = "女";
		} else if ("1".equals(ui.getSex())) {
			sex_str = "男";
		}
		show_user_detail_sex.setText(sex_str);
		show_user_detail_birthday.setText(ui.getBirthday());
		show_user_detail_email.setText(ui.getEmail());
		/**
		 * 获取基本信息之后，如果判断不是同一个人，判断是否可以关注
		 */
		if (!ifOneUser) {
			try {
				JSONObject data0 = new JSONObject();
				data0.put("currentId", loginUUID);
				data0.put("friendId", uuid);
				new IFOfFriendFocusAsyncTask(ShowUserInfoDetail.this)
						.execute(data0.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @tags @param b
	 * @date 2015年5月20日
	 * @todo 不同的人，是否已经是关注关系
	 * @author pang
	 */
	public void ifCanAddSomeone(Boolean b) {
		if (b) {
			ifAdd = b;
			showUserBtu.setText("取消关注");
		}
	}

	/**
	 * 
	 * @tags @param view
	 * @date 2015年5月13日
	 * @todo 返回
	 * @author pang
	 */
	public void btn_back(View view) {
		this.finish();
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月13日
	 * @todo 点击按钮修改或添加好友
	 * @author pang
	 */
	public void showUserBtu_click(View v) {
		if (ifOneUser) {// 同一个人就编辑
			Intent intent = new Intent(ShowUserInfoDetail.this,
					EditUserInfoDetail.class);
			intent.putExtra("uuid", uuid);
			startActivity(intent);
			finish();
		} else {// 不是同一个用户，可以进行关注的操作
			if (!ifAdd) {// 添加关注
				addFocus();
			} else {// 取消关注
				cancelFocus();
			}
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月20日
	 * @todo 异步添加关注
	 * @author pang
	 */
	public void addFocus() {

		try {
			JSONObject d = new JSONObject();
			d.put("currentId", loginUUID);
			d.put("friendId", uuid);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					boolean b = FocusUtil.commonFocusResult(data);
					addFocusOver(b);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.some_one_focus_another, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags @param b
	 * @date 2015年5月20日
	 * @todo 关注操作执行完毕
	 * @author pang
	 */
	public void addFocusOver(Boolean b) {
		// 关注成功
		if (b) {
			ifAdd = true;
			/*** 个人手收藏信息变得无效 **/
			new SharedPreInto(getApplicationContext()).unvalidSelfNum();
			showUserBtu.setText("取消关注");
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月20日
	 * @todo 取消关注
	 * @author pang
	 */
	public void cancelFocus() {
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", loginUUID);
			d.put("friendId", uuid);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					boolean b = FocusUtil.commonFocusResult(data);
					cancelFocusOver(b);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.cancel_some_one_focus_another,
					map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags @param b
	 * @date 2015年5月20日
	 * @todo取消 关注操作执行完毕
	 * @author pang
	 */
	public void cancelFocusOver(Boolean b) {
		// 取消关注成功
		if (b) {
			ifAdd = false;
			showUserBtu.setText("关注");
		}
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月24日
	 * @todo 进入个人信息空间
	 * @author pang
	 */
	public void gointo_space(View v) {
		Intent intent = new Intent(ShowUserInfoDetail.this,
				MineSpaceActivity.class);
		intent.putExtra("uuid", uuid);
		startActivity(intent);
	}
}
