package cn.com.hzzc.health.pro.controller;

import android.view.View;
import android.view.View.OnClickListener;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.topic.HomeAllSpaceShareFragment;

/**
 * @todo 健康空间事件处理controller
 * @author pang
 *
 */
public class FragmentShareController implements OnClickListener {

	private HomeAllSpaceShareFragment fragment;

	public FragmentShareController(HomeAllSpaceShareFragment context) {
		this.fragment = context;
	}

	@Override
	public void onClick(View v) {
		fragment.afterListerner(v);

	}

}
