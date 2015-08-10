package com.datapush.buyhand.common;

public class Settings {
	
	public static int DELAY_SHUTTER_TIME;
	
	// 屏幕高度
	public static int DISPLAY_HEIGHT;
	// 屏幕宽度
	public static int DISPLAY_WIDTH;
	// 状态栏高度
	public static int STATUS_BAR_HEIGHT;

	// 临时文件路径
	public static String TEMP_PATH = "";
	// 缓存图片路径
	public static String PIC_PATH = "";
	// 缓存语音路径
	public static String VOICE_PATH = "";
	// 缓存最小阀值
	public static final int SAVE_SIZE_MIN = 100;
	// 缓存最大阀值
	public static final int SAVE_SIZE_MAX = 200;
	
	public static final String PICTURE_TEMP_EXTENSION = ".tmp";

	public static final int CAMERA = 1001;
	public static final int ALBUM = 1002;
	public static final int CROP = 1003;

	public static final String BASE_URL = "http://115.28.134.173:9080/buyer_server/";
//	public static final String BASE_URL = "http://172.27.35.1:8080/buyer_server/";


//	public static final String USER_URL = BASE_URL + "UserServlet";
    // 更新文件路径
    public static String APK_SAVE = "";
	
	public static final String FASHIONLIST = BASE_URL+"goods/buyerallgoodsjson/obtainGoodsList.do";
	public static final String MYCOECTGOODS = BASE_URL+"appuser/buyeruserjson/getMyCollectGoods.do";
	public static final String LOGIN = BASE_URL+"appuser/buyeruserjson/loginPhoneNumber.do";
	public static final String UPDATAPERSONAL = BASE_URL+"appuser/buyeruserjson/modifyUser.do";
	public static final String FILE_URL = BASE_URL+"servlet/upload";
	public static final String TRIPLIST = BASE_URL+"appuser/buyerplancategoryjson/getPlanCategoryList.do";//行程目录接口
	public static final String ADDTRIP = BASE_URL+"appuser/buyerplancategoryjson/addPlanCategory.do";//添加行程目录接口
	public static final String CARALOGLIST = BASE_URL+"appuser/buyerplancategoryjson/getPlanList.do";//行程小目录
	public static final String ADDCATALOG = BASE_URL+"appuser/buyerplanjson/addPlan.do";//行程小目录
	public static final String REGISTER = BASE_URL + "appuser/buyeruserjson/registerPhoneNumber.do";
	public static final String GET_AUTH_CODE = BASE_URL + "appuser/buyeruserjson/reqCaptcha.do";
    public static final String CLOTHING_LIST = BASE_URL + "goods/buyerclothingjson/getClothingList.do";//添衣主页List
    public static final String CHEST_LIST = BASE_URL + "goods/buyerclothingchestjson/getChestList.do";//取到所有衣柜
    public static final String WARDOBELIST = BASE_URL + "goods/buyerclothingjson/getClothingForChest.do";//取到衣柜宝贝
    public static final String STORAGE = BASE_URL + "goods/buyerclothingjson/storageClothing.do";//添加到衣柜
    public static final String GET_CLOTHING = BASE_URL + "goods/buyerclothingjson/getClothingById.do";//查询添衣宝贝
    public static final String UPDATANAME = BASE_URL + "goods/buyerclothingchestjson/modifyChest.do";//修改衣柜名称
    public static final String WARDROBELIST = BASE_URL + "goods/buyerclothingchestjson/getClothingListByChest.do";//
    public static final String TOPICLIST = BASE_URL + "topic/buyertopicjson/getTopicList.do";//
    public static final String TOPICDETAIL = BASE_URL + "topic/buyertopicjson/getTopicDetail.do";//议题详情
    public static final String POST = BASE_URL + "topic/buyertopicjson/praiseOrStep.do";//提交
    public static final String CANCEL = BASE_URL + "topic/buyertopicjson/cancelPraiseOrStep.do";//取消
    public static final String COMMENTLIST = BASE_URL + "topic/buyertopicjson/getTopicCommentList.do";//获取评论
    public static final String POSTCOMMENT = BASE_URL + "topic/buyertopicjson/addComment.do";//提交评论
    public static final String COLLECT = BASE_URL + "topic/buyertopicjson/collectOrCancel.do";//收藏
    public static final String SCREENSHOT = BASE_URL + "match/buyermatchcategoryjson/getMatchCategoryList.do";//搭配类目
    public static final String ADDDAPEI = BASE_URL + "goods/buyerclothingjson/getClothingForMatch.do";//获取所有搭配item
    public static final String ADDCATEGORY = BASE_URL + "match/buyermatchcategoryjson/addCategory.do";//
    public static final String MATCHLIST = BASE_URL + "match/buyermatchjson/getMatchList.do";//获取我的搭配列表
    public static final String SHARE = BASE_URL + "topic/buyertopicjson/launchTopic.do";//分享到讨论区
    public static final String MYCOLLECT = BASE_URL + "appuser/buyeruserjson/getMyCollectTopic.do";//获取我的收藏
    public static final String PERSONAL = BASE_URL + "personal/buyerpersonaljson/getPersonalMessage.do";//
    public static final String BUYERALL = BASE_URL + "goods/buyerallgoodsjson/getBasicData.do";//
    public static final String GOOD_COLLECTION = BASE_URL + "goods/buyerallgoodsjson/collectOrCancel.do";//
    public static final String CAPTURE = BASE_URL + "seller/qrcode/sendLoginDate.do";//
    
}
