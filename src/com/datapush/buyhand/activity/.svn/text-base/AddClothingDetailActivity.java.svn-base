package com.datapush.buyhand.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.BitmapUtils;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.Clothing;
import com.datapush.buyhand.net.data.ClothingParam;
import com.datapush.buyhand.util.CacheImageLoader;

/**
 * 添衣 操作页面
 * 
 * @author yb
 * 
 */
public class AddClothingDetailActivity extends BaseActivity {

	private Button btnSeason;

	private Button btnStyle;

	private Button btnBrand;

	private Button btnPrice;

	private Button btnSave;

	private EditText descriptionEd;

	private TextView tvCancel;

	private ListView lvData;

	private RelativeLayout rlData;

	private String imagePath;// 图片路径

	private File imageFile;// 图片文件

	private File newImageFile;// 提交使用(相机拍照缩小后)

	private String mUuuid;

	private static final String PHOTO_TEMP_FILE = "Image.jpg";

	private ImageView clothingIv;

	private CacheImageLoader imageLoader;

	private DropDownAdapter dropDownAdapter;// 下拉列表适配器

	private int dropDownFlag;// 1 季节 2 风格 3 品牌 4 价格

	private String[] seasonIds = { "501", "502", "503", "504" };// 季节ID

	private String[] styleIds = { "301", "302", "303", "304", "305" };// 风格ID

	private String[] brandIds = { "201", "202", "203", "204", "205", "206" };// 品牌ID

	private String[] priceIds = { "401", "402", "403" };// 价格ID

	private String categoryId;

	private int choosedSeasonId, choosedStyleId, choosedBrandId,
			choosedPriceId;// 选中的属性index

	private List<String> dropDownList = new ArrayList<String>();

	private ProgressDialog pd;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// Clothing clothing = (Clothing) msg.obj;
				// imageLoader.loadImage(clothing.getImagePath(), clothingIv,
				// new OnLoadListener() {
				//
				// @Override
				// public void onLoad(Bitmap bitmap, View targetView) {
				// if (bitmap != null) {
				// ((ImageView) targetView)
				// .setImageBitmap(bitmap);
				// }
				// }
				// });
				break;
			case 1:

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clothing_layout);
		init();
		initView();
	}

	public void init() {
		btnSeason = (Button) findViewById(R.id.btn_season);
		btnStyle = (Button) findViewById(R.id.btn_style);
		btnBrand = (Button) findViewById(R.id.btn_brand);
		btnPrice = (Button) findViewById(R.id.btn_price);
		btnSave = (Button) findViewById(R.id.save);
		descriptionEd = (EditText) findViewById(R.id.et_description);
		tvCancel = (TextView) findViewById(R.id.tv_cancel);
		clothingIv = (ImageView) findViewById(R.id.iv_goods);
		lvData = (ListView) findViewById(R.id.lv_data);
		dropDownAdapter = new DropDownAdapter();
		lvData.setAdapter(dropDownAdapter);
		clothingIv.setImageResource(R.drawable.pohoto_bg);
		imageLoader = new CacheImageLoader(this);
		imagePath = getIntent().getStringExtra("imagePath");
		imageFile = new File(imagePath);
		categoryId = getIntent().getStringExtra("categoryId");
		if (!TextUtils.isEmpty(imagePath)) {
			int degree = readPictureDegree(imagePath);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			Bitmap bitmap = BitmapUtils.getBitmapFromFile(imageFile,
					Settings.DISPLAY_WIDTH, Settings.DISPLAY_HEIGHT);
			// Bitmap newBitmap = rotaingImageView(degree,
			// BitmapFactory.decodeFile(imagePath, opts));
			Bitmap newBitmap = rotaingImageView(degree, bitmap);
			createDisplayImage(newBitmap);
			clothingIv.setImageBitmap(newBitmap);
		}
	}

	/**
	 * 创建新的图片
	 */
	private void createDisplayImage(Bitmap bitmap) {
		try {
			// File file = new File(imagePath);
			mUuuid = UUID.randomUUID().toString();
			String extend = imagePath.substring(imagePath.lastIndexOf("."));
			newImageFile = new File(Settings.TEMP_PATH, mUuuid
					+ extend);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(newImageFile));
			if (!imagePath.substring(imagePath.lastIndexOf("/") + 1).toUpperCase()
					.contains("PNG")) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			}
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public void loadData() {
		ClothingTask task = new ClothingTask();
		task.execute(Settings.GET_CLOTHING);
	}

	public void initView() {
		btnSeason.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				if (dropDownFlag != 1) {
					dropDownFlag = 1;
					initLvData(v);
				} else {
					lvData.setVisibility(View.GONE);
					dropDownFlag = 0;
				}
			}

		});
		btnStyle.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				if (dropDownFlag != 2) {
					dropDownFlag = 2;
					initLvData(v);
				} else {
					lvData.setVisibility(View.GONE);
					dropDownFlag = 0;
				}
			}
		});
		btnBrand.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				if (dropDownFlag != 3) {
					dropDownFlag = 3;
					initLvData(v);
				} else {
					lvData.setVisibility(View.GONE);
					dropDownFlag = 0;
				}
			}
		});
		btnPrice.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				if (dropDownFlag != 4) {
					dropDownFlag = 4;
					initLvData(v);
				} else {
					lvData.setVisibility(View.GONE);
					dropDownFlag = 0;
				}
			}
		});
		// 取消
		tvCancel.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		// 保存
		btnSave.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				ClothingTask task = new ClothingTask();
				task.execute(Settings.FILE_URL);
			}
		});
	}

	public void initLvData(View view) {
		lvData.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams params = (LayoutParams) lvData
				.getLayoutParams();
		params.width = (Settings.DISPLAY_WIDTH - (DensityUtils.dp2px(
				AddClothingDetailActivity.this, 10)) * 3) / 4;
		params.height = Settings.DISPLAY_HEIGHT / 2;
		switch (view.getId()) {
		case R.id.btn_season:
			params.leftMargin = 0;
			break;
		case R.id.btn_style:
			params.leftMargin = params.width
					+ DensityUtils.dp2px(AddClothingDetailActivity.this, 10);
			break;
		case R.id.btn_brand:
			params.leftMargin = (params.width + DensityUtils.dp2px(
					AddClothingDetailActivity.this, 10)) * 2;
			break;
		case R.id.btn_price:
			params.leftMargin = (params.width + DensityUtils.dp2px(
					AddClothingDetailActivity.this, 10)) * 3;
			break;
		default:
			break;
		}
		loadListData(view.getId());
		lvData.setLayoutParams(params);
		dropDownAdapter.notifyDataSetChanged();
	}

	public void loadListData(int id) {
		switch (id) {
		case R.id.btn_season:
			dropDownList.clear();
			dropDownList.add("春季");
			dropDownList.add("夏季");
			dropDownList.add("秋季");
			dropDownList.add("冬季");
			break;
		case R.id.btn_style:
			dropDownList.clear();
			dropDownList.add("欧美");
			dropDownList.add("韩范");
			dropDownList.add("甜美");
			dropDownList.add("原创");
			dropDownList.add("日系");
			break;
		case R.id.btn_brand:
			dropDownList.clear();
			dropDownList.add("ONLY");
			dropDownList.add("OCHIRLY");
			dropDownList.add("OSA");
			dropDownList.add("AIKEN");
			dropDownList.add("米兰假日");
			dropDownList.add("拉夏贝尔");
			break;
		case R.id.btn_price:
			dropDownList.clear();
			dropDownList.add("0-100");
			dropDownList.add("100-500");
			dropDownList.add("500以上");
			break;
		default:
			break;
		}
	}

	class DropDownAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dropDownList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dropDownList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater
						.from(AddClothingDetailActivity.this);
				convertView = inflater.inflate(R.layout.drop_down_item, null);
				holder.tv = (TextView) convertView.findViewById(R.id.drop);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			RelativeLayout.LayoutParams params = (LayoutParams) holder.tv
					.getLayoutParams();
			params.width = (Settings.DISPLAY_WIDTH - (DensityUtils.dp2px(
					AddClothingDetailActivity.this, 10)) * 3) / 4;
			params.height = lvData.getHeight() / 9;
			holder.tv.setText(dropDownList.get(position));
			final int location = position;
			holder.tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					lvData.setVisibility(View.GONE);
					switch (dropDownFlag) {
					case 1:
						btnSeason.setText(dropDownList.get(location));
						dropDownFlag = 0;
						choosedSeasonId = location;
						break;
					case 2:
						btnStyle.setText(dropDownList.get(location));
						dropDownFlag = 0;
						choosedStyleId = location;
						break;
					case 3:
						btnBrand.setText(dropDownList.get(location));
						dropDownFlag = 0;
						choosedBrandId = location;
						break;
					case 4:
						btnPrice.setText(dropDownList.get(location));
						dropDownFlag = 0;
						choosedPriceId = location;
						break;
					default:
						break;
					}
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView tv;
		}

	}

	class ClothingTask extends AsyncTask<String, Void, Clothing> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
			super.onPreExecute();
		}

		@Override
		protected Clothing doInBackground(String... url) {
			JSONAccessor ac = new JSONAccessor(AddClothingDetailActivity.this,
					HttpAccessor.METHOD_POST_MULTIPART);
			ClothingParam param = new ClothingParam();
			if (TextUtils.isEmpty(imagePath)) {
				return null;
			}
			String description = descriptionEd.getText().toString();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setSeasonId(seasonIds[choosedSeasonId]);
			param.setStyleId(styleIds[choosedStyleId]);
			param.setBrandId(brandIds[choosedBrandId]);
			param.setPriceId(priceIds[choosedPriceId]);
			param.setCategoryId(categoryId);
			param.setDescription(description);
			param.setFile(newImageFile);
			// 需要缩略图
			param.setThumbnail("1");
			return ac.execute(url[0], param, Clothing.class);
		}

		@Override
		protected void onPostExecute(Clothing result) {
			pd.dismiss();
			if (result != null && "0".equals(result.getCode())) {
				Log.v("Add", result.toString());
				Toast.makeText(AddClothingDetailActivity.this, "添加成功",
						Toast.LENGTH_SHORT).show();
				//finish();
				Intent intent = new Intent();
				intent.setClass(AddClothingDetailActivity.this, AddClothingActivity.class);
				intent.putExtra("flag", "Detail");
				setResult(RESULT_OK, intent);
				finish();
			}
			super.onPostExecute(result);
		}

		private void showProgressDialog() {
			pd = new ProgressDialog(AddClothingDetailActivity.this);
			pd.setTitle(getString(R.string.register_pd_title));
			pd.setMessage(getString(R.string.addClothing_msg_save));
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
	}
}
