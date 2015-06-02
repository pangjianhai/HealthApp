package cn.com.health.pro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author pang
 * @todo 用户搜索框
 *
 */
public class FriendSeachOpsActivity extends BaseActivity {

	private EditText search_friend_ky;
	private TextView key_copy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friends_search_users);
		init();
	}

	private void init() {
		key_copy = (TextView) findViewById(R.id.key_copy);
		search_friend_ky = (EditText) findViewById(R.id.search_friend_ky);
		search_friend_ky.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				key_copy.setText(s);

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	/**
	 * @tags @param view
	 * @date 2015年5月11日
	 * @todo 点击去搜索结果页面
	 * @author pang
	 */
	public void searchToNextActi(View view) {
		String key = search_friend_ky.getText().toString();
		if (key == null || "".equals(key.trim())) {
			Toast.makeText(FriendSeachOpsActivity.this, "关键词不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(FriendSeachOpsActivity.this,
				FriendSearchResultActivity.class);
		intent.putExtra("key", key);
		startActivity(intent);
	}

	/**
	 * 
	 * @tags @param v
	 * @date 2015年5月19日
	 * @todo 关闭当前页
	 * @author pang
	 */
	public void go_back(View v) {
		finish();
	}
}
