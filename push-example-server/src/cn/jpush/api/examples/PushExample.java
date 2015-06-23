package cn.jpush.api.examples;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class PushExample {
	protected static final Logger LOG = LoggerFactory
			.getLogger(PushExample.class);

	// demo App defined in resources/jpush-api.conf
	private static final String appKey = "2f2bbdcd69bf3749b8577f22";
	private static final String masterSecret = "88495ca3f173c3a7a86853af";

	public static final String TITLE = "消息标题";
	public static final String ALERT = "警报信息";
	public static final String MSG_CONTENT = "消息内容哦，妹子";
	public static final String REGISTRATION_ID = "0900e8d85ef";
	public static final String TAG = "tag_api";

	public static void main(String[] args) {
		testSendPush();
	}

	public static void testSendPush() {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload = push_zcinfo_to_user("5534bbf219ba8f03c39a50b7",
				"吃对食物抗过敏 不做“敏感”之人", "ziLegfyjxjGcarEUtjZu6BN8SYyJYK7G");

		try {
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);

		} catch (APIRequestException e) {
			LOG.error(
					"Error response from JPush server. Should review and fix it. ",
					e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
		}
	}

	public static PushPayload push_zcinfo_to_user(String zcId, String zcTitle,
			String userId) {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("zcId", zcId);
		maps.put("zcTitle", zcTitle);
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.alias(userId))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert("最新健康消息：" + zcTitle)
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.addExtras(maps)
												.setTitle("新消息").build())
								.addPlatformNotification(
										IosNotification.newBuilder()
												.incrBadge(1).setAlert("新消息")
												.addExtras(maps).build())
								.build()).build();
	}

	public static PushPayload buildPushObject_all_alias_alert(String alias) {
		return PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.alias(alias))
				.setNotification(Notification.alert(ALERT)).build();
	}

	public static PushPayload buildPushObject_all_all_alert() {
		return PushPayload.alertAll(ALERT);
	}

	public static PushPayload buildPushObject_all_alias_alert() {
		return PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.alias("alias1"))
				.setNotification(Notification.alert(ALERT)).build();
	}

	public static PushPayload buildPushObject_android_tag_alertWithTitle() {
		return PushPayload.newBuilder().setPlatform(Platform.android())
				.setAudience(Audience.tag("tag1"))
				.setNotification(Notification.android(ALERT, TITLE, null))
				.build();
	}

	public static PushPayload buildPushObject_all_tag_alertWithTitle(String tag1) {
		return PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.tag(tag1))
				.setNotification(Notification.android(ALERT, TITLE, null))
				.build();
	}

	public static PushPayload buildPushObject_android_and_ios() {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.tag("tag1"))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert("alert content")
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setTitle("Android Title")
												.build())
								.addPlatformNotification(
										IosNotification
												.newBuilder()
												.incrBadge(1)
												.addExtra("extra_key",
														"extra_value").build())
								.build()).build();
	}

	public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.tag_and("tag1", "tag_all"))
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										IosNotification.newBuilder()
												.setAlert(ALERT).setBadge(5)
												.setSound("happy")
												.addExtra("from", "JPush")
												.build()).build())
				.setMessage(Message.content(MSG_CONTENT))
				.setOptions(
						Options.newBuilder().setApnsProduction(true).build())
				.build();
	}

	public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(
						Audience.newBuilder()
								.addAudienceTarget(
										AudienceTarget.tag("tag1", "tag2"))
								.addAudienceTarget(
										AudienceTarget
												.alias("alias1", "alias2"))
								.build())
				.setMessage(
						Message.newBuilder().setMsgContent(MSG_CONTENT)
								.addExtra("from", "JPush").build()).build();
	}

}
