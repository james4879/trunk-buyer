package com.datapush.buyhand.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.util.FileUtils;
import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.AddClothingParam;
import com.datapush.buyhand.net.data.AddClothingResult;
import com.datapush.buyhand.net.data.ClothingDetailMessage;
import com.datapush.buyhand.net.data.ClothingMessage;
import com.datapush.buyhand.net.data.ClothingParam;
import com.datapush.buyhand.util.CacheImageLoader;

/**
 * 添衣
 * 
 * @author yb
 * 
 */
public class AddClothingActivity extends BaseActivity {

	public static final String TAG = "AddClothingActivity";

	public static final String FLAG = "Detail";

	public String callBackFlag = "";

	public LinearLayout mClothingCategory;// 目录

	public ImageView backIv;// 返回

	public String categoryId = "101";// 类目ID

	public GridView gvClothing;

	public String[] categorys = { "上衣", "下装", "打底", "连衣裙", "女鞋", "配饰", "其他" };

	public String[] categoryIds = { "101", "102", "103", "104", "105", "106",
			"107" };

	public int chooseIndex = 0;// 选中的类目

	public String newAddClothingId = "";// 新增的宝贝ID

	public int oldIndex = 0;
	public GvClothingAdapter adapter;

	public List<ClothingDetailMessage> clothingData = new ArrayList<ClothingDetailMessage>();// 加载的数据

	public CacheImageLoader imageLoader;
	private Dialog mPohotoDg;
	private final int CAMERA = 1001;// 相机回调码
	private final int LOCAL = 1002;// 本地回调码
	private final int CROP = 1003;// 截取图片回调码
	private final int DETAIL = 1004;// 添衣回调码
	private String mUuuid;
	private static final String PHOTO_TEMP_FILE = "Image.jpg";
	private static final String PHOTO_TEMP_THUMBNAIL = "Thumbnail.jpg";
	private File OldFile, ThumbnailFile;// 要上传的图片地址
	private ImageView mHead;// 头像

	private ProgressDialog pd;

	private int from = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addclothing_layout);
		from = getIntent().getIntExtra("from", 0);
		init();
		initView();
	}

	@Override
	protected void onResume() {
		// clothingData.clear();
		// loadData();
		super.onResume();
	}

	public void loadData() {
		clothingData.clear();
		AddClothingTask task = new AddClothingTask();
		task.execute();
	}

	public void init() {
		mClothingCategory = (LinearLayout) findViewById(R.id.all_category);
		backIv = (ImageView) findViewById(R.id.back);
		gvClothing = (GridView) findViewById(R.id.gv_clothing);
		imageLoader = new CacheImageLoader(this);
	}

	@SuppressLint("ResourceAsColor")
	public void initView() {

		gvClothing.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (from == 1) {
					Intent intent = new Intent();
					intent.setClass(AddClothingActivity.this,
							HelpMeChooseActivity.class);
					intent.putExtra("imagePath", clothingData.get(position)
							.getImagePath());
					intent.putExtra("id", clothingData.get(position)
							.getImageId());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					if (clothingData.get(position).getId().equals("0")) {
						getDialog();
					}
				}
			}
		});

		List<Integer> clothingList = new ArrayList<Integer>();
		clothingList.add(R.drawable.c1);
		clothingList.add(R.drawable.c2);
		clothingList.add(R.drawable.c3);
		clothingList.add(R.drawable.c4);
		clothingList.add(R.drawable.c5);
		clothingList.add(R.drawable.c6);
		// GvClothingAdapter adapter = new GvClothingAdapter(this,
		// clothingList);
		// gvClothing.setAdapter(adapter);
		adapter = new GvClothingAdapter();
		gvClothing.setAdapter(adapter);
		backIv.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View view) {
				finish();
				overridePendingTransition(R.anim.in_form_left,
						R.anim.out_from_left);
			}
		});
		for (int i = 0; i < categorys.length; i++) {
			final int index = i;
			Button tv = new Button(AddClothingActivity.this);
			int childWidth = -1, childHeight = -1;

			int parentWidth = mClothingCategory.getWidth();
			childWidth = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(
					AddClothingActivity.this, 3)) / 4;
			childHeight = DensityUtils.dp2px(AddClothingActivity.this, 37);
			LayoutParams lp = new LayoutParams(childWidth, childHeight);
			lp.leftMargin = 0;
			lp.gravity = Gravity.CENTER;
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.WHITE);
			if (i == 0) {
				tv.setBackgroundResource(R.drawable.add_cl_category_choosed);
			} else {
				tv.setBackgroundResource(R.drawable.add_cl_category);
			}
			tv.setText(categorys[i]);
			tv.setOnClickListener(new OnSingleClickListener() {

				@Override
				public void doOnClick(View v) {
					chooseIndex = index;
					if (2 * index != oldIndex) {
						mClothingCategory.getChildAt(2 * index)
								.setBackgroundResource(
										R.drawable.add_cl_category_choosed);
						mClothingCategory.getChildAt(oldIndex)
								.setBackgroundResource(
										R.drawable.add_cl_category);
						oldIndex = 2 * index;
					}
					loadData();
				}

			});
			mClothingCategory.addView(tv, lp);
			ImageView iv = new ImageView(AddClothingActivity.this);
			lp = new LayoutParams(DensityUtils.dp2px(AddClothingActivity.this,
					1), DensityUtils.dp2px(AddClothingActivity.this, 33));
			lp.gravity = Gravity.CENTER_VERTICAL;
			iv.setBackgroundResource(R.drawable.tp_line_bg);
			mClothingCategory.addView(iv, lp);
		}
		loadData();
	}

	class GvClothingAdapter extends BaseAdapter {

		class ViewHolder {
			ImageView iv;
		}

		@Override
		public int getCount() {

			return clothingData.size();
		}

		@Override
		public Object getItem(int index) {
			return index;
		}

		@Override
		public long getItemId(int index) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int index = position;
			ViewHolder holder = null;
			if (convertView == null) {
				if (position == 0) {
					parent.removeAllViewsInLayout();
				}
				convertView = LayoutInflater.from(AddClothingActivity.this)
						.inflate(R.layout.add_clothing_item, parent, false);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position >= 0 && clothingData.get(position) != null
					&& !clothingData.get(position).getId().equals("0")) {

				if (clothingData.get(position).getImagePath() != null
						&& clothingData.get(position).getImagePath().length() > 0) {
//					Log.v(TAG, "正在执行...,position=" + position + ",id="
//							+ clothingData.get(position).getId()
//							+ ",parent.count=" + parent.getChildCount()
//							+ "data.count=" + clothingData.size());
					holder.iv.setImageBitmap(BitmapFactory
							.decodeStream(getResources().openRawResource(
									R.drawable.pohoto_bg)));
					String thumbnailPath = clothingData.get(position).getThumbnailPath();
					String imagePath = clothingData.get(position).getImagePath();
					if(!TextUtils.isEmpty(thumbnailPath)){
						imagePath = thumbnailPath;
					}
					imageLoader.loadImage(imagePath, holder.iv, new OnLoadListener() {

						@Override
						public void onLoad(Bitmap bitmap, View targetView) {
							if (bitmap != null) {
								((ImageView) targetView).setImageBitmap(bitmap);
							}
						}
					});
				}
			} else if (clothingData.get(position).getId().equals("0")) {
				Log.v(TAG,
						"正在执行...,position=" + position + ",id="
								+ clothingData.get(position).getId()
								+ ",parent.count=" + parent.getChildCount()
								+ "data.count=" + clothingData.size());
				holder.iv.setImageBitmap(BitmapFactory
						.decodeStream(getResources().openRawResource(
								R.drawable.c6)));
			}

			// holder.iv.setOnClickListener(new OnSingleClickListener() {
			//
			// @Override
			// public void doOnClick(View v) {
			// if(clothingData.get(index).getId().equals("0")){
			// getDialog();
			// }else{
			// //进到详情
			//
			//
			// }
			// }
			// });

			int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(
					AddClothingActivity.this, 2)) / 3;

			GridView.LayoutParams pl = new GridView.LayoutParams(w, w * 3 / 2);
			convertView.setLayoutParams(pl);
			return convertView;
		}
	}

	// 选择图片dialog
	private void getDialog() {
		if (mPohotoDg != null) {
			mPohotoDg = null;
		}
		View view = LayoutInflater.from(AddClothingActivity.this).inflate(
				R.layout.pohoto_item_clothing, null);
		mPohotoDg = new Dialog(AddClothingActivity.this, R.style.dialog);
		mPohotoDg.setContentView(view);
		mPohotoDg.show();

		Window w = mPohotoDg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		// lp.gravity = Gravity.TOP;
		// lp.x = (Settings.DISPLAY_WIDTH/2-Settings.DISPLAY_WIDTH)/2;
		// lp.y = 0;
		lp.width = Settings.DISPLAY_WIDTH / 2;
		w.setAttributes(lp);

		RelativeLayout local = (RelativeLayout) view.findViewById(R.id.local);
		RelativeLayout camera = (RelativeLayout) view.findViewById(R.id.camera);

		// 本地图片获取
		local.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {

				mPohotoDg.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, LOCAL);
			}
		});
		// 相机调取
		camera.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {

				mPohotoDg.dismiss();
				mUuuid = UUID.randomUUID().toString();
				File tempFile = new File(Settings.TEMP_PATH, mUuuid
						+ PHOTO_TEMP_FILE);
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
				// cropPhoto(photo);
				Intent intent = new Intent(AddClothingActivity.this,
						AddClothingDetailActivity.class);
				intent.putExtra("imagePath", OldFile.getAbsolutePath());
				intent.putExtra("categoryId", categoryIds[chooseIndex]);
				// startActivity(intent);
				startActivityForResult(intent, DETAIL);
			} else {
				OldFile = new File(Settings.TEMP_PATH, mUuuid + PHOTO_TEMP_FILE);
				if (OldFile.exists()) {
					// cropPhoto(OldFile);
					Intent intent = new Intent(AddClothingActivity.this,
							AddClothingDetailActivity.class);
					intent.putExtra("imagePath", OldFile.getAbsolutePath());
					intent.putExtra("categoryId", categoryIds[chooseIndex]);
					// startActivity(intent);
					startActivityForResult(intent, DETAIL);
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
				Cursor cursor = this.getContentResolver().query(uri, null,
						null, null, null);
				if (cursor.moveToFirst())
					path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
			}

			if (path != null && path.length() > 0) {
				String extend = path.substring(path.lastIndexOf("."));
				OldFile = new File(Settings.TEMP_PATH, mUuuid + extend);
				if (FileUtils.copyFile(new File(path), OldFile)) {
					// cropPhoto(OldFile);
					Intent intent = new Intent(AddClothingActivity.this,
							AddClothingDetailActivity.class);
					intent.putExtra("imagePath", OldFile.getAbsolutePath());
					intent.putExtra("categoryId", categoryIds[chooseIndex]);
					// startActivity(intent);
					startActivityForResult(intent, DETAIL);
				}
			}
		}
		// 新增回调
		if (DETAIL == requestCode && RESULT_OK == resultCode) {
			callBackFlag = getIntent().getStringExtra("flag");
			loadData();
		}

		if (CROP == requestCode && RESULT_OK == resultCode) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = data.getParcelableExtra("data");
				ThumbnailFile = new File(Settings.TEMP_PATH, mUuuid
						+ PHOTO_TEMP_THUMBNAIL);
				saveBitmapToFile(photo, ThumbnailFile);
				// new UpdateTask(AddClothingActivity.this,
				// ThumbnailFile.getName()).execute(ThumbnailFile
				// .getPath());
				Intent intent = new Intent(AddClothingActivity.this,
						AddClothingDetailActivity.class);
				intent.putExtra("imagePath", ThumbnailFile.getAbsolutePath());
				intent.putExtra("categoryId", categoryIds[chooseIndex]);
				startActivity(intent);
			}
		}
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

	public boolean saveBitmapToFile(Bitmap bitmap, File file) {
		BufferedOutputStream bos = null;
		try {
			File tempPicFile = new File(Settings.TEMP_PATH,
					FileUtils.getFileNameByPath(file.getPath())
							+ Settings.PICTURE_TEMP_EXTENSION);
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

	private class AddClothingTask extends
			AsyncTask<String, Void, ClothingMessage> {

		@Override
		protected void onPreExecute() {
			Log.v(TAG, "flag=" + callBackFlag);
			if (!FLAG.equals(callBackFlag)) {
				showProgressDialog();
			}
			super.onPreExecute();
		}

		private void showProgressDialog() {
			pd = new ProgressDialog(AddClothingActivity.this);
			pd.setTitle(getString(R.string.register_pd_title));
			pd.setMessage(getString(R.string.addClothing_msg));
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}

		@Override
		protected ClothingMessage doInBackground(String... url) {
			JSONAccessor ac = new JSONAccessor(AddClothingActivity.this,
					HttpAccessor.METHOD_GET);
			ac.enableJsonLog(true);
			ClothingParam param = new ClothingParam();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setCategoryId(categoryIds[chooseIndex]);
			return ac.execute(Settings.CLOTHING_LIST, param,
					ClothingMessage.class);
		}

		@Override
		protected void onPostExecute(ClothingMessage result) {
			pd.dismiss();
			if (result != null && result.getClothingList() != null) {
				clothingData.clear();
				clothingData.addAll(result.getClothingList());
				if (from == 1) {
					clothingData.remove(clothingData.size() - 1);
				}
				adapter.notifyDataSetChanged();
				// if(clothingData.size() > 0)
				// gvClothing.setSelection(0);
			}
			super.onPostExecute(result);
		}

	}

	// 上传头像线程
	public class UpdateTask extends AsyncTask<String, Void, AddClothingResult> {

		private JSONAccessor mAccessor;

		public UpdateTask(Context context, String fileName) {
			mAccessor = new JSONAccessor(context,
					HttpAccessor.METHOD_POST_MULTIPART);
		}

		@Override
		protected AddClothingResult doInBackground(String... imagePath) {
			File file = new File(imagePath[0]);
			if (!file.exists() || !file.isFile())
				return null;
			AddClothingParam param = new AddClothingParam();
			param.setFile(file);
			param.setCategoryId(categoryIds[chooseIndex]);
			param.setUserId(AppContents.getUserInfo().getId());
			AddClothingResult result = mAccessor.execute(Settings.FILE_URL,
					param, AddClothingResult.class);
			return result;
		}

		@Override
		protected void onPostExecute(AddClothingResult result) {
			if ("0".equals(result.getCode())) {
				newAddClothingId = result.getGoodsId();
				Intent intent = new Intent(AddClothingActivity.this,
						AddClothingDetailActivity.class);
				intent.putExtra("clothingId", newAddClothingId);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_from_left);
				// Toast.makeText(AddClothingActivity.this, "上传成功",
				// Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
}
