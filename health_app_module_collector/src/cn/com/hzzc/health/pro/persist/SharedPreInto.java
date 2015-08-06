package cn.com.hzzc.health.pro.persist;

import android.content.Context;
import android.content.SharedPreferences;
import cn.com.hzzc.health.pro.SystemConst;

/**
 * 
 * @author pang
 * @todo 关于key-value持久化工具 主要是用户名和密码
 *
 */
public class SharedPreInto {

	/**
	 * 上下文对象
	 */
	private Context context;

	public SharedPreInto(Context context) {
		super();
		this.context = context;
	}

	/**
	 * @tags @return
	 * @date 2015年5月11日
	 * @todo 获取SP
	 * @author pang
	 */
	public SharedPreferences getSharedPreferences() {
		SharedPreferences sp = context.getSharedPreferences(
				SystemConst.share_doc_name, Context.MODE_PRIVATE);
		return sp;
	}

	/**
	 * 
	 * @tags @param id
	 * @tags @param name
	 * @tags @param password
	 * @tags @return
	 * @date 2015年5月11日
	 * @todo 注册成功后第一次保存信息到手机
	 * @author pang
	 */
	public boolean initAccountAfterReg(String id, String name, String password) {
		SharedPreferences sp = this.getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("id", id);// 用户ID
		editor.putString("name", name);// 用户注册登陆名字（unique）
		editor.putString("password", password);// 用户的密码
		editor.commit();
		return true;
	}

	/**
	 * 
	 * @tags @param fieldName
	 * @tags @return
	 * @date 2015年5月11日
	 * @todo 获取某个域的值，没有的话返回空
	 * @author pang
	 */
	public String getSharedFieldValue(String fieldName) {
		SharedPreferences sp = this.getSharedPreferences();
		return sp.getString(fieldName, "");
	}
}
