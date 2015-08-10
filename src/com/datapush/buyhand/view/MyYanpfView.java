package com.datapush.buyhand.view;

import com.datapush.buyhand.R;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


/**
 * 滑动按钮
 * 
 * @author yanpf
 *
 */
public class MyYanpfView extends View implements OnTouchListener {
	private String strName;
	private boolean enabled = true;
	public boolean flag = false;
	public boolean NowChoose = false;
	private boolean OnSlip = false;
	public float DownX = 0f, NowX = 0f;
	private Rect Btn_On, Btn_Off;

	private boolean isChgLsnOn = false;
	private OnChangedListener ChgLsn;
	private Bitmap bg_on, bg_off, slip_btn;

	public MyYanpfView(Context context) {
		super(context);
		init();
	}

	public MyYanpfView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setChecked(boolean fl) {
		if (fl) {
			flag = true;
			NowChoose = true;
			NowX = 80;
		} else {
			flag = false;
			NowChoose = false;
			NowX = 0;
		}
	}

	public void setEnabled(boolean b) {
		if (b) {
			enabled = true;
		} else {
			enabled = false;
		}
	}

	private void init() {
		bg_on = BitmapFactory.decodeResource(getResources(),
				R.drawable.images_on);
		bg_off = BitmapFactory.decodeResource(getResources(),
				R.drawable.images_off);
		slip_btn = BitmapFactory.decodeResource(getResources(),
				R.drawable.white_btn);
		Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
		Btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0,
				bg_off.getWidth(), slip_btn.getHeight());
		setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x;
		{
			if (flag) {
				NowX = 80;
				flag = false;
			}
			if (NowX < (bg_on.getWidth() / 2))
				canvas.drawBitmap(bg_off, matrix, paint);
			else
				canvas.drawBitmap(bg_on, matrix, paint);

			if (OnSlip) {
				if (NowX >= bg_on.getWidth())
					x = bg_on.getWidth() - slip_btn.getWidth() / 2;
				else
					x = NowX - slip_btn.getWidth() / 2;
			} else {
				if (NowChoose)
					x = Btn_Off.left;
				else
					x = Btn_On.left;
			}
			if (x < 0)
				x = 0;
			else if (x > bg_on.getWidth() - slip_btn.getWidth())
				x = bg_on.getWidth() - slip_btn.getWidth();
			canvas.drawBitmap(slip_btn, x, 0, paint);
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (!enabled) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			NowX = event.getX();
			break;
		case MotionEvent.ACTION_DOWN:
			if (event.getX() > bg_on.getWidth()
					|| event.getY() > bg_on.getHeight())
				return false;
			OnSlip = true;
			DownX = event.getX();
			NowX = DownX;
			break;
		case MotionEvent.ACTION_UP:
			OnSlip = false;
			boolean LastChoose = NowChoose;
			if (event.getX() >= (bg_on.getWidth() / 2))
				NowChoose = true;
			else
				NowChoose = false;
			if (isChgLsnOn && (LastChoose != NowChoose))
				ChgLsn.OnChanged(strName, NowChoose);
			break;
		default:

		}
		invalidate();
		return true;
	}

	public void SetOnChangedListener(String name, OnChangedListener l) {
		strName = name;
		isChgLsnOn = true;
		ChgLsn = l;
	}
}
