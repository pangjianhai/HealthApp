package cn.com.health.pro;

import android.os.Bundle;
import android.view.Window;

/**
 * 
 * @author pang
 * @todo 用户设置标签activity
 *
 */
public class TagsForUserActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tags_for_users);

	}

}
