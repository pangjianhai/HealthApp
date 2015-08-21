package cn.com.hzzc.health.pro.persist;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import cn.com.hzzc.health.pro.SystemConst;
import cn.com.hzzc.health.pro.model.SelfNum;
import cn.com.hzzc.health.pro.model.UserItem;
import cn.com.hzzc.health.pro.util.CommonDateUtil;

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
	 * @user:pang
	 * @data:2015年8月21日
	 * @todo:删除所有数据
	 * @return:void
	 */
	public void deleteAllData() {
		context.getSharedPreferences(SystemConst.share_doc_name,
				Context.MODE_PRIVATE).edit().clear().commit();
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
	 * @param id
	 * @param name
	 * @param password
	 * @return
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:退出后清空数据
	 * @return:boolean
	 */
	public boolean clearAccountAfterLoginOut() {
		SharedPreferences sp = this.getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
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

	/**
	 * @param num
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:重置个人的收藏数目等信息
	 * @return:void
	 */
	public void setSelfNum(SelfNum num) {
		SharedPreferences sp = this.getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(SharePreIntoConst.MainMeConst.if_need_reload, false);// 不需要重新取，直接可以用
		editor.putString(SharePreIntoConst.MainMeConst.last_set_reload_date,
				CommonDateUtil.formatDate(new Date()));
		editor.putString(SharePreIntoConst.MainMeConst.my_share_num,
				num.getShareNum());//
		editor.putString(SharePreIntoConst.MainMeConst.my_myfocus_num,
				num.getMyFocusNum());//
		editor.putString(SharePreIntoConst.MainMeConst.my_focusme_num,
				num.getFocusMyNum());//
		editor.commit();
	}

	/**
	 * @return
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:获取个人收藏等数目对象
	 * @return:SelfNum
	 */
	public SelfNum getSelfNum() {
		SharedPreferences sp = this.getSharedPreferences();
		boolean need_reload = sp.getBoolean(
				SharePreIntoConst.MainMeConst.if_need_reload, true);
		if (need_reload) {// 如果需要重新加载则存储在本地的废弃，重新http请求加载
			return null;
		}
		String my_share_num = sp.getString(
				SharePreIntoConst.MainMeConst.my_share_num, "0");
		String my_myfocus_num = sp.getString(
				SharePreIntoConst.MainMeConst.my_myfocus_num, "0");
		String my_focusme_num = sp.getString(
				SharePreIntoConst.MainMeConst.my_focusme_num, "0");
		SelfNum sn = new SelfNum();
		sn.setShareNum(my_share_num);
		sn.setFocusMyNum(my_focusme_num);
		sn.setMyFocusNum(my_myfocus_num);
		return sn;
	}

	/**
	 * @user:pang
	 * @data:2015年8月12日
	 * @todo:个人的收藏数据等信息置为无效
	 * @return:void
	 */
	public void unvalidSelfNum() {
		SharedPreferences sp = this.getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(SharePreIntoConst.MainMeConst.if_need_reload, true);// 设置为需要重新加载
		editor.putString(SharePreIntoConst.MainMeConst.last_set_reload_date, "");
		editor.putString(SharePreIntoConst.MainMeConst.my_share_num, "");//
		editor.putString(SharePreIntoConst.MainMeConst.my_myfocus_num, "");//
		editor.putString(SharePreIntoConst.MainMeConst.my_focusme_num, "");//
		editor.commit();
	}

	/**
	 * @param ui
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:保存登录用户的信息
	 * @return:void
	 */
	public void setLoginUser(UserItem ui) {
		SharedPreferences sp = this.getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(SharePreIntoConst.LoginUserConst.if_need_reload,
				false);// 不需要重新取，直接可以用
		editor.putString(SharePreIntoConst.LoginUserConst.last_set_reload_date,
				CommonDateUtil.formatDate(new Date()));

		editor.putString(SharePreIntoConst.LoginUserConst.uuid, ui.getUuid());//
		editor.putString(SharePreIntoConst.LoginUserConst.userid,
				ui.getUserId());//
		editor.putString(SharePreIntoConst.LoginUserConst.username,
				ui.getUserName());//
		editor.putString(SharePreIntoConst.LoginUserConst.tags, ui.getTags());//
		editor.putString(SharePreIntoConst.LoginUserConst.sex, ui.getSex());//
		editor.putString(SharePreIntoConst.LoginUserConst.img, ui.getImg());//
		editor.putString(SharePreIntoConst.LoginUserConst.birthday,
				ui.getBirthday());//
		editor.putString(SharePreIntoConst.LoginUserConst.email, ui.getEmail());//
		editor.putString(SharePreIntoConst.LoginUserConst.sentence,
				ui.getSentence());//

		/**
		 * 提交保存
		 */
		editor.commit();
	}

	/**
	 * @return
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:获取存储在本地的登录用户的信息
	 * @return:UserItem
	 */
	public UserItem getUserItem() {
		SharedPreferences sp = this.getSharedPreferences();
		boolean need_reload = sp.getBoolean(
				SharePreIntoConst.LoginUserConst.if_need_reload, true);
		if (need_reload) {// 如果需要重新加载则存储在本地的废弃，重新http请求加载
			return null;
		}
		UserItem ui = new UserItem();
		String uuid = sp.getString(SharePreIntoConst.LoginUserConst.uuid, "");
		String userid = sp.getString(SharePreIntoConst.LoginUserConst.userid,
				"");
		String username = sp.getString(
				SharePreIntoConst.LoginUserConst.username, "");
		String tags = sp.getString(SharePreIntoConst.LoginUserConst.tags, "");
		String sex = sp.getString(SharePreIntoConst.LoginUserConst.sex, "");
		String img = sp.getString(SharePreIntoConst.LoginUserConst.img, "");
		String birthday = sp.getString(
				SharePreIntoConst.LoginUserConst.birthday, "");

		String email = sp.getString(SharePreIntoConst.LoginUserConst.email, "");
		String sentence = sp.getString(
				SharePreIntoConst.LoginUserConst.sentence, "");
		ui.setUuid(uuid);
		ui.setUserId(userid);
		ui.setUserName(username);
		ui.setTags(tags);
		ui.setSex(sex);
		ui.setImg(img);
		ui.setBirthday(birthday);
		ui.setEmail(email);
		ui.setSentence(sentence);
		if (uuid == null || "".equals(uuid)) {
			return null;
		}
		return ui;
	}

	/**
	 * @user:pang
	 * @data:2015年8月13日
	 * @todo:设置登录用户信息为无效
	 * @return:void
	 */
	public void unvalidUserItem() {
		SharedPreferences sp = this.getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(SharePreIntoConst.LoginUserConst.if_need_reload, true);// 设置为需要重新加载
		editor.commit();
	}

}
