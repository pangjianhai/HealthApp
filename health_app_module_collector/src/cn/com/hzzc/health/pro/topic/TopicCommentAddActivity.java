package cn.com.hzzc.health.pro.topic;

import android.os.Bundle;
import android.view.Window;
import cn.com.hzzc.health.pro.BaseActivity;
import cn.com.hzzc.health.pro.R;

/**
 * @todo 添加主题评论
 * @author pang
 *
 */
public class TopicCommentAddActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.topic_comment_add);
	}
}
