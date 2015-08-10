package com.datapush.buyhand.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.datapush.buyhand.net.data.BaseResult;
import com.datapush.buyhand.net.data.Chest;
import com.datapush.buyhand.net.data.ChestListParam;
import com.datapush.buyhand.net.data.ChestListResult;
import com.datapush.buyhand.net.data.ClothingDetailMessage;
import com.datapush.buyhand.net.data.ClothingMessage;
import com.datapush.buyhand.net.data.ClothingParam;
import com.datapush.buyhand.net.data.StorageParam;
import com.datapush.buyhand.net.data.UpdataNameParam;
import com.datapush.buyhand.net.data.UploadFileResult;
import com.datapush.buyhand.util.CacheImageLoader;
import com.datapush.buyhand.view.MyImageView;
import com.datapush.buyhand.view.UploadFileTask;

/**
 * 我的衣柜
 * @author yanpf
 *
 */

public class MyWardrobeActivity extends BaseActivity{
	private ImageView mBack;
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	private List<Button> mListBt = new ArrayList<Button>();
	private List<ImageView> mListImg = new ArrayList<ImageView>();
	private List<TextView> mListTv = new ArrayList<TextView>();
	private List<RelativeLayout> mListRl = new ArrayList<RelativeLayout>();
	private RelativeLayout rl1,rl2,rl3,rl4,rl5,rl6,rl7,rl8,rl9,rl10;
	private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
	private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7;
	private MyImageView selection;
	private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10,selectionFlg;
	private int mSetectId = -1;
	private int l,t,r,b;
	private List<ClothingDetailMessage> clothingList = new ArrayList<ClothingDetailMessage>();//宝贝数据
	private int clothingIndex = 0;//设置第几张图片
	private String[] mListStlye = new String[]{"101","102","103","104","105","106","107"};
	
	private List<Chest> chestList = new ArrayList<Chest>(); //衣柜list
	private int chestIndex;//点了那个衣柜第一行数据
	private Dialog mAddDialog,mPohotoDg;
	private final int CAMERA = 1001;//相机回调码
	private final int CROP = 1003;//截取图片回调码
	private String mUuuid;
	private static final String PHOTO_TEMP_FILE = "Image.jpg";
	private static final String PHOTO_TEMP_THUMBNAIL = "Thumbnail.jpg";
	private File OldFile,ThumbnailFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mywardrobe_layout);
		findView();
		initView();
		new ChestListTask().execute();
	}
	
	public void findView(){
		mBack = (ImageView) findViewById(R.id.back);
		mCacheImageLoader = new CacheImageLoader(MyWardrobeActivity.this);
		
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl5 = (RelativeLayout) findViewById(R.id.rl5);
		rl6 = (RelativeLayout) findViewById(R.id.rl6);
		rl7 = (RelativeLayout) findViewById(R.id.rl7);
		rl8 = (RelativeLayout) findViewById(R.id.rl8);
		rl9 = (RelativeLayout) findViewById(R.id.rl9);
		rl10 = (RelativeLayout) findViewById(R.id.rl10);
		
		mListRl.add(rl1);
		mListRl.add(rl2);
		mListRl.add(rl3);
		mListRl.add(rl4);
		mListRl.add(rl5);
		mListRl.add(rl6);
		mListRl.add(rl7);
		mListRl.add(rl8);
		mListRl.add(rl9);
		mListRl.add(rl10);
		
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt5 = (Button) findViewById(R.id.bt5);
		bt6 = (Button) findViewById(R.id.bt6);
		bt7 = (Button) findViewById(R.id.bt7);
		mListBt.add(bt1);
		mListBt.add(bt2);
		mListBt.add(bt3);
		mListBt.add(bt4);
		mListBt.add(bt5);
		mListBt.add(bt6);
		mListBt.add(bt7);
		
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		tv6 = (TextView) findViewById(R.id.tv6);
		tv7 = (TextView) findViewById(R.id.tv7);
		tv8 = (TextView) findViewById(R.id.tv8);
		tv9 = (TextView) findViewById(R.id.tv9);
		tv10 = (TextView) findViewById(R.id.tv10);
		
		mListTv.add(tv1);
		mListTv.add(tv2);
		mListTv.add(tv3);
		mListTv.add(tv4);
		mListTv.add(tv5);
		mListTv.add(tv6);
		mListTv.add(tv7);
		mListTv.add(tv8);
		mListTv.add(tv9);
		mListTv.add(tv10);
		
		
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		img4 = (ImageView) findViewById(R.id.img4);
		img5 = (ImageView) findViewById(R.id.img5);
		img6 = (ImageView) findViewById(R.id.img6);
		img7 = (ImageView) findViewById(R.id.img7);
		img8 = (ImageView) findViewById(R.id.img8);
		img9 = (ImageView) findViewById(R.id.img9);
		img10 = (ImageView) findViewById(R.id.img10);
		
		mListImg.add(img1);
		mListImg.add(img2);
		mListImg.add(img3);
		mListImg.add(img4);
		mListImg.add(img5);
		mListImg.add(img6);
		mListImg.add(img7);
		mListImg.add(img8);
		mListImg.add(img9);
		mListImg.add(img10);
		
		selection = (MyImageView) findViewById(R.id.selection);
		selectionFlg = (ImageView) findViewById(R.id.selectionflg);
		
		int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(MyWardrobeActivity.this, 10))/3;
		int h = (Settings.DISPLAY_HEIGHT - DensityUtils.dp2px(MyWardrobeActivity.this, 105))/10;
		
		int pL1H = h*3-15;
		int pL2H = h*2;
		int pL3H = h*4+DensityUtils.dp2px(MyWardrobeActivity.this, 5);
		
		RelativeLayout.LayoutParams param1 = (RelativeLayout.LayoutParams) img1.getLayoutParams();
		param1.width = w;
		param1.height = pL1H;
		img1.setLayoutParams(param1);
		
		RelativeLayout.LayoutParams param10 = (RelativeLayout.LayoutParams) img10.getLayoutParams();
		param10.width = w;
		param10.height = pL2H;
		img10.setLayoutParams(param10);
		
		RelativeLayout.LayoutParams param9 = (RelativeLayout.LayoutParams) img9.getLayoutParams();
		param9.width = w;
		param9.height = pL2H;
		img9.setLayoutParams(param9);
		
		RelativeLayout.LayoutParams param8 = (RelativeLayout.LayoutParams) img8.getLayoutParams();
		param8.width = w;
		param8.height = pL1H;
		img8.setLayoutParams(param8);
		
		RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams) img2.getLayoutParams();
		param2.width = w;
		param2.height = pL1H;
		img2.setLayoutParams(param2);
		
		RelativeLayout.LayoutParams paramselectionflg = (RelativeLayout.LayoutParams) selectionFlg.getLayoutParams();
		paramselectionflg.width = w;
		paramselectionflg.height = pL3H;
		selectionFlg.setLayoutParams(paramselectionflg);
		
		RelativeLayout.LayoutParams paramselection = (RelativeLayout.LayoutParams) selection.getLayoutParams();
		paramselection.width = w;
		paramselection.height = pL3H;
		selection.setLayoutParams(paramselection);
		
		RelativeLayout.LayoutParams param7 = (RelativeLayout.LayoutParams) img7.getLayoutParams();
		param7.width = w;
		param7.height = pL1H;
		img7.setLayoutParams(param7);
		
		
		RelativeLayout.LayoutParams param3 = (RelativeLayout.LayoutParams) img3.getLayoutParams();
		param3.width = w;
		param3.height = pL1H;
		img3.setLayoutParams(param3);
		
		RelativeLayout.LayoutParams param4 = (RelativeLayout.LayoutParams) img4.getLayoutParams();
		param4.width = w;
		param4.height = pL2H;
		img4.setLayoutParams(param4);
		
		RelativeLayout.LayoutParams param5 = (RelativeLayout.LayoutParams) img5.getLayoutParams();
		param5.width = w;
		param5.height = pL2H;
		img5.setLayoutParams(param5);
		
		RelativeLayout.LayoutParams param6 = (RelativeLayout.LayoutParams) img6.getLayoutParams();
		param6.width = w;
		param6.height = pL1H;
		img6.setLayoutParams(param6);
		
		
		
	}
	public void initView(){
		
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		
		//衣柜的点击和长按时间
		for (int i = 0; i < mListImg.size(); i++) {
			final int index = i;
			mListImg.get(i).setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(chestList.size() >0){
						Intent intent = new Intent(MyWardrobeActivity.this,WardrobeDetailActivity.class);
						intent.putExtra("id", chestList.get(index).getId());
						intent.putExtra("name", chestList.get(index).getChestName());
						startActivity(intent);
					}
					
				}
			});
			mListImg.get(i).setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					if(chestList.size() >0){
					chestIndex = index;
					getDialog();
					}
					return false;
				}
			});
			
		}
		
		
		int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(MyWardrobeActivity.this, 3))/4;
		new ClotheListTask().execute(mListStlye[0]);
		setSelect(R.id.bt1,0);
		for (int i = 0; i < mListBt.size(); i++) {
			final int index = i;
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mListBt.get(i).getLayoutParams();
			lp.width = w;
			mListBt.get(i).setLayoutParams(lp);
			
			mListBt.get(i).setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					setSelect(v.getId(),index);
				}
			});
		}
		//拖动处理
		OnTouchListener listener = new OnTouchListener() {
			boolean flg = false;
			int lastX, lastY;
			int startLeft, startRight, startTop, startBottom;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					l = v.getLeft();
					t = v.getTop();
					r = v.getRight();
					b = v.getBottom();
					
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					startLeft = v.getLeft();
					startRight = v.getRight();
					startTop = v.getTop();
					startBottom = v.getBottom();
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;

					int left = v.getLeft() + dx;
					int top = v.getTop() + dy;
					int right = v.getRight() + dx;
					int bottom = v.getBottom() + dy;

					v.layout(left, top, right, bottom);
					v.bringToFront();

					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					if(!flg){
					setScale(v, 0.3f, false);
					}
					flg = true;
					break;
				case MotionEvent.ACTION_UP:
					for (int i = 0; i < mListRl.size(); i++) {
						if(v.getLeft() >= mListRl.get(i).getLeft() && v.getTop() >= mListRl.get(i).getTop() &&v.getRight() <= mListRl.get(i).getRight() && v.getBottom() <= mListRl.get(i).getBottom()){
							if(chestList.size() >0 && clothingList.size() > 0){
							new StorageTask().execute(chestList.get(i).getId(),clothingList.get(clothingIndex).getId());
							}
						}
					}
					flg = false;
					v.layout(startLeft, startTop, startRight, startBottom);
					setScale(v, 0.03f, true);
					break;
				case MotionEvent.ACTION_CANCEL:
					v.layout(startLeft, startTop, startRight, startBottom);
					break;
				}
				return false;
			}
		};
		selection.setOnTouchListener(listener);
	}
	
	//选择指向
	public void setSelect(int resid,int index){
		
		if(mSetectId ==resid ){
			return;
		}else{
			mSetectId =resid;
		}
		new ClotheListTask().execute(mListStlye[index]);
		
		for(int i = 0; i < mListBt.size(); i++){
			if(mListBt.get(i).getId() == resid){
				mListBt.get(i).setSelected(true);
			}else{
				mListBt.get(i).setSelected(false);
			}
		}
		
	}
	
    /** 
     * 按住变小，放下还原
     */  
    private void setScale(View v,float temp,boolean flag) {     
          
        if(flag) {     
//            selection.setSace(v.getLeft()-(int)(temp*v.getWidth()),      
//                          v.getTop()-(int)(temp*v.getHeight()),      
//                          v.getRight()+(int)(temp*v.getWidth()),      
//                          v.getBottom()+(int)(temp*v.getHeight()));        
        	selection.setSace(l, t, r, b);
        }else{     
        	selection.setSace(v.getLeft()+(int)(temp*v.getWidth()),      
                          v.getTop()+(int)(temp*v.getHeight()),      
                          v.getRight()-(int)(temp*v.getWidth()),      
                          v.getBottom()-(int)(temp*v.getHeight()));     
        }     
    }  
    
    //取到宝贝数据
	private class ClotheListTask extends AsyncTask<String, Void, ClothingMessage> {
		

		@Override
		protected ClothingMessage doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(MyWardrobeActivity.this,HttpAccessor.METHOD_GET);
			ClothingParam param = new ClothingParam();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setCategoryId(arg0[0]);
			return accessor.execute(Settings.WARDOBELIST, param, ClothingMessage.class);
		}

		@Override
		protected void onPostExecute(ClothingMessage result) {
			if (result != null) {
				if (result.getCode().equals("0")) {
					clothingList.clear();
					if(result.getClothingList() != null){
					clothingList.addAll(result.getClothingList());
					}
					setClothing(clothingIndex, clothingList);
				}

			} else {
				Toast.makeText(MyWardrobeActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
	/**
	 * 	宝贝数据变动
	 * @param index
	 * @param list
	 */
	public void setClothing(int index , List<ClothingDetailMessage> list){
		
		if (list.size() == 0) {
			return;
		}
		//判断有有没宝贝存在
		if(clothingIndex ==list.size()){
			selection.setClickable(false);
		}else{
			selection.setClickable(true);
		}
		selection.setBackgroundResource(R.drawable.tugui_dikuang_bg);
		selectionFlg.setBackgroundResource(R.drawable.tugui_dikuang_bg);
		selection.setImageBitmap(null);
		selectionFlg.setImageBitmap(null);
		//判断不能超过宝贝个数
		if(clothingIndex  <list.size()){
		mCacheImageLoader.loadImage(list.get(index).getImagePath(), selection, new OnLoadListener() {
			
			@Override
			public void onLoad(Bitmap bitmap, View targetView) {
				if(bitmap != null){
					((ImageView) targetView).setImageBitmap(bitmap); 
				}
			}
		});}
		if(list.size() >1 && clothingIndex <list.size() -1){
		mCacheImageLoader.loadImage(list.get(index+1).getImagePath(), selectionFlg, new OnLoadListener() {
			
			@Override
			public void onLoad(Bitmap bitmap, View targetView) {
				if(bitmap != null){
					((ImageView) targetView).setImageBitmap(bitmap); 
				}
			}
		});}
	}
    //取到衣柜数据
	private class ChestListTask extends AsyncTask<String, Void, ChestListResult> {
		

		@Override
		protected ChestListResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(MyWardrobeActivity.this,HttpAccessor.METHOD_GET);
			ChestListParam param = new ChestListParam();
			param.setUserId(AppContents.getUserInfo().getId());
			return accessor.execute(Settings.CHEST_LIST, param, ChestListResult.class);
		}

		@Override
		protected void onPostExecute(ChestListResult result) {
			if (result != null) {
				if (result.getCode() == 0) {
					chestList.clear();
					chestList.addAll(result.getChestList());
					findChest(chestList);
				}

			} else {
				Toast.makeText(MyWardrobeActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
	//初始化所有衣柜
	public void findChest(List<Chest> list){
		for (int i = 0; i < list.size(); i++) {
			
			if(list.get(i).getImagePath() != null && !list.get(i).getImagePath().equals("")){
				mCacheImageLoader.loadImage(list.get(i).getImagePath(), mListImg.get(i), new OnLoadListener() {
					
					@Override
					public void onLoad(Bitmap bitmap, View targetView) {
						if(bitmap != null){
							((ImageView) targetView).setImageBitmap(bitmap); 
						}
					}
				});
			}
			mListTv.get(i).setText(list.get(i).getChestName());
		}
	}
	//放入柜子
private class StorageTask extends AsyncTask<String, Void, BaseResult> {
	
	
	private ProgressDialog mProgressDialog;

	@Override
	protected void onPreExecute() {

		mProgressDialog = new ProgressDialog(MyWardrobeActivity.this);
		mProgressDialog.setTitle(R.string.app_name);
		mProgressDialog.setMessage(getString(R.string.fangru));
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
	}
		

		@Override
		protected BaseResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(MyWardrobeActivity.this,HttpAccessor.METHOD_GET);
			StorageParam param = new StorageParam();
			param.setChestId(arg0[0]);
			param.setGoodsId(arg0[1]);
			return accessor.execute(Settings.STORAGE, param, BaseResult.class);
		}

		@Override
		protected void onPostExecute(BaseResult result) {
			
			mProgressDialog.dismiss();
			if (result != null) {
				if (result.getCode() == 0) {
					clothingIndex +=1;
					setClothing(clothingIndex,clothingList);
//					Toast.makeText(MyWardrobeActivity.this,getString(R.string.cgfangru), Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(MyWardrobeActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
//弹出修改
public void Eject(){
	
	if(mAddDialog != null){
		mAddDialog = null;
	}
	View view = LayoutInflater.from(MyWardrobeActivity.this).inflate(R.layout.trip_bottom_dialog, null);
	mAddDialog = new Dialog(MyWardrobeActivity.this, R.style.dialog);
	mAddDialog.setContentView(view);
	mAddDialog.show();
	
	Window w = mAddDialog.getWindow();
	WindowManager.LayoutParams lp = w.getAttributes();
	lp.gravity = Gravity.TOP;
	lp.y = -200;
	w.setAttributes(lp);
	
	ImageView post = (ImageView) view.findViewById(R.id.post);
	ImageView delete = (ImageView) view.findViewById(R.id.delete);
	final EditText edit = (EditText) view.findViewById(R.id.edit); 
	
	post.setOnClickListener(new OnSingleClickListener() {
		
		@Override
		public void doOnClick(View v) {
			mAddDialog.dismiss();
			if(edit.getText().toString().trim().equals("")){
			Toast.makeText(MyWardrobeActivity.this,getString(R.string.biaoti), Toast.LENGTH_SHORT).show();
			}else{
				mAddDialog.dismiss();
				new UpdataNameTask().execute(edit.getText().toString());
			}
		}
	});
	
	delete.setOnClickListener(new OnSingleClickListener() {
		
		@Override
		public void doOnClick(View v) {
			mAddDialog.dismiss();
		}
	});
}

//选择图片dialog
	public void getDialog(){
		
		if(mPohotoDg != null){
			mPohotoDg = null;
		}
		View view = LayoutInflater.from(MyWardrobeActivity.this).inflate(R.layout.pohoto_item, null);
		mPohotoDg = new Dialog(MyWardrobeActivity.this, R.style.dialog);
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
		
		local.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
				mPohotoDg.dismiss();
				Eject();
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
	
	//上传头像线程
	public class UpdateTask extends UploadFileTask{

		public UpdateTask(Context context, String filename) {
			super(context, filename);
		}

		@Override
		protected void onPostExecute(UploadFileResult result) {
			
			if (result != null) {
				if (result.getCode() == 0) {
					mListImg.get(chestIndex).setImageBitmap(BitmapUtils.getBitmapFromFile(
									ThumbnailFile, mListImg.get(chestIndex).getWidth(),mListImg.get(chestIndex).getHeight()));
				} else {
					Toast.makeText(MyWardrobeActivity.this,getString(R.string.action_settings),Toast.LENGTH_SHORT).show();
				}

			}
		}
		
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
		
		if (CROP == requestCode && RESULT_OK == resultCode) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = data.getParcelableExtra("data");
				ThumbnailFile = new File(Settings.TEMP_PATH, mUuuid + PHOTO_TEMP_THUMBNAIL);
				saveBitmapToFile(photo, ThumbnailFile);
				new UpdateTask(MyWardrobeActivity.this, ThumbnailFile.getName()).execute(ThumbnailFile.getPath(),chestList.get(chestIndex).getId());
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
		intent.putExtra("outputY", 240);
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
		intent.putExtra("outputY", 240);
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
	
	//修改衣柜名字
		private class UpdataNameTask extends AsyncTask<String, Void, BaseResult> {
			
			String text;
			
			@Override
			protected BaseResult doInBackground(String... arg0) {

				JSONAccessor accessor = new JSONAccessor(MyWardrobeActivity.this,HttpAccessor.METHOD_GET);
				accessor.enableJsonLog(true);
				UpdataNameParam param = new UpdataNameParam();
				param.setChestId(chestList.get(chestIndex).getId());
				param.setChestName(arg0[0]);
				 text = arg0[0];
				return accessor.execute(Settings.UPDATANAME, param, BaseResult.class);
			}

			@Override
			protected void onPostExecute(BaseResult result) {
				if (result != null) {
					if (result.getCode() == 0) {
						mListTv.get(chestIndex).setText(text);
					}

				} else {
					Toast.makeText(MyWardrobeActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}

		}

	

}
