package com.datapush.buyhand.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {
	
	private float xMaxWight; //控件最大宽度
	private int xLineCount = 10;//分成10份
	private float xMinWight;//控件最小宽度
	private Paint paintPoint, linkPaint;//画笔
	private int index=1;//
	
	public void setIndex(int index) {
		this.index = index;
	}

	public PathView(Context context) {
		super(context);
		init(context);
	}

	public PathView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PathView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
	 * 绘制曲线
	 * 
	 * @param canvas
	 */
	private void doDraw(Canvas canvas) {
		
		canvas.drawLine(0, 0, xMinWight*index, 0, paintPoint);
	}
	/**
	 * 绘制线
	 */
	private void drawFrame(Canvas canvas) {
		pushView();
		canvas.drawLine(0, 0, xMaxWight, 0, linkPaint);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawFrame(canvas);
		doDraw(canvas);
		super.onDraw(canvas);
	}
	/**初始化宽度**/
	public void pushView(){
		
		if(xMaxWight == 0){
			xMaxWight = this.getWidth();
		}
		if(xMinWight == 0 ){
			xMinWight = xMaxWight /10;
		}
	}
	
	private void init(Context cont) {
		linkPaint = new Paint();
		linkPaint.setColor(Color.BLUE);
		linkPaint.setStrokeWidth(15);
		linkPaint.setFakeBoldText(true);
		linkPaint.setTextSize(25);
		
		paintPoint = new Paint();
		paintPoint.setColor(Color.YELLOW);
		paintPoint.setStrokeWidth(15);
		paintPoint.setFakeBoldText(true);
		paintPoint.setTextSize(25);
	}
}
