package com.datapush.buyhand;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;








import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.NewJSONAccessor;
import com.datapush.buyhand.activity.LoadingActivity;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Constants;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.util.CommonUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

public class BuyHandAppApplication extends Application{
	
	   private static final String TAG = BuyHandAppApplication.class.getName();

	    // 启动的Activity集合
	    public static List<Activity> mActivityList = new ArrayList<Activity>();

//	    public static volatile DatabaseHelper mDataBaseHelper;
	    
	    public static boolean ifChatting;

	    /**
	     * 程序启动时的处理
	     * 
	     * @see android.app.Application#onCreate()
	     */
	    @Override
	    public void onCreate() {
	        super.onCreate();
	        // 出现应用级异常时的处理
	        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

	            @Override
	            public void uncaughtException(Thread thread, Throwable throwable) {
	                new Thread(new Runnable() {

	                    @Override
	                    public void run() {
	                        // 弹出报错并强制退出的对话框
	                        if (mActivityList.size() > 0) {
	                            Looper.prepare();
	                            new AlertDialog.Builder(getCurrentActivity())
	                                    .setTitle(R.string.app_name)
	                                    .setMessage(R.string.err_fatal)
	                                    .setPositiveButton(R.string.confirm,
	                                            new DialogInterface.OnClickListener() {

	                                                @Override
	                                                public void onClick(DialogInterface dialog,
	                                                        int which) {
	                                                    // 强制退出程序
	                                                    finish();
	                                                }
	                                            }).show();
	                            Looper.loop();
	                        }
	                    }
	                }).start();

	                // 错误LOG
	                Log.e(TAG, throwable.getMessage(), throwable);
	            }
	        });

	        start();
	    }

	    // 生成Activity存入列表
	    public static void addCurrentActivity(Activity activity) {
	        mActivityList.add(activity);
	    }

	    // 获取当前Activity对象
	    public static  Activity getCurrentActivity() {
	        if (mActivityList.size() > 0) {
	            return mActivityList.get(mActivityList.size() - 1);
	        }
	        return null;
	    }

	    // 清空Activity列表
	    public static void clearActivityList() {
	        for (int i = 0; i < mActivityList.size(); i++) {
	            Activity activity = mActivityList.get(i);
	            activity.finish();
	        }

	        mActivityList.clear();
	    }

	    // 退出程序处理
	    public static void finish() {

	        // 关闭程序所有的Activity
	        clearActivityList();

	        // 清理临时文件
	        CommonUtils.deleteTempFile();

	        // 退出
	        System.exit(0);

	    }

	    private void initDataBaseHelper() {
//	        if (mDataBaseHelper == null)
//	            mDataBaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
	    }

	    /**
	     * 启动程序时的处理
	     */
	    public void start() {
	        // 获得屏幕高度（像素）
	        Settings.DISPLAY_HEIGHT = getResources().getDisplayMetrics().heightPixels;
	        // 获得屏幕宽度（像素）
	        Settings.DISPLAY_WIDTH = getResources().getDisplayMetrics().widthPixels;
	        // 获得系统状态栏高度（像素）
	        Settings.STATUS_BAR_HEIGHT = getStatusBarHeight();

	        // 文件路径设置
	        String parentPath = null;

	        // 存在SDCARD的时候，路径设置到SDCARD
	        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
	            parentPath =
	                    Environment.getExternalStorageDirectory().getPath()
	                            + File.separator + getPackageName();

	            // 不存在SDCARD的时候，路径设置到ROM
	        } else {
	            parentPath = Environment.getDataDirectory().getPath() + "/data/" + getPackageName();

	        }

	        // 临时文件路径设置
	        Settings.TEMP_PATH = parentPath + "/tmp";
	        // 图片缓存路径设置
	        Settings.PIC_PATH = parentPath + "/pic";
	        // 更新APK路径设置
	        Settings.VOICE_PATH = parentPath + "/voice";

	        // 创建各目录
	        new File(Settings.TEMP_PATH,".nomedia").mkdirs();
	        new File(Settings.PIC_PATH,".nomedia").mkdirs();
	        new File(Settings.VOICE_PATH).mkdir();
	        // new File(Settings.VOICE_PATH, ".nomedia").mkdir();
	        
	        AppContents.init(this);
	        
	        initDataBaseHelper();
	    }

	    // 获取状态栏高度
	    private int getStatusBarHeight() {
	        try {
	            Class<?> cls = Class.forName("com.android.internal.R$dimen");
	            Object obj = cls.newInstance();
	            Field field = cls.getField("status_bar_height");
	            int x = Integer.parseInt(field.get(obj).toString());
	            return getResources().getDimensionPixelSize(x);
	        } catch (Exception e) {}
	        return 0;
	    }
	    public void UpdateCategory(){
	    	new CategoryTask().execute();
	    }
	    
		//获取类目所有数据
		private class CategoryTask extends AsyncTask<String, Void, String> {
			
			@Override
			protected String doInBackground(String... params) {
				
				NewJSONAccessor mAccessor = new NewJSONAccessor(getApplicationContext(), HttpAccessor.METHOD_POST);
				return mAccessor.execute(Settings.BUYERALL, Object.class);
			}
			
			@SuppressLint("CommitPrefEdits")
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					SharedPreferences preferences = getApplicationContext().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
			        Editor editor = preferences.edit();
					String str = preferences.getString("category", "");
					if(result.equals(str)){
						return;
					}else{
				        editor.putString("category", result);
				        editor.commit();
					}
				} else {
//					Toast.makeText(LoadingActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
				}
			}
		}
		
}
