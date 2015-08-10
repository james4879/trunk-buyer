package com.datapush.buyhand.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.BaseResult;
import com.datapush.buyhand.net.data.ShareTopicParam;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class BuyerShare {

	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	Context context;

	String info;

	String imagePath;
	
	String title;
	
	String userId;
	
	String content;

	int index = 5;

	SHARE_MEDIA media;
	
	OnCallBack callback;

	public BuyerShare(Context context, String info, String imagePath, int index) {
		this.context = context;
		this.info = info;
		this.imagePath = imagePath;
		this.index = index;
		initShare();
		switch (index) {
		case 1:
			// sina
			this.media = SHARE_MEDIA.SINA;
			break;
		case 2:
			// weixin
			this.media = SHARE_MEDIA.WEIXIN;
			break;
		case 3:
			// weixin_circle
			this.media = SHARE_MEDIA.WEIXIN_CIRCLE;
			break;
		case 4:
			// tencent
			this.media = SHARE_MEDIA.TENCENT;
			break;
		case 5:

			break;
		default:
			break;
		}
	}
	/**
	 * 
	 * @param context
	 * @param title
	 * @param userId
	 * @param imageIds
	 * @param info
	 * @param callback 回调
	 */
	public BuyerShare(Context context, String title, String userId,
			String imageIds, String info,OnCallBack callback) {
		this.callback = callback;
		this.context = context;
		this.title = title;
		this.userId = userId;
		this.imagePath = imageIds;
		this.content = info;
	}
	
	class ShareTask extends AsyncTask<String, Void, BaseResult>{
		
		@Override
		protected void onPostExecute(BaseResult result) {
			if(result != null && result.getCode() == 0){
				callback.callback();
			}
			super.onPostExecute(result);
		}

		@Override
		protected BaseResult doInBackground(String... url) {
			JSONAccessor ac = new JSONAccessor(context, HttpAccessor.METHOD_GET);
			ShareTopicParam param = new ShareTopicParam();
			param.setUserId(userId);
			param.setTitle(title);
			param.setContent(content);
			param.setImageIds(imagePath);
			return ac.execute(url[0], param, BaseResult.class);
		}
		
	}
	
	
	
	public static abstract class OnCallBack{
		public abstract void callback();
	}

	public void share() {
		if (this.index != 5) {
			if(!OauthHelper.isAuthenticated(context, media)){
				authVerify();
			}
			mController.setShareContent(this.info);
			mController.setShareMedia(new UMImage(context, this.imagePath));
			// 开始分享
			mController.directShare(context, media, new SnsPostListener() {
				@Override
				public void onStart() {
					Toast.makeText(context, "begin", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode,
						SocializeEntity entity) {
					if (eCode == StatusCode.ST_CODE_SUCCESSED) {
						Toast.makeText(context, "success", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "failed", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
		}else if(index == 5){
			//讨论区
			ShareTask task = new ShareTask();
			task.execute(Settings.SHARE);
		}
	}

	public void initShare() {
		mController.getConfig().removePlatform(SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE);
		// 新浪 微信 朋友圈 腾讯微博
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.TENCENT);
		addWXPlatform();
	}

	// 授权
	public void authVerify() {
		mController.doOauthVerify(context, media, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
				Toast.makeText(context, "授权开始", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				Toast.makeText(context, "授权完成", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
			}
		});
	}

	// 添加微信分享
	public void addWXPlatform() {
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wxb08477757e321823";
		String appSecret = "638a002bb7fa17bb35acac1490905843";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
}
