package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.adapter.CommentAdapter;
import cn.com.hzzc.health.pro.adapter.ShareSinglePicAdapter;
import cn.com.hzzc.health.pro.model.CommentEntity;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.part.XListView.IXListViewListener;
import cn.com.hzzc.health.pro.service.CollectionForInfoService;
import cn.com.hzzc.health.pro.service.ShareCommentService;
import cn.com.hzzc.health.pro.service.ViewForInfoService;
import cn.com.hzzc.health.pro.util.CommentUtil;
import cn.com.hzzc.health.pro.util.PicUtil;
import cn.com.hzzc.health.pro.util.ShareSentenceUtil;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author pang
 * @todo 分享信息详情activity
 *
 */
public class ShareSentenceAllDetailActivity extends BaseActivity implements
		IXListViewListener {

	private TextView share_all_detail_content, share_all_detail_tag;

	private GridView share_detail_imgs_gridview;

	/**
	 * 分享ID
	 */
	private String share_sentence_id;

	/**
	 * 弹出框
	 */
	private LinearLayout share_bottom;
	private EditText et_pop;

	/**
	 * 关于评论的东西
	 */
	private cn.com.hzzc.health.pro.part.XListView share_comment_listview;
	private CommentAdapter ad = null;
	List<CommentEntity> ds = new ArrayList<CommentEntity>();
	private int page = 0;
	private int size = SystemConst.page_size;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_sentence_all_detail);
		Intent intent = getIntent();
		share_sentence_id = intent.getStringExtra("share_sentence_id");
		init();
		initListView();
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月25日
	 * @todo:初始化内容
	 * @return:void
	 */
	private void init() {
		share_all_detail_content = (TextView) findViewById(R.id.share_all_detail_content);
		share_all_detail_tag = (TextView) findViewById(R.id.share_all_detail_tag);
		loadData();
		share_detail_imgs_gridview = (GridView) findViewById(R.id.share_detail_imgs_gridview);

		share_bottom = (LinearLayout) findViewById(R.id.share_bottom);
		et_pop = (EditText) findViewById(R.id.tv_pop);

	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月25日
	 * @todo:初始化listview
	 * @return:void
	 */
	private void initListView() {
		share_comment_listview = (cn.com.hzzc.health.pro.part.XListView) findViewById(R.id.share_comment_listview);
		share_comment_listview.setPullLoadEnable(true);
		share_comment_listview.setXListViewListener(this);
		ad = new CommentAdapter(getApplicationContext(), ds);
		share_comment_listview.setAdapter(ad);
		loadCommentData();
	}

	private void loadData() {
		try {
			JSONObject d = new JSONObject();
			d.put("key", share_sentence_id);

			RequestCallBack<String> rcb = new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					ShareSentenceEntity entity = ShareSentenceUtil
							.parseJsonAddToEntity(data);
					renderText(entity);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.getShareDetailById, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param entity
	 * @user:pang
	 * @data:2015年6月1日
	 * @todo:渲染健康信息的文字和图片
	 * @return:void
	 */
	public void renderText(ShareSentenceEntity entity) {
		if (entity != null) {
			String type = entity.getType();
			String material = entity.getMaterial();
			String function = entity.getFunction();
			String content = entity.getContent();
			String tags = entity.getTags();

			if (SystemConst.ShareInfoType.SHARE_TYPE_FOOD.equals(type)) {
				content = material + "\n" + function + "\n" + content;
			}
			String displayTags = "无";
			if (tags != null && !"".equals(tags)) {
				displayTags = tags;
			}
			share_all_detail_tag.setText("【健康标签】" + displayTags);
			share_all_detail_content.setText(content);
			/**
			 * 图片适配器
			 */
			List<String> imgs = entity.getImgsIds();
			imgs = PicUtil.pureImgList(imgs);
			if (imgs != null && !imgs.isEmpty()) {
				ShareSinglePicAdapter adapter = new ShareSinglePicAdapter(
						ShareSentenceAllDetailActivity.this, imgs);
				share_detail_imgs_gridview.setAdapter(adapter);
			} else {
				share_detail_imgs_gridview.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年7月21日
	 * @todo:右上角点击展开分享按钮
	 * @return:void
	 */
	@Deprecated
	public void showPopWin(View v) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.share_pop_window, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		PopupWindow window = new PopupWindow(view, 300,
				WindowManager.LayoutParams.WRAP_CONTENT);

		window.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		window.showAsDropDown(v);
		window.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");

			}
		});
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月19日
	 * @todo 关闭当前页
	 * @author pang
	 */
	public void backoff(View v) {
		finish();
	}

	/**
	 * @tags @param v
	 * @date 2015年5月26日
	 * @todo 点击操作栏
	 * @author pang
	 */
	public void share_ops_bar(View v) {
		if (!isLogin()) {
			no_login_alter(v);
			return;
		}
		// 点击收藏
		if (v.getId() == R.id.single_share_bottom_ops_sc) {
			Intent intent = new Intent(ShareSentenceAllDetailActivity.this,
					CollectionForInfoService.class);
			intent.putExtra("userId", userId);
			intent.putExtra("sentenceordocId", share_sentence_id);
			intent.putExtra("type",
					CollectionForInfoService.VIEW_ITEM_TYPE_SHARE);
			startService(intent);
			Toast.makeText(ShareSentenceAllDetailActivity.this, "收藏成功",
					Toast.LENGTH_SHORT).show();
		} else if (v.getId() == R.id.single_share_bottom_ops_comment) {
			showInput();
		} else if (v.getId() == R.id.single_share_bottom_ops_ok) {
			Intent intent = new Intent(ShareSentenceAllDetailActivity.this,
					ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
			intent.putExtra("id", share_sentence_id);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_OK);
			startService(intent);
			Toast.makeText(ShareSentenceAllDetailActivity.this, "操作成功",
					Toast.LENGTH_SHORT).show();
		} else if (v.getId() == R.id.single_share_bottom_ops_nook) {
			Intent intent = new Intent(ShareSentenceAllDetailActivity.this,
					ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
			intent.putExtra("id", share_sentence_id);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_NO);
			startService(intent);
			Toast.makeText(ShareSentenceAllDetailActivity.this, "操作成功",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月26日
	 * @todo 出现评论展示框
	 * @author pang
	 */
	public void showInput() {
		share_bottom.setVisibility(View.VISIBLE);
		et_pop.setFocusable(true);
		et_pop.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(et_pop, 0);
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月26日
	 * @todo 评论
	 * @author pang
	 */
	public void comment_share(View v) {
		String comment_str = et_pop.getText().toString();
		if (comment_str == null || "".equals(comment_str.trim())) {
			Toast.makeText(getApplicationContext(), "评论内容不得为空",
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(ShareSentenceAllDetailActivity.this,
				ShareCommentService.class);
		intent.putExtra("userId", userId);
		intent.putExtra("sentenceId", share_sentence_id);
		intent.putExtra("content", comment_str);
		startService(intent);
		et_pop.setText("");
		share_bottom.setVisibility(View.GONE);
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(ShareSentenceAllDetailActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		after_comment_share(comment_str);
	}

	/**
	 * @user:pang
	 * @data:2015年8月14日
	 * @todo:评论之后
	 * @return:void
	 */
	private void after_comment_share(String comment_str) {
		CommentEntity ce = new CommentEntity();
		ce.setUserId(userId);
		ce.setUserName("我");
		ce.setContent(comment_str);
		ds.add(0, ce);
		ad.notifyDataSetChanged();
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月26日
	 * @todo 关闭输入框
	 * @author pang
	 */
	public void closeInput(View v) {
		et_pop.setText("");
		int vi = share_bottom.getVisibility();
		if (vi == View.VISIBLE) {
			share_bottom.setVisibility(View.GONE);
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							ShareSentenceAllDetailActivity.this
									.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**************************************************** 加载评论 ************************************************************/

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年6月25日
	 * @todo:加载评论数据
	 * @return:void
	 */
	private void loadCommentData() {
		try {
			page = page + 1;
			JSONObject d = new JSONObject();
			d.put("sentenceId", share_sentence_id);
			d.put("pageNum", page);
			d.put("pageSize", size);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					List<CommentEntity> list = CommentUtil.parseInfo(data);
					if (list != null && !list.isEmpty()) {
						System.out.println("list:" + list.size());
						ds.addAll(list);
						ad.notifyDataSetChanged();
					}
					onLoadOver();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.get_share_comment_by_id, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @tags
	 * @todo 刷新完毕或加载完毕
	 * @author pang
	 */
	private void onLoadOver() {
		share_comment_listview.stopRefresh();
		share_comment_listview.stopLoadMore();
		share_comment_listview.setRefreshTime("刚才");
	}

	@Override
	public void onRefresh() {
		onLoadOver();
	}

	@Override
	public void onLoadMore() {
		loadCommentData();
	}

	/*******************************************************/
	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年8月6日
	 * @todo:分享
	 * @return:void
	 */
	public void detail_ops(View v) {
		if (isLogin()) {
			showShare();
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

	/**
	 * 
	 * @param v
	 * @user:pang
	 * @data:2015年8月6日
	 * @todo:TODO
	 * @return:void
	 */
	public void show_ops(View v) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.share_detail_ops_window, null);

		PopupWindow window = new PopupWindow(view,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		// window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAsDropDown(v);
		/**
		 * popwindow按钮地方法
		 */
		/**
		 * 让popwindow消失
		 */
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");

			}
		});
	}

}
