package cn.com.hzzc.health.pro.controller;

import android.view.View;
import android.view.View.OnClickListener;
import cn.com.hzzc.health.pro.R;
import cn.com.hzzc.health.pro.topic.ShareSpaceFragment;

/**
 * @todo 健康空间事件处理controller
 * @author pang
 *
 */
public class FragmentShareController implements OnClickListener {

	private ShareSpaceFragment fragment;

	public FragmentShareController(ShareSpaceFragment context) {
		this.fragment = context;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.share_bottom) {
			fragment.closeInput(v);
		} else if (v.getId() == R.id.share_comment_input_btn) {
			fragment.comment_share(v);
		}

	}

}
