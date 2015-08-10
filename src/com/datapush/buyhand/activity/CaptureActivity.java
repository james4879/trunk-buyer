package com.datapush.buyhand.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.datapush.buyhand.R;
import com.datapush.buyhand.camera.CameraManager;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.BaseResult;
import com.datapush.buyhand.net.data.CaptureParam;
import com.datapush.buyhand.util.MD5;
import com.datapush.buyhand.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public class CaptureActivity extends Activity implements Callback {

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
////    private CaptureActivityHandler handler;
//    private ViewfinderView viewfinderView;
//    private MediaPlayer mediaPlayer;
//    private boolean hasSurface;
//    private boolean playBeep;
//    private boolean vibrate;
//    private Vector<BarcodeFormat> decodeFormats;
//    private String characterSet;
//    private FrameLayout capturelayout;
//    private static final long VIBRATE_DURATION = 200L;
//    private static final float BEEP_VOLUME = 0.10f;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.capture_layout);
//        CameraManager.init(getApplication());
//        
//        hasSurface = false;
//        initView();
//    }
//
//    public void initView() {
//        capturelayout = (FrameLayout) findViewById(R.id.capturelayout);
//        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//    }
//
//    @SuppressWarnings("deprecation")
//	@Override
//    protected void onResume() {
//        super.onResume();
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder);
//        } else {
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//        capturelayout.invalidate();
//        decodeFormats = null;
//        characterSet = null;
//        playBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
//        initBeepSound();
//        vibrate = true;
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        CameraManager.get().closeDriver();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        try {
//            CameraManager.get().openDriver(surfaceHolder);
//        } catch (IOException ioe) {
//            return;
//        } catch (RuntimeException e) {
//            return;
//        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//
//    }
//
//    public ViewfinderView getViewfinderView() {
//        return viewfinderView;
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void drawViewfinder() {
//        viewfinderView.drawViewfinder();
//    }
//
//    public void handleDecode(final Result obj, Bitmap barcode) {
//        if (barcode != null) {
//            playBeepSoundAndVibrate();
//            String resultText = obj.getText();
//           if(resultText != null && resultText.length() >0){
//        	   new CaptureTask().execute(resultText);
//           }
//         
//        }
//    }
//
//    private void initBeepSound() {
//        if (playBeep && mediaPlayer == null) {
//
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(beepListener);
//
//            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
//            try {
//                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
//                        file.getLength());
//                file.close();
//                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null;
//            }
//        }
//    }
//
//    private final OnCompletionListener beepListener = new OnCompletionListener() {
//
//        @Override
//		public void onCompletion(MediaPlayer mediaPlayer) {
//            mediaPlayer.seekTo(0);
//        }
//    };
//
//    private void playBeepSoundAndVibrate() {
//        if (playBeep && mediaPlayer != null) {
//            mediaPlayer.start();
//        }
//        if (vibrate) {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(VIBRATE_DURATION);
//        }
//    }
// 
//	private class CaptureTask extends AsyncTask<String, Void, BaseResult> {
//		
//		
//		private ProgressDialog mProgressDialog;
//		
//		@Override
//		protected void onPreExecute() {
//			
//				mProgressDialog = new ProgressDialog(CaptureActivity.this);
//				mProgressDialog.setTitle(R.string.app_name);
//				mProgressDialog.setMessage(getString(R.string.tijiao));
//				mProgressDialog.setCanceledOnTouchOutside(false);
//				mProgressDialog.show();
//		}
//		
//		
//		@Override
//		protected BaseResult doInBackground(String... arg0) {
//
//			JSONAccessor accessor = new JSONAccessor(CaptureActivity.this,HttpAccessor.METHOD_GET);
//			accessor.enableJsonLog(true);
//			CaptureParam param = new CaptureParam();
//			param.setLoginKey(arg0[0]);
//			param.setCode(MD5.GetMD5Code(arg0[0]+AppContents.getUserInfo().getUserCode()+"datapush"));
//			param.setUsername(AppContents.getUserInfo().getUserCode());
//			return accessor.execute(Settings.CAPTURE, param, BaseResult.class);
//		}
//
//		@Override
//		protected void onPostExecute(BaseResult result) {
//			mProgressDialog.dismiss();
//			if (result != null && result.getCode() == 1) {
//				Toast.makeText(CaptureActivity.this,"获取的code是：" + result.getCode(), Toast.LENGTH_SHORT).show();
//				finish();
//			} else {
//				Toast.makeText(CaptureActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
//			}
//			super.onPostExecute(result);
//
//		}
//	}
}