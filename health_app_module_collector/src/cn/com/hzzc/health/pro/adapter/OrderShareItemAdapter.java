package cn.com.hzzc.health.pro.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.abstracts.ParentShareSentenceEntity;
import cn.com.hzzc.health.pro.config.HealthApplication;
import cn.com.hzzc.health.pro.model.ShareSentenceEntity;
import cn.com.hzzc.health.pro.part.CircularImage;
import cn.com.hzzc.health.pro.util.CommonDateUtil;
import cn.com.hzzc.health.pro.util.IShareCallbackOperator;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 空间健康分享适配
 */
public class OrderShareItemAdapter extends BaseAdapter {
	private List<ShareSentenceEntity> dataSourceList = new ArrayList<ShareSentenceEntity>();
	private HolderView holder;
	private Context context;
	private IShareCallbackOperator callback;

	private String today = "";
	private String yesteday = "";
	private int this_year;

	public OrderShareItemAdapter(Context context,
			IShareCallbackOperator callback,
			List<ShareSentenceEntity> dataSourceList) {
		super();
		this.context = context;
		this.callback = callback;
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
			convertview = View.inflate(context,
					R.layout.order_share_sentence_item, null);
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
			holder.share_type = (TextView) convertview
					.findViewById(R.id.share_type);
			holder.share_top = (ImageView) convertview
					.findViewById(R.id.share_top);
			holder.share_to_platform = (ImageView) convertview
					.findViewById(R.id.share_to_platform);

			holder.share_bottom_ok = (TextView) convertview
					.findViewById(R.id.share_bottom_ok);
			convertview.setTag(holder);
		} else {
			holder = (HolderView) convertview.getTag();
		}

		/**
		 * 渲染页面
		 */
		rendAll(position);
		/**
		 * 关于操作按钮部分
		 */
		renderOps(dataSourceList.get(position));
		/**
		 * 添加事件
		 */
		addListener(position);
		return convertview;
	}

	private class HolderView {
		private TextView share_id, share_name, share_type, share_content,
				share_bottom_ok;
		private GridView picGridView;
		private ImageView share_top, share_to_platform;
		private CircularImage share_photo;
	}

	/**
	 * 
	 * @tags @param entity
	 * @date 2015年5月22日
	 * @todo 重新绘制UI
	 * @author pang
	 */
	public void renderOps(ShareSentenceEntity entity) {
		holder.share_bottom_ok.setText("收获" + entity.getGoodNum() + "个赞");
		holder.share_bottom_ok.setTextColor(Color.parseColor("#999999"));
	}

	private void rendAll(int position) {
		ShareSentenceEntity entity = dataSourceList.get(position);
		/**
		 * 挨个属性赋值
		 */
		String userId = entity.getUserId();
		String author = entity.getAuthor();
		holder.share_id.setText(entity.getId());
		if (userId != null && userId.equals(HealthApplication.getUserId())) {
			holder.share_name.setText("我");
		} else {
			if (author == null || "".equals(author) || "null".equals(author)) {
				author = "匿名者";
			}
			holder.share_name.setText(author);
		}

		String typeCode = entity.getType();
		String typeName = "";
		String function_field = "", material_filed = "";
		String common_content = entity.getContent();
		String commentNum = entity.getCommentNum();
		String date = entity.getcDate();
		String tags = entity.getTags();
		if (typeCode.equals(SystemConst.ShareInfoType.SHARE_TYPE_FOOD)) {
			typeName = "【饮食】";
			function_field = "功效：" + entity.getFunction();
			material_filed = "食材：" + entity.getMaterial();
			common_content = material_filed + "\n" + function_field + "\n"
					+ common_content;
		} else if (typeCode.equals(SystemConst.ShareInfoType.SHARE_TYPE_HEALTH)) {
			typeName = "【信息】";
		} else if (typeCode.equals(SystemConst.ShareInfoType.SHARE_TYPE_SPORTS)) {
			typeName = "【运动】";
		}
		holder.share_content.setVisibility(View.VISIBLE);
		holder.share_content.setText(common_content);
		holder.share_type.setText(typeName + " " + tags);

		/**
		 * 头像部分
		 */
		userId = entity.getUserId();
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
		ShareSpacePicAdapter picAdapter = new ShareSpacePicAdapter(context,
				entity.getImgsIds());
		holder.picGridView.setAdapter(picAdapter);
		/**
		 * 第一个参数就是我们的图片加载对象ImageLoader,
		 * 第二个是控制是否在滑动过程中暂停加载图片，如果需要暂停传true就行了，第三个参数控制猛的滑动界面的时候图片是否加载
		 */
		holder.picGridView.setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), true, false));

		if (position == 0) {
			holder.share_top.setImageResource(R.drawable.ic_activity_top1);
			holder.share_top.setVisibility(View.VISIBLE);
		} else if (position == 1) {
			holder.share_top.setImageResource(R.drawable.ic_activity_top2);
			holder.share_top.setVisibility(View.VISIBLE);
		} else if (position == 2) {
			holder.share_top.setImageResource(R.drawable.ic_activity_top3);
			holder.share_top.setVisibility(View.VISIBLE);
		} else {
			holder.share_top.setVisibility(View.GONE);
		}
	}

	private void addListener(final int position) {
		ShareSentenceEntity entity = dataSourceList.get(position);
		holder.share_content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.afterClickContent(
						dataSourceList.get(position).getId(), position);
			}
		});
		// 点击用户名
		holder.share_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.afterClickAuthor(dataSourceList.get(position).getId(),
						position);
			}
		});

		// 分享到第三方平台点击事件
		holder.share_to_platform.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.afterClickAuthor(dataSourceList.get(position).getId(),
						position);
			}
		});

	}

	/**
	 * 
	 * @tags @param position
	 * @tags @return
	 * @date 2015年5月20日
	 * @todo 当前用户是否点击过了OK
	 * @author pang
	 */
	public boolean ifClickOk(int position) {
		int if_ok = dataSourceList.get(position).getOps();
		if (if_ok == ParentShareSentenceEntity.OK) {
			Toast.makeText(context, "已经顶过了哦", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @tags @param position
	 * @tags @return
	 * @date 2015年5月20日
	 * @todo 当前用户是否点击过了NOOK
	 * @author pang
	 */
	public boolean ifClickNoOk(int position) {
		int if_nook = dataSourceList.get(position).getOps();
		if (if_nook == ParentShareSentenceEntity.NO_OK) {
			Toast.makeText(context, "已经踩过了哦", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

}
