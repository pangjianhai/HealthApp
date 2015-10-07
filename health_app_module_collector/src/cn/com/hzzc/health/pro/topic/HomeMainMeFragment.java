package cn.com.hzzc.health.pro.topic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.AppFeedbackActivity;
import cn.com.hzzc.health.pro.MainPageLayoutMeActivity;
import cn.com.hzzc.health.pro.MainPageLayoutMeCollectionActivity;
import cn.com.hzzc.health.pro.MainPageLayoutMeFocusMeActivity;
import cn.com.hzzc.health.pro.MainPageLayoutMeMyFocusActivity;
import cn.com.hzzc.health.pro.MineSpaceActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.ShowUserInfoDetail;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.controller.FragmentMeController;
import cn.com.hzzc.health.pro.model.SelfNum;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.model.VersionEntity;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.part.CustomDialog;
import cn.com.hzzc.health.pro.persist.SharedPreInto;
import cn.com.hzzc.health.pro.util.ActivityCollector;
import cn.com.hzzc.health.pro.util.FileUtil;
import cn.com.hzzc.health.pro.util.UserUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * TODO 首页“我”页签
 * 
 * @author pang
 *
 */
public class HomeMainMeFragment extends ParentFragment {

	private View view;
	private CircularImage main_page_me_photo;
	private TextView main_page_me_name, main_page_me_sentence, me_my_dangan,
			me_my_focus, me_my_focusme, me_my_msg, me_my_shoucang, me_my_tucao,
			me_my_version;
	private Button me_my_logout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.home_main_me_fragment,
				(ViewGroup) getActivity().findViewById(R.id.content), false);
		initPart();
		initUserData();
		initMyNum();
		initListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		ViewGroup viewGroup = (ViewGroup) view.getParent();
		return view;
	}

	public void initPart() {
		main_page_me_photo = (CircularImage) view
				.findViewById(R.id.main_page_me_photo);
		main_page_me_name = (TextView) view
				.findViewById(R.id.main_page_me_name);
		main_page_me_sentence = (TextView) view
				.findViewById(R.id.main_page_me_sentence);
		me_my_dangan = (TextView) view.findViewById(R.id.me_my_dangan);
		me_my_focus = (TextView) view.findViewById(R.id.me_my_focus);
		me_my_focusme = (TextView) view.findViewById(R.id.me_my_focusme);
		me_my_msg = (TextView) view.findViewById(R.id.me_my_msg);
		me_my_shoucang = (TextView) view.findViewById(R.id.me_my_shoucang);
		me_my_version = (TextView) view.findViewById(R.id.me_my_version);
		me_my_tucao = (TextView) view.findViewById(R.id.me_my_tucao);
		me_my_logout = (Button) view.findViewById(R.id.me_my_logout);
	}

	private void initListener() {
		FragmentMeController fsc = new FragmentMeController(this);

		main_page_me_photo.setOnClickListener(fsc);
		main_page_me_name.setOnClickListener(fsc);
		main_page_me_sentence.setOnClickListener(fsc);
		me_my_dangan.setOnClickListener(fsc);
		me_my_focus.setOnClickListener(fsc);
		me_my_focusme.setOnClickListener(fsc);
		me_my_msg.setOnClickListener(fsc);
		me_my_shoucang.setOnClickListener(fsc);
		me_my_tucao.setOnClickListener(fsc);
		me_my_version.setOnClickListener(fsc);
		me_my_logout.setOnClickListener(fsc);

	}

	public void show_me_about(View v) {
		HomeAllShowActivity ac = (HomeAllShowActivity) getActivity();
		if (!ac.isLogin()) {
			ac.no_login_alter(v);
			return;
		}
		if (R.id.main_page_me_photo == v.getId()
				|| R.id.main_page_me_name == v.getId()
				|| R.id.main_page_me_sentence == v.getId()) {// 查看个人信息
			showUserDetail(userId);
		} else if (R.id.me_my_dangan == v.getId()) {
			Intent intent = new Intent(getActivity(), MineSpaceActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.me_my_focus == v.getId()) {
			Intent intent = new Intent(getActivity(),
					MainPageLayoutMeMyFocusActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.me_my_focus == v.getId()) {// 我的关注
			Intent intent = new Intent(getActivity(),
					MainPageLayoutMeMyFocusActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.me_my_focusme == v.getId()) {// 关注我的
			Intent intent = new Intent(getActivity(),
					MainPageLayoutMeFocusMeActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.me_my_shoucang == v.getId()) {// 我的收藏
			Intent intent = new Intent(getActivity(),
					MainPageLayoutMeCollectionActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.me_my_tucao == v.getId()) {// 吐槽产品
			Intent intent = new Intent(getActivity(), AppFeedbackActivity.class);
			intent.putExtra("uuid", userId);
			startActivity(intent);
		} else if (R.id.me_my_version == v.getId()) {// 版本检测
			versionScan();
		} else if (R.id.me_my_logout == v.getId()) {// 退出
			logout(null);
		}
	}

	/**
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:获取用户数据
	 * @return:void
	 */
	public void initUserData() {
		final SharedPreInto spi = new SharedPreInto(getActivity());
		UserItem ui = spi.getUserItem();
		/**
		 * 先判断本地文件是否存在用户信息
		 */
		if (ui != null) {
			getOver(ui);
		} else {// http请求最新用户信息
			getUserInfo();
		}

		/**
		 * 如果这里是下载完最新apk之后，点击消息，重新进入此activity则安装apk文件
		 */
		String install_flag = getActivity().getIntent().getStringExtra(
				"install");
		if (install_flag != null && !"".equals(install_flag)) {
			installApk();
		}
	}

	/**
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:http请求最新用户西悉尼
	 * @return:void
	 */
	private void getUserInfo() {
		try {
			final SharedPreInto spi = new SharedPreInto(getActivity());
			JSONObject d = new JSONObject();
			d.put("Id", userId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					UserItem ui = UserUtils.parseUserItemFromJSON(data);
					getOver(ui);
					/**
					 * 将最新的数据缓存到本地文件
					 */
					spi.setLoginUser(ui);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.getUserById, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:获取关注数字等信息
	 * @return:void
	 */
	public void initMyNum() {
		try {
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					SelfNum d = UserUtils.parsUserResult(data);
					getNum(d);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.getResultNumberUserById, map, rcb);
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
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("确定要退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				/******** 正式退出 ******/
				ActivityCollector.finishAll();
				/******** 返回首页 *********/
				Intent intent = new Intent(HealthApplication.getContext(),
						HomeAllShowActivity.class);
				startActivity(intent);
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

	}

	/**
	 * 
	 * @tags @param ui
	 * @date 2015年5月27日
	 * @todo 渠道个人信息
	 * @author pang
	 */
	public void getOver(UserItem ui) {
		String userName = ui.getUserName();
		String sentence = ui.getSentence();
		if (userName == null || "".equals(userName)) {
			userName = "未设置昵称";
		}
		if (sentence == null || "".equals(sentence)) {
			sentence = "点击可以设置个性签名";
		}
		main_page_me_name.setText(userName);
		main_page_me_sentence.setText(sentence);

		String imgId = ui.getImg();
		if (imgId != null && !"".equals(imgId)) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getHeadImgById
					+ "?para={headImg:'" + imgId + "'}";
			ImageLoader.getInstance().displayImage(pic_url, main_page_me_photo);
		} else {
			String imageUri = "drawable://" + R.drawable.visitor_me_cover;
			ImageLoader.getInstance()
					.displayImage(imageUri, main_page_me_photo);
		}

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

	/************************************ 和版本检测和下载有关的代码 *****************************************/
	/**
	 * 关于真正的下载
	 */
	private ProgressDialog dialog = null;
	/**
	 * 封装从服务端返回的最新版本的信息
	 */
	private VersionEntity ve = null;
	private String download_app_name = "health_app_module_collector.apk";

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:检测新版本，根据当前版本去服务器查看，如果有新版本服务器会返回信息，否则返回为空
	 * @return:void
	 */
	private void versionScan() {
		final String versionName = this.getString(R.string.app_version_name);
		final String versionNum = this.getString(R.string.app_version_num);// 当前APP的版本

		try {
			JSONObject d = new JSONObject();
			d.put("versionNum", versionNum);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					ve = FileUtil.parseVersionEntity(data);
					if (ve == null) {// 说明当前版本是最新
						no_new_version(versionName, versionNum);
					} else {// 说明已经有最新版本了，提示可以下载
						has_new_version();
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.scan_app_version_num, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param versionName
	 * @param versionNum
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:没有最新版本的提示
	 * @return:void
	 */
	private void no_new_version(String versionName, String versionNum) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("当前版本已是最新 \n " + versionName);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	/**
	 * 
	 * @param versionName
	 * @param versionNum
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:拥有最新版本，删除旧文件，提示可以进行下载了
	 * @return:void
	 */
	private void has_new_version() {
		/**
		 * 清空旧的安装包
		 */
		String dirPath = FileUtil.getDownloadFileDir();
		File f = new File(dirPath);
		File[] fs = f.listFiles();
		if (fs != null && fs.length > 0) {
			for (File ff : fs) {
				ff.delete();
			}
		}
		/**
		 * 提示可以下载新的安装包
		 */

		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("有最新版本可供下载\n确定下载吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				real_download();
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	/**
	 * @user:pang
	 * @data:2015年7月10日
	 * @todo:真正下载新版本apk的地方
	 * @return:void
	 */
	private void real_download() {
		dialog = new ProgressDialog(getActivity());
		dialog.setIcon(R.drawable.download_app1);
		dialog.setTitle("下载");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(true);
		dialog.show();
		HttpUtils http = new HttpUtils();
		HttpHandler handler = http.download(ve.getDownLoadUrl().trim(),
				FileUtil.getDownloadFileDir() + "/" + download_app_name, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {

					@Override
					public void onStart() {
						dialog.setMessage("开始下载");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						int x = (int) (current * 100 / total);
						dialog.setProgress(x);
					}

					/**
					 * 下载成功之后给后台发送消息通知，同时移动端提醒下载完毕，最后对话框消失
					 */
					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						after_download_success();
						sendMsgForDownloadSuccess();
						dialog.dismiss();
						Toast.makeText(getActivity(), "下载成功",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						dialog.dismiss();
						Toast.makeText(getActivity(), "下载失败",
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	/**
	 * @user:pang
	 * @data:2015年8月9日
	 * @todo:下载成功后手机消息收到通知
	 * @return:void
	 */
	public void sendMsgForDownloadSuccess() {
		NotificationManager nm = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getActivity()).setSmallIcon(R.drawable.download_app0)// 设置图标
				.setContentTitle("通知")// 设置标题
				.setContentText("最新[康兮]下载成功");// 设置内容

		// 构建一个Intent
		Intent resultIntent = new Intent(getActivity(),
				MainPageLayoutMeActivity.class);
		resultIntent.putExtra("install", "install");
		// 封装一个Intent
		PendingIntent resultPendingIntent = PendingIntent.getActivity(
				getActivity(), 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		Notification n = mBuilder.build();
		n.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动关闭通知
		// 设定默认震动
		n.defaults |= Notification.DEFAULT_VIBRATE;
		// 设定默认LED灯提醒
		n.defaults |= Notification.DEFAULT_LIGHTS;
		// 设置点击后通知自动清除
		n.defaults |= Notification.FLAG_AUTO_CANCEL;

		nm.notify(0, n);
	}

	/**
	 * @user:pang
	 * @data:2015年8月9日
	 * @todo:安装apk文件
	 * @return:void
	 */
	protected void installApk() {
		String dirPath = FileUtil.getDownloadFileDir();
		File f = new File(dirPath);
		File[] fs = f.listFiles();
		File apk_file = fs[0];
		Intent intent = new Intent();
		// 执行动作
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(apk_file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	/**
	 * @user:pang
	 * @data:2015年7月11日
	 * @todo:下载成功后，发送消息到后台
	 * @return:void
	 */
	private void after_download_success() {
		try {
			JSONObject d = new JSONObject();
			d.put("userUuid", userId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.after_down_load_success, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param id
	 * @user:pang
	 * @data:2015年8月9日
	 * @todo:查看用户信息
	 * @return:void
	 */
	public void showUserDetail(String id) {
		Intent intent = new Intent(getActivity(), ShowUserInfoDetail.class);
		intent.putExtra("uuid", id);
		startActivity(intent);
	}

	@Override
	public void screenScroll(float y) {
		// TODO Auto-generated method stub

	}

}
