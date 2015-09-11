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
import cn.com.hzzc.health.pro.BaseActivity;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.adapter.TopicPicAdapter;
import cn.com.hzzc.health.pro.model.TopicPostEntity;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.part.SentenceGridView;
import cn.com.hzzc.health.pro.util.FocusUtil;
import cn.com.hzzc.health.pro.util.PicUtil;
import cn.com.hzzc.health.pro.util.TopicUtil;
import cn.com.hzzc.health.pro.util.UserUtils;

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

	private TextView topic_post_detail_content;
	private SentenceGridView topic_post_imgs_gridview;

	/**
	 * 作者信息
	 */
	private UserItem author;
	private CircularImage topic_post_detail_author_photo;
	private TextView topic_post_detail_author_username;
	private Button topic_post_detail_author_focus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_comment_detail);
		postId = getIntent().getStringExtra("postId");
		initPart();
		initData();
	}

	private void initPart() {
		topic_post_detail_author_photo = (CircularImage) findViewById(R.id.topic_post_detail_author_photo);
		topic_post_detail_author_username = (TextView) findViewById(R.id.topic_post_detail_author_username);
		topic_post_detail_content = (TextView) findViewById(R.id.topic_post_detail_content);
		topic_post_detail_author_focus = (Button) findViewById(R.id.topic_post_detail_author_focus);
		topic_post_imgs_gridview = (SentenceGridView) findViewById(R.id.topic_post_imgs_gridview);
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
					TopicPostEntity tpe = TopicUtil.parseEntity(data);
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
			System.out.println("-----------------imgs:"+imgs.size());
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

	public void backoff(View v) {
		finish();
	}
}
