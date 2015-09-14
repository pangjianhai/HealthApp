package cn.com.hzzc.health.pro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.adapter.CommentAdapter;
import cn.com.hzzc.health.pro.adapter.ShareSinglePicAdapter;
import cn.com.hzzc.health.pro.config.ShareConst;
import cn.com.hzzc.health.pro.model.CommentEntity;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.part.SentenceListView.SentenceListViewListener;
import cn.com.hzzc.health.pro.service.CollectionForInfoService;
import cn.com.hzzc.health.pro.service.ShareCommentService;
import cn.com.hzzc.health.pro.service.ViewForInfoService;
import cn.com.hzzc.health.pro.util.CommentUtil;
import cn.com.hzzc.health.pro.util.FocusUtil;
import cn.com.hzzc.health.pro.util.PicUtil;
import cn.com.hzzc.health.pro.util.ShareSentenceUtil;
import cn.com.hzzc.health.pro.util.UserUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author pang
 * @todo 分享信息详情activity
 *
 */
public class ShareSentenceAllDetailActivity extends BaseActivity implements
		SentenceListViewListener {

	private TextView share_all_detail_content, share_all_detail_tag;

	private GridView share_detail_imgs_gridview;

	/**
	 * 分享ID
	 */
	private String share_sentence_id;

	/**
	 * 获取的分享信息实例
	 */
	private ShareSentenceEntity entity;

	/**
	 * 弹出框
	 */
	private LinearLayout share_bottom;
	private EditText et_pop;
	/**
	 * 回复某人
	 */
	private String replyUserId = "";

	/**
	 * 关于评论的东西
	 */
	private cn.com.hzzc.health.pro.part.SentenceListView share_comment_listview;
	private CommentAdapter ad = null;
	List<CommentEntity> ds = new ArrayList<CommentEntity>();
	private int page = 0;
	private int size = 10;

	/**
	 * 作者信息
	 */
	private UserItem author;
	private CircularImage share_all_detail_author_photo;
	private TextView share_all_detail_author_name;
	private Button share_all_detail_author_focus;

	/**
	 * 点赞状态
	 */
	private String like_or_dis_state = "more";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_sentence_all_detail);
		Intent intent = getIntent();
		share_sentence_id = intent.getStringExtra("share_sentence_id");
		init();
		initListView();
		initGoodState(userId, share_sentence_id);
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
		share_all_detail_author_photo = (CircularImage) findViewById(R.id.share_all_detail_author_photo);
		share_all_detail_author_name = (TextView) findViewById(R.id.share_all_detail_author_name);
		share_all_detail_author_focus = (Button) findViewById(R.id.share_all_detail_author_focus);

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
		share_comment_listview = (cn.com.hzzc.health.pro.part.SentenceListView) findViewById(R.id.share_comment_listview);
		share_comment_listview.setPullRefreshEnable(false);// 不允许更新
		share_comment_listview.setPullLoadEnable(false);// 不允许加载（是否允许load的时候会有判断）
		share_comment_listview.setXListViewListener(this);
		ad = new CommentAdapter(ShareSentenceAllDetailActivity.this, ds);
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
					entity = ShareSentenceUtil.parseJsonAddToEntity(data);
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
			String authorId = entity.getUserId();
			renderAuthorInfo(authorId);
			renderFriendRel(authorId);
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
	 * @param authorId
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:初始化作者信息
	 * @return:void
	 */
	private void renderAuthorInfo(String authorId) {
		try {
			JSONObject d = new JSONObject();
			d.put("Id", authorId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					author = UserUtils.parseUserItemFromJSON(data);

					share_all_detail_author_name.setText(author.getUserName());
					String imgId = author.getImg();
					if (imgId != null && !"".equals(imgId)) {
						String pic_url = SystemConst.server_url
								+ SystemConst.FunctionUrl.getHeadImgById
								+ "?para={headImg:'" + imgId + "'}";
						ImageLoader.getInstance().displayImage(pic_url,
								share_all_detail_author_photo);
					} else {
						String imageUri = "drawable://"
								+ R.drawable.default_head1;
						ImageLoader.getInstance().displayImage(imageUri,
								share_all_detail_author_photo);
					}
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
	 * 
	 * @param authorId
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:判断用户和作者的关系，是否是同一个人，是否可以添加关注
	 * @return:void
	 */
	private void renderFriendRel(final String authorId) {
		if (userId.equals(authorId)) {
			share_all_detail_author_focus.setVisibility(View.GONE);
			return;
		}
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", userId);
			d.put("friendId", authorId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					Boolean flag = FocusUtil.commonFocusResult(data);
					if (flag) {
						share_all_detail_author_focus.setText("关注此人");
						share_all_detail_author_focus
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										addFocus(authorId);
									}
								});
						changeStyle(false);
					} else {
						share_all_detail_author_focus.setText("取消关注");
						share_all_detail_author_focus
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										cancelFocus(authorId);
									}
								});
						changeStyle(true);

					}
					share_all_detail_author_focus.setVisibility(View.VISIBLE);
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.if_some_one_focus_another, map,
					rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:添加关注
	 * @return:void
	 */
	public void addFocus(final String authorId) {

		try {
			JSONObject d = new JSONObject();
			d.put("currentId", userId);
			d.put("friendId", authorId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					boolean b = FocusUtil.commonFocusResult(data);
					share_all_detail_author_focus.setText("取消关注");
					share_all_detail_author_focus
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									cancelFocus(authorId);
								}
							});
					changeStyle(true);

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
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:取消关注
	 * @return:void
	 */
	public void cancelFocus(final String authorId) {
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", userId);
			d.put("friendId", authorId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					boolean b = FocusUtil.commonFocusResult(data);
					share_all_detail_author_focus.setText("关注此人");
					share_all_detail_author_focus
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									addFocus(authorId);
								}
							});
					changeStyle(false);
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

	private void changeStyle(boolean ifFocus) {
		if (ifFocus) {
			int gray_color = Color.parseColor("#BEBEBE");
			share_all_detail_author_focus.setTextColor(gray_color);
			share_all_detail_author_focus
					.setBackgroundResource(R.drawable.share_info_focus_yet_shape);
		} else {
			int gray_color = Color.parseColor("#FFA500");
			share_all_detail_author_focus.setTextColor(gray_color);
			share_all_detail_author_focus
					.setBackgroundResource(R.drawable.share_info_focus_shape);
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
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:初始化操作状态
	 * @return:void
	 */
	private void initGoodState(String userId, String shareId) {
		try {
			JSONObject d = new JSONObject();
			d.put("currentId", userId);
			d.put("shareId", shareId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					like_or_dis_state = ShareSentenceUtil
							.paseLikeShareOrDis(data);
					if ("like".equals(like_or_dis_state)) {
						changeColorAfterClickLikeOr(R.id.single_share_bottom_ops_ok);
					} else if ("dislike".equals(like_or_dis_state)) {
						changeColorAfterClickLikeOr(R.id.single_share_bottom_ops_nook);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {

				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.judgeIflikeNumOrdislikeNum, map,
					rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param id
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:点击好评或者差评之后改变颜色
	 * @return:void
	 */
	private void changeColorAfterClickLikeOr(int id) {
		int gray_color = Color.parseColor("#FFA500");
		share_all_detail_author_focus.setTextColor(gray_color);
		((Button) findViewById(id)).setTextColor(gray_color);
	}

	/**
	 * @return
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:是否可以点赞或者差评，因为之前可能已经操作过了
	 * @return:boolean
	 */
	public boolean ifCanClickLikeOrDislike() {
		if (like_or_dis_state == null || "more".equals(like_or_dis_state)) {
			return true;
		} else {
			if ("like".equals(like_or_dis_state)) {
				Toast.makeText(getApplicationContext(), "您已经顶过",
						Toast.LENGTH_SHORT).show();
			} else if ("dislike".equals(like_or_dis_state)) {
				Toast.makeText(getApplicationContext(), "您已经踩过",
						Toast.LENGTH_SHORT).show();
			}

			return false;
		}
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
		} else if (v.getId() == R.id.single_share_bottom_ops_comment) {// 评论
			showInput();
		} else if (v.getId() == R.id.single_share_bottom_ops_ok) {// 好评
			if (!ifCanClickLikeOrDislike()) {
				return;
			}
			Intent intent = new Intent(ShareSentenceAllDetailActivity.this,
					ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
			intent.putExtra("id", share_sentence_id);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_OK);
			startService(intent);
			Toast.makeText(ShareSentenceAllDetailActivity.this, "操作成功",
					Toast.LENGTH_SHORT).show();

			like_or_dis_state = "like";
			changeColorAfterClickLikeOr(R.id.single_share_bottom_ops_ok);
		} else if (v.getId() == R.id.single_share_bottom_ops_nook) {// 差评
			if (!ifCanClickLikeOrDislike()) {
				return;
			}
			Intent intent = new Intent(ShareSentenceAllDetailActivity.this,
					ViewForInfoService.class);
			intent.putExtra("type", ViewForInfoService.VIEW_ITEM_TYPE_SHARE);
			intent.putExtra("id", share_sentence_id);
			intent.putExtra("view", ViewForInfoService.VIEW_VIEW_TYPE_NO);
			startService(intent);
			Toast.makeText(ShareSentenceAllDetailActivity.this, "操作成功",
					Toast.LENGTH_SHORT).show();

			like_or_dis_state = "dislike";
			changeColorAfterClickLikeOr(R.id.single_share_bottom_ops_nook);
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
		et_pop.setHint("");
		replyUserId = "";
		et_pop.setFocusable(true);
		et_pop.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(et_pop, 0);
	}

	/**
	 * @param position
	 * @user:pang
	 * @data:2015年8月30日
	 * @todo:评论
	 * @return:void
	 */
	public void showInput(int position) {
		CommentEntity ce = ds.get(position);
		share_bottom.setVisibility(View.VISIBLE);
		et_pop.setHint("回复" + ce.getUserName());
		replyUserId = ce.getUserId();
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
		intent.putExtra("repyUserId", replyUserId);
		after_comment_share(comment_str);
		startService(intent);
		et_pop.setText("");
		share_bottom.setVisibility(View.GONE);
		// ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
		// .hideSoftInputFromWindow(ShareSentenceAllDetailActivity.this
		// .getCurrentFocus().getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);
		InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
		ce.setCommentDate(new Date());
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
						ds.addAll(list);
						ad.notifyDataSetChanged();
					}
					onLoadOver();
					if (list != null && list.size() >= size) {
						share_comment_listview.setPullLoadEnable(true);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					onLoadOver();
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
		// entity
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(ShareConst.share_title);
		// text是分享文本，所有平台都需要这个字段
		String content = entity.getContent();
		if (content.length() > 100) {
			content = content.substring(0, 90) + "...";
		}
		oks.setText(content);
		// url仅在微信（包括好友和朋友圈）中使用，查看分享信息的详情
		String info_url = SystemConst.server_url
				+ SystemConst.FunctionUrl.weixin_getShareById + "?id="
				+ share_sentence_id;
		// 朋友圈、微信好友打开的链接
		oks.setUrl(info_url);
		// 人人网和QQ空间点击打开的链接
		oks.setTitleUrl(info_url);
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(info_url);
		String image = entity.getImg0();
		if (image == null || "".equals(image.trim())) {
			image = "";
		}
		String pic_url = SystemConst.server_url
				+ SystemConst.FunctionUrl.weixin_getShareImgById + "?id="
				+ image;
		// 微信、朋友圈、QQ看到的提示图片
		oks.setImageUrl(pic_url);

		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));

		// 启动分享GUI
		oks.show(this);
		noticeServerTo3Part(share_sentence_id);
	}

	/**
	 * @param shareId
	 * @user:pang
	 * @data:2015年8月30日
	 * @todo:分享到第三方平台成功后通知后台
	 * @return:void
	 */
	private void noticeServerTo3Part(String shareId) {
		try {
			JSONObject d = new JSONObject();
			d.put("userUuid", userId);
			d.put("shareId", shareId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
				}

				@Override
				public void onFailure(HttpException error, String msg) {
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.FunctionUrl.share_to_3part_platform, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
