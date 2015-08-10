package com.datapush.buyhand.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.BitmapUtils;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.util.FileUtils;
import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.LoginParam;
import com.datapush.buyhand.net.data.UpdataPersonalParam;
import com.datapush.buyhand.net.data.UpdataPersonalResult;
import com.datapush.buyhand.net.data.UploadFileResult;
import com.datapush.buyhand.util.BitmapUtil;
import com.datapush.buyhand.util.CacheImageLoader;
import com.datapush.buyhand.view.UploadFileTask;

public class PersonalActivity extends BaseActivity {
	private ImageView mPost;
	private ImageView mBack;// 返回
	private ImageView mHead;// 头像
	private EditText mNikeName;// 昵称
	private ImageView mStyleImage1, mStyleImage2, mStyleImage3;// 风格
	private ImageView mStyleImage4, mStyleImage5, mStyleImage6;// 风格
	private ImageView mStyleImage7, mStyleImage8, mStyleImage9;// 风格
	private String[] mStylestr = new String[9];
	private Dialog mPohotoDg;
	private final int CAMERA = 1001;//相机回调码
	private final int LOCAL = 1002;//本地回调码
	private final int CROP = 1003;//截取图片回调码
	private String mUuuid;
	private static final String PHOTO_TEMP_FILE = "Image.jpg";
	private static final String PHOTO_TEMP_THUMBNAIL = "Thumbnail.jpg";
	private File OldFile,ThumbnailFile;//要上传的图片地址
	private String mAttachId = "";
	private CacheImageLoader mCacheImageLoader;//图片下载共通

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_layout);
		findView();
		initView();
		getData();
	}

	public void findView() {
		mCacheImageLoader = new CacheImageLoader(PersonalActivity.this);
		mPost = (ImageView) findViewById(R.id.post);
		mBack = (ImageView) findViewById(R.id.back);
		mHead = (ImageView) findViewById(R.id.head);
		mNikeName = (EditText) findViewById(R.id.nikename);
		mStyleImage1 = (ImageView) findViewById(R.id.img1);
		mStyleImage2 = (ImageView) findViewById(R.id.img2);
		mStyleImage3 = (ImageView) findViewById(R.id.img3);
		mStyleImage4 = (ImageView) findViewById(R.id.img4);
		mStyleImage5 = (ImageView) findViewById(R.id.img5);
		mStyleImage6 = (ImageView) findViewById(R.id.img6);
		mStyleImage7 = (ImageView) findViewById(R.id.img7);
		mStyleImage8 = (ImageView) findViewById(R.id.img8);
		mStyleImage9 = (ImageView) findViewById(R.id.img9);
	}
  
	public void initView(){
		mNikeName.setText(AppContents.getUserInfo().getUserName());
		if(AppContents.getUserInfo().getHeadPic() != null && AppContents.getUserInfo().getHeadPic().length() > 0){
		mCacheImageLoader.loadImage(AppContents.getUserInfo().getHeadPic(), mHead, new OnLoadListener() {
			
			@Override
			public void onLoad(Bitmap bitmap, View targetView) {
				if(bitmap != null){
					((ImageView) targetView).setImageBitmap(BitmapUtil.getRoundBitmap(bitmap)); 
				}
			}
		});
		}
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		mPost.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				new UpdataPersonal().execute();
			}
		});
		mHead.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				getDialog();
			}
		});
		mStyleImage1.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				setStyle(v.getId());
				
			}
		});
		mStyleImage2.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				setStyle(v.getId());
				
			}
		});
		mStyleImage3.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				
				setStyle(v.getId());
			}
		});
		mStyleImage4.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				setStyle(v.getId());
				
			}
		});
		mStyleImage5.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				
				setStyle(v.getId());
				
			}
		});
		mStyleImage6.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				
				setStyle(v.getId());
				
			}
		});
		mStyleImage7.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				setStyle(v.getId());
				
			}
		});
		mStyleImage8.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				setStyle(v.getId());
				
			}
		});
		mStyleImage9.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				setStyle(v.getId());
				
			}
		});
	}
	//初次进出赋值
	public void getData(){
		mStylestr = AppContents.getUserInfo().getUserStyle().split("A");
		if(mStylestr[0].equals("0")){
			mStyleImage1.setSelected(false);
		}else{
			mStyleImage1.setSelected(true);
		}
		
		if(mStylestr[1].equals("0")){
			mStyleImage2.setSelected(false);
		}else{
			mStyleImage2.setSelected(true);
		}
		
		if(mStylestr[2].equals("0")){
			mStyleImage3.setSelected(false);
		}else{
			mStyleImage3.setSelected(true);
		}
		
		if(mStylestr[3].equals("0")){
			mStyleImage4.setSelected(false);
		}else{
			mStyleImage4.setSelected(true);
		}
		
		if(mStylestr[4].equals("0")){
			mStyleImage5.setSelected(false);
		}else{
			mStyleImage5.setSelected(true);
		}
		
		if(mStylestr[5].equals("0")){
			mStyleImage6.setSelected(false);
		}else{
			mStyleImage6.setSelected(true);
		}
		
		if(mStylestr[6].equals("0")){
			mStyleImage7.setSelected(false);
		}else{
			mStyleImage7.setSelected(true);
		}
		
		if(mStylestr[7].equals("0")){
			mStyleImage8.setSelected(false);
		}else{
			mStyleImage8.setSelected(true);
		}
		
		if(mStylestr[8].equals("0")){
			mStyleImage9.setSelected(false);
		}else{
			mStyleImage9.setSelected(true);
		}
	}
	//选择图片dialog
	public void getDialog(){
		
		if(mPohotoDg != null){
			mPohotoDg = null;
		}
		View view = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.photo_dialog, null);
		mPohotoDg = new Dialog(PersonalActivity.this, R.style.dialog);
		mPohotoDg.setContentView(view);
		mPohotoDg.show();
		
		Window w = mPohotoDg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
//		lp.gravity = Gravity.TOP;
//		lp.x = (Settings.DISPLAY_WIDTH/2-Settings.DISPLAY_WIDTH)/2;
//		lp.y = 0;
		lp.width = Settings.DISPLAY_WIDTH/2;
		w.setAttributes(lp);
		
		RelativeLayout local = (RelativeLayout) view.findViewById(R.id.local);
		RelativeLayout camera = (RelativeLayout) view.findViewById(R.id.camera);
		
		//本地图片获取
		local.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				mPohotoDg.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, LOCAL);
			}
		});
		//相机调取
		camera.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				mPohotoDg.dismiss();
				mUuuid = UUID.randomUUID().toString();
				File tempFile = new File(Settings.TEMP_PATH, mUuuid + PHOTO_TEMP_FILE);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				startActivityForResult(intent, CAMERA);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (CAMERA == requestCode && RESULT_OK == resultCode) {

			if (data != null && data.hasExtra("data")) {
				Bitmap photo = data.getParcelableExtra("data");
				OldFile = new File(Settings.TEMP_PATH, mUuuid + PHOTO_TEMP_FILE);
				saveBitmapToFile(photo, OldFile);
				cropPhoto(photo);
			} else {
				OldFile = new File(Settings.TEMP_PATH, mUuuid + PHOTO_TEMP_FILE);
				if (OldFile.exists()) {
					cropPhoto(OldFile);
				}
			}
		}
		if (LOCAL == requestCode && RESULT_OK == resultCode) {

			Uri uri = data.getData();
			mUuuid = UUID.randomUUID().toString();
			String path = null;

			if ("file".equals(uri.getScheme())) {
				path = uri.getPath();
			} else if ("content".equals(uri.getScheme())) {
				Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
				if (cursor.moveToFirst())
					path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			}

			if (path != null && path.length() > 0) {
				OldFile = new File(Settings.TEMP_PATH, mUuuid + PHOTO_TEMP_FILE);
				if (FileUtils.copyFile(new File(path), OldFile)) {
					cropPhoto(OldFile);
				}
			}
		}
		
		if (CROP == requestCode && RESULT_OK == resultCode) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = data.getParcelableExtra("data");
				ThumbnailFile = new File(Settings.TEMP_PATH, mUuuid + PHOTO_TEMP_THUMBNAIL);
				saveBitmapToFile(photo, ThumbnailFile);
				new UpdateTask(PersonalActivity.this, ThumbnailFile.getName()).execute(ThumbnailFile.getPath(),"");
			}
		}

	}
	//风格唯一性
	public void setStyle(int resid){
		ResetStr();
		mStyleImage1.setSelected(false);
		mStyleImage2.setSelected(false);
		mStyleImage3.setSelected(false);
		mStyleImage4.setSelected(false);
		mStyleImage5.setSelected(false);
		mStyleImage6.setSelected(false);
		mStyleImage7.setSelected(false);
		mStyleImage8.setSelected(false);
		mStyleImage9.setSelected(false);
		
		switch (resid) {
		case R.id.img1:
			mStyleImage1.setSelected(true);
			mStylestr[0] = "1";
			break;
		case R.id.img2:
			mStyleImage2.setSelected(true);
			mStylestr[1] = "1";
			break;
		case R.id.img3:
			mStyleImage3.setSelected(true);
			mStylestr[2] = "1";
			break;
		case R.id.img4:
			mStyleImage4.setSelected(true);
			mStylestr[3] = "1";
			break;
		case R.id.img5:
			mStyleImage5.setSelected(true);
			mStylestr[4] = "1";
			break;
		case R.id.img6:
			mStyleImage6.setSelected(true);
			mStylestr[5] = "1";
			break;
		case R.id.img7:
			mStyleImage7.setSelected(true);
			mStylestr[6] = "1";
			break;
		case R.id.img8:
			mStyleImage8.setSelected(true);
			mStylestr[7] = "1";
			break;
		case R.id.img9:
			mStyleImage9.setSelected(true);
			mStylestr[8] = "1";
			break;
		}
	}
	//重置数组
	public void ResetStr(){
		for(int i=0;i<mStylestr.length;i++){
			mStylestr[i] = "0";
		}
	}
	
	// 截取图片
	private void cropPhoto(Bitmap data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		intent.putExtra("data", data);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 160);
		intent.putExtra("outputY", 160);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP);
	}

	// 截取图片
	private void cropPhoto(File file) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 160);
		intent.putExtra("outputY", 160);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP);
	}

	public boolean saveBitmapToFile(Bitmap bitmap, File file) {
		BufferedOutputStream bos = null;
		try {
			File tempPicFile = new File(Settings.TEMP_PATH, FileUtils.getFileNameByPath(file.getPath()) + Settings.PICTURE_TEMP_EXTENSION);
			tempPicFile.delete();
			file.delete();

			tempPicFile.getParentFile().mkdirs();
			tempPicFile.createNewFile();

			bos = new BufferedOutputStream(new FileOutputStream(tempPicFile));
			bitmap.compress(CompressFormat.JPEG, 80, bos);

			bos.flush();
			bos.close();
			bos = null;

			return tempPicFile.renameTo(file);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
		}
		return false;
	}
	//上传头像线程
	public class UpdateTask extends UploadFileTask{

		public UpdateTask(Context context, String filename) {
			super(context, filename);
		}

		@Override
		protected void onPostExecute(UploadFileResult result) {
			
			if(result!=null){
						if(result.getCode() == 0){
							mHead.setImageBitmap(BitmapUtil.getRoundBitmap(BitmapUtils.getBitmapFromFile(
									ThumbnailFile, DensityUtils.dp2px(PersonalActivity.this, 160), 
									DensityUtils.dp2px(PersonalActivity.this, 160))));
							mAttachId = result.getAttachId();
						}else{
							
						}
			}else{
				Toast.makeText(PersonalActivity.this, getString(R.string.action_settings), Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	/**
	 * 修改资料
	 * 
	 */
	private class UpdataPersonal extends AsyncTask<String, Void, UpdataPersonalResult> {
		
		private ProgressDialog mProgressDialog;
		
		@Override
		protected void onPreExecute() {
			
				mProgressDialog = new ProgressDialog(PersonalActivity.this);
				mProgressDialog.setTitle(R.string.app_name);
				mProgressDialog.setMessage(getString(R.string.tijiao));
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
		}
		
		@Override
		protected UpdataPersonalResult doInBackground(String... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(PersonalActivity.this, HttpAccessor.METHOD_POST);
			UpdataPersonalParam param = new UpdataPersonalParam();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setUserName(mNikeName.getText().toString().trim());
			if(mAttachId.length() >0){
			param.setAttachId(mAttachId);
			}
			param.setUserStyle(MosaicStr());
			return mAccessor.execute(Settings.UPDATAPERSONAL, param, UpdataPersonalResult.class);
		}
		
		@Override
		protected void onPostExecute(UpdataPersonalResult result) {
			mProgressDialog.dismiss();
			if (result != null && result.getCode() == 0) {
					finish();
					AppContents.getUserInfo().setUserName(result.getUserinfo().getUserName());
					AppContents.getUserInfo().setHeadPic(result.getUserinfo().getHeadPic());
					AppContents.getUserInfo().setUserStyle(result.getUserinfo().getUserStyle());
			} else {
				Toast.makeText(PersonalActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}

		}

	}
	/**
	 * 拼接字符串
	 */
	
	public String MosaicStr(){
		String str = "";
		for (int i = 0; i < mStylestr.length; i++) {
			if(i == mStylestr.length-1){
				str += mStylestr[i];
			}else{
			str += mStylestr[i] +"A";
			}
		}
		return str;
	}
	
}
