package cn.com.hzzc.health.pro.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.TopicPostEntity;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.ITopicCommentListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 空间健康分享适配
 */
public class TopicPostItemAdapter extends BaseAdapter {
	private List<TopicPostEntity> dataSourceList = new ArrayList<TopicPostEntity>();
	private HolderView holder;
	private Context context;
	private ITopicCommentListener l;

	private String today = "";
	private String yesteday = "";
	private int this_year;

	public TopicPostItemAdapter(Context context, ITopicCommentListener l,
			List<TopicPostEntity> dataSourceList) {
		super();
		this.l = l;
		this.context = context;
		this.dataSourceList = dataSourceList;
		initDate();
	}

	/**
	 * 
	 * @tags
	 * @date 2015年5月22日
	 * @todo 初始化日期
	 * @author pang
	 */
	private void initDate() {
		today = CommonDateUtil.formatDate(new Date());
		yesteday = CommonDateUtil
				.formatDate(CommonDateUtil.preDate(new Date()));
		this_year = CommonDateUtil.getYear(new Date());
	}

	@Override
	public int getCount() {
		return dataSourceList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataSourceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		holder = new HolderView();
		if (convertview == null) {
			convertview = View.inflate(context, R.layout.topic_post_item, null);
			holder.share_photo = (CircularImage) convertview
					.findViewById(R.id.share_photo);
			holder.share_id = (TextView) convertview
					.findViewById(R.id.share_id);
			holder.share_name = (TextView) convertview
					.findViewById(R.id.share_username);
			holder.share_content = (TextView) convertview
					.findViewById(R.id.share_content);
			holder.picGridView = (GridView) convertview
					.findViewById(R.id.share_space_gridview);
			holder.share_time = (TextView) convertview
					.findViewById(R.id.share_time);
			holder.share_good_num = (TextView) convertview
					.findViewById(R.id.share_good_num);
			holder.share_trans = (TextView) convertview
					.findViewById(R.id.share_trans);
			convertview.setTag(holder);
		} else {
			holder = (HolderView) convertview.getTag();
		}

		/**
		 * 渲染页面
		 */
		rendAll(position);
		addListener(position);
		return convertview;
	}

	/**
	 * 
	 * @param position
	 * @user:pang
	 * @data:2015年9月10日
	 * @todo:渲染界面
	 * @return:void
	 */
	private void rendAll(int position) {
		TopicPostEntity entity = dataSourceList.get(position);
		/**
		 * 挨个属性赋值
		 */
		String id = entity.getId();
		String userId = entity.getUserId();
		String author = entity.getUserName();
		String common_content = entity.getShortMsg();
		int goodnum = entity.getGoodNum();
		int isGood = entity.getIsGood();
		String date = entity.getPostDate();// 带有时分秒
		List<String> imgs = entity.getImgs();
		holder.share_id.setText(id);
		if (userId != null && userId.equals(HealthApplication.getUserId())) {
			holder.share_name.setText("我");
		} else {
			if (author == null || "".equals(author) || "null".equals(author)) {
				author = "匿名者";
			}
			holder.share_name.setText(author);
		}

		if (date != null && !"".equals(date)) {
			date = CommonDateUtil.formatDate(CommonDateUtil.getTime(date));
		}
		if (today.equals(date)) {
			holder.share_time.setText("今天");
		} else if (yesteday.equals(date)) {
			holder.share_time.setText("昨天");
		} else {
			Date createDate = CommonDateUtil.getDate(date);
			int year = CommonDateUtil.getYear(createDate);
			int month = CommonDateUtil.getMonth(createDate);
			int day = CommonDateUtil.getDay(createDate);
			String d_str = "";
			if (this_year == year) {
				d_str = month + "-" + day;
			} else {
				d_str = date;
			}
			holder.share_time.setText(d_str);
		}
		// holder.share_content.setVisibility(View.VISIBLE);
		// holder.share_content.setText(common_content);
		setCommentText(common_content, holder.share_content);
		holder.share_good_num.setText(goodnum + "");
		if (isGood == TopicPostEntity.GOOD_ALREADY) {
			holder.share_good_num.setTextColor(Color.parseColor("#FFA500"));
		} else {
			holder.share_good_num.setTextColor(Color.parseColor("#999999"));
		}
		/**
		 * 头像部分
		 */
		if (userId != null && !"".equals(userId)) {
			String pic_url = SystemConst.server_url
					+ SystemConst.FunctionUrl.getHeadImgByUserId
					+ "?para={userId:'" + userId + "'}";
			ImageLoader.getInstance().displayImage(pic_url, holder.share_photo,
					HealthApplication.getDisplayImageOption());
		} else {
			String imageUri = "drawable://" + R.drawable.head_default;
			ImageLoader.getInstance().displayImage(imageUri,
					holder.share_photo,
					HealthApplication.getDisplayImageOption());
		}
		/**
		 * 图片部分
		 */
		if (imgs != null && !imgs.isEmpty()) {
			TopicPicAdapter picAdapter = new TopicPicAdapter(context,
					entity.getImgs());
			holder.picGridView.setAdapter(picAdapter);
			/**
			 * 第一个参数就是我们的图片加载对象ImageLoader,
			 * 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载
			 */
			holder.picGridView.setOnScrollListener(new PauseOnScrollListener(
					ImageLoader.getInstance(), true, false));
		}
	}

	private void setCommentText(String text, TextView tv) {
		int begin = text.indexOf("#");
		int end = text.lastIndexOf("#");
		if (begin == 0 && end > begin) {
			SpannableString ss = new SpannableString(text);
			ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), 0,
					end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv.setVisibility(View.VISIBLE);
			tv.setText(ss);
		} else {
			tv.setVisibility(View.VISIBLE);
			tv.setText(text);
		}
	}

	/**
	 * @param position
	 * @user:pang
	 * @data:2015年9月10日
	 * @todo:添加监听
	 * @return:void
	 */
	private void addListener(final int position) {
		final TopicPostEntity entity = dataSourceList.get(position);
		holder.share_good_num.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int isGood = entity.getIsGood();
				if (isGood == TopicPostEntity.GOOD_ALREADY) {
					Toast.makeText(context, "您已经赞过了哦", Toast.LENGTH_SHORT)
							.show();
				} else {
					l.addGood(position, entity);
				}
			}
		});

		holder.share_content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				l.detailShow(position, entity);
			}
		});

		holder.share_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				l.userShow(position, entity);
			}
		});
	}

	private class HolderView {
		private TextView share_id, share_name, share_content, share_time,
				share_good_num, share_trans;
		private GridView picGridView;

		private CircularImage share_photo;
	}

}
