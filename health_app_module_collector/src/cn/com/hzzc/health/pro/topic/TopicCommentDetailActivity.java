package cn.com.hzzc.health.pro.topic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.BaseActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.adapter.TopicPicAdapter;
import cn.com.hzzc.health.pro.config.ShareConst;
import cn.com.hzzc.health.pro.model.TopicPostEntity;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.part.SentenceGridView;
import cn.com.hzzc.health.pro.util.FocusUtil;
import cn.com.hzzc.health.pro.util.PicUtil;
import cn.com.hzzc.health.pro.util.TopicUtil;
import cn.com.hzzc.health.pro.util.UserUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @todo 主题评论详情
 * @author pang
 *
 */
public class TopicCommentDetailActivity extends BaseActivity {

	private String postId;
	private TopicPostEntity tpe;

	private TextView topic_post_detail_content;
	private SentenceGridView topic_post_imgs_gridview;

	/**
	 * 作者信息
	 */
	private UserItem author;
	private CircularImage topic_post_detail_author_photo;
	private TextView topic_post_detail_author_username;
	private Button topic_post_detail_author_focus,
			single_topic_post_bottom_ops_sc, single_topic_post_bottom_ops_good;
	private boolean isFaverate;// 是否收藏
	private boolean isGood;// 是否点赞

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_comment_detail);
		postId = getIntent().getStringExtra("postId");
		initPart();
		initData();
		initOps();
	}

	private void initPart() {
		topic_post_detail_author_photo = (CircularImage) findViewById(R.id.topic_post_detail_author_photo);
		topic_post_detail_author_username = (TextView) findViewById(R.id.topic_post_detail_author_username);
		topic_post_detail_content = (TextView) findViewById(R.id.topic_post_detail_content);
		topic_post_detail_author_focus = (Button) findViewById(R.id.topic_post_detail_author_focus);
		topic_post_imgs_gridview = (SentenceGridView) findViewById(R.id.topic_post_imgs_gridview);
		single_topic_post_bottom_ops_sc = (Button) findViewById(R.id.single_topic_post_bottom_ops_sc);
		single_topic_post_bottom_ops_good = (Button) findViewById(R.id.single_topic_post_bottom_ops_good);
	}

	/**
	 * @user:pang
	 * @data:2015年8月16日
	 * @todo:初始化操作状态
	 * @return:void
	 */
	private void initData() {
		try {
			JSONObject d = new JSONObject();
			// d.put("currentId", userId);
			d.put("picPostId", postId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					tpe = TopicUtil.parseEntity(data);
					renderText(tpe);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					error.printStackTrace();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.getTopicPostBytopicPostId, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void renderText(TopicPostEntity entity) {
		if (entity != null) {
			String authorId = entity.getUserId();
			renderAuthorInfo(authorId);
			renderFriendRel(authorId);
			String content = entity.getShortMsg();
			topic_post_detail_content.setText(content);
			/**
			 * 图片适配器
			 */
			List<String> imgs = entity.getImgs();
			imgs = PicUtil.pureImgList(imgs);
			if (imgs != null && !imgs.isEmpty()) {
				TopicPicAdapter adapter = new TopicPicAdapter(
						TopicCommentDetailActivity.this, imgs);
				topic_post_imgs_gridview.setAdapter(adapter);
			} else {
				topic_post_imgs_gridview.setVisibility(View.GONE);
			}
		}
	}

	private void renderAuthorInfo(String authorId) {
		try {
			JSONObject d = new JSONObject();
			d.put("Id", authorId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					author = UserUtils.parseUserItemFromJSON(data);

					topic_post_detail_author_username.setText(author
							.getUserName());
					String imgId = author.getImg();
					if (imgId != null && !"".equals(imgId)) {
						String pic_url = SystemConst.server_url
								+ SystemConst.FunctionUrl.getHeadImgById
								+ "?para={headImg:'" + imgId + "'}";
						ImageLoader.getInstance().displayImage(pic_url,
								topic_post_detail_author_photo);
					} else {
						String imageUri = "drawable://"
								+ R.drawable.default_head1;
						ImageLoader.getInstance().displayImage(imageUri,
								topic_post_detail_author_photo);
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
			topic_post_detail_author_focus.setVisibility(View.GONE);
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
						topic_post_detail_author_focus.setText("关注此人");
						topic_post_detail_author_focus
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										addFocus(authorId);
									}
								});
						changeStyle(false);
					} else {
						topic_post_detail_author_focus.setText("取消关注");
						topic_post_detail_author_focus
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										cancelFocus(authorId);
									}
								});
						changeStyle(true);

					}
					topic_post_detail_author_focus.setVisibility(View.VISIBLE);
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
					topic_post_detail_author_focus.setText("取消关注");
					topic_post_detail_author_focus
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
					topic_post_detail_author_focus.setText("关注此人");
					topic_post_detail_author_focus
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
			topic_post_detail_author_focus.setTextColor(gray_color);
			topic_post_detail_author_focus
					.setBackgroundResource(R.drawable.share_info_focus_yet_shape);
		} else {
			int gray_color = Color.parseColor("#FFA500");
			topic_post_detail_author_focus.setTextColor(gray_color);
			topic_post_detail_author_focus
					.setBackgroundResource(R.drawable.share_info_focus_shape);
		}
	}

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
		String content = tpe.getShortMsg();
		if (content.length() > 100) {
			content = content.substring(0, 90) + "...";
		}
		oks.setText(content);
		// url仅在微信（包括好友和朋友圈）中使用，查看分享信息的详情
		String info_url = SystemConst.server_url
				+ SystemConst.FunctionUrl.weixin_getShareById + "?id=" + postId;
		// 朋友圈、微信好友打开的链接
		oks.setUrl(info_url);
		// 人人网和QQ空间点击打开的链接
		oks.setTitleUrl(info_url);
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(info_url);
		String image = tpe.getImg0();
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
		// noticeServerTo3Part(share_sentence_id);
	}

	public void initOps() {
		try {
			JSONObject d = new JSONObject();
			d.put("userId", userId);
			d.put("picPostId", postId);
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String data = responseInfo.result;
					isGood = TopicUtil.parseBooleanFlag(data);
					System.out.println(isGood + "==============data:" + data);
					if (isGood) {
						int orange_color = Color.parseColor("#FFA500");
						single_topic_post_bottom_ops_good
								.setTextColor(orange_color);
					}
					single_topic_post_bottom_ops_good
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if (isGood) {
										Toast.makeText(getApplicationContext(),
												"您赞过了...", Toast.LENGTH_SHORT)
												.show();
									} else {
										favorate();
									}
								}
							});
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					error.printStackTrace();
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(SystemConst.server_url
					+ SystemConst.TopicUrl.judgeFavoritePicByUserId, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void favorate() {
		System.out.println("---------------------favorate");
		try {
			JSONObject d = new JSONObject();
			d.put("picPostId", tpe.getId());
			d.put("userId", userId);
			String url = SystemConst.server_url
					+ SystemConst.TopicUrl.clickPostCommentGoodNum;
			RequestCallBack<String> rcb = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					isGood = true;
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(getApplicationContext(), "点赞失败，请重试",
							Toast.LENGTH_SHORT).show();
					;
				}
			};
			Map map = new HashMap();
			map.put("para", d.toString());
			send_normal_request(url, map, rcb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void backoff(View v) {
		finish();
	}
}
