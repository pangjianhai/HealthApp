package cn.com.hzzc.health.pro.config;

/**
 * @todo 推送类型，参数的主要目的是要设置成PushEntity的type字段，用于统一服务端的封装与前端的解析匹配，
 *       不同类型消息的具体JSON内容设置成为PushEntity的jsonData参数
 * @author pang
 *
 */
public class PushTypeConst {

	// ���۷�����Ϣ
	public static final String comment_to_share = "0";
	// �ظ�����
	public static final String reply_to_comment = "1";
	// ��ĳ�˹�ע
	public static final String focused_by_someone = "2";
	// ������Ϣ�����?����ƽ̨
	public static final String share_introduced_to_3part = "3";

}
