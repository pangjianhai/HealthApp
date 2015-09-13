package cn.com.hzzc.health.pro.controller;

import android.view.View;
import android.view.View.OnClickListener;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.topic.HomeMainMeFragment;

/**
 * @todo 健康空间事件处理controller
 * @author pang
 *
 */
public class FragmentMeController implements OnClickListener {

	private HomeMainMeFragment fragment;

	public FragmentMeController(HomeMainMeFragment context) {
		this.fragment = context;
	}

	@Override
	public void onClick(View v) {
		System.out
				.println("************************************>>" + v.getId());
		fragment.show_me_about(v);

	}

}
