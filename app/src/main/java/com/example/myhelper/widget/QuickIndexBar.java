package com.example.myhelper.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myhelper.R;
import com.example.myhelper.utils.DensityUtils;

/**
 * Created by ccy on 2016/9/8.
 */
public class QuickIndexBar extends View {

	private Paint paint;
	private float cellWidth;
	private float cellHeight;

	public QuickIndexBar(Context context) {
		this(context, null);
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setColor(Color.GRAY);
		//抗锯齿
		paint.setAntiAlias(true);

		paint.setTextSize(DensityUtils.dp2px(getContext(),20));
		//设置粗体和斜体
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC));

	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < Cheeses.LETTERS.length; i++) {
			String letter = Cheeses.LETTERS[i];
			//定义一个矩形
			Rect rect = new Rect();
			//把letter装进一个矩形区域内
			//这个方法将letter进行测量,将letter的宽高设置到Rect中
			//参数2:字母的开始
			//参数3:字母的结束
			paint.getTextBounds(letter, 0, 1, rect);
			int textWidth = rect.width();
			int textHeight = rect.height();
			//等于一个单位宽度/2+文本占用的宽度/2
			float x = cellWidth / 2 - textWidth / 2;
			//等于一个单位高度/2+文本占用的高度/2+每个单位高度*i
			float y = cellHeight / 2 + textHeight / 2 + i * cellHeight;
			//判断绘制的文本是否是当前选择的索引,如果是,则高亮显示
			paint.setColor(i == currentIndex ? getResources().getColor(R.color.colorPrimary) : Color.GRAY);
			paint.setTextSize(i==currentIndex?30:20);
			canvas.drawText(letter, x, y, paint);
		}
		//绘制一个文本在View上
//		canvas.drawText("#",10f,20f,paint);
//		canvas.drawText("#",10f,50f,paint);
//		canvas.drawText("#",10f,80f,paint);
//		canvas.drawText("#",10f,110f,paint);
//		canvas.drawText("#",10f,140f,paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		cellWidth = this.getMeasuredWidth();
		cellHeight = this.getMeasuredHeight() * 1.0f / Cheeses.LETTERS.length;
	}

	private int currentIndex = -1;
	private int preIndex = -1;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				if (listener != null) {
					preIndex = currentIndex;
					float y = event.getY();
					//计算触摸的点所在的位置
					currentIndex = (int) (y / cellHeight);
					//识别被触摸到的letter
					try {
						String letter = Cheeses.LETTERS[currentIndex];
						//检查触摸的位置与上一个位置是否一致,如果不一致,则打印
						if (preIndex != currentIndex) {
							Log.i("test", "letter:" + letter);
							listener.onLetterChanged(letter);
						}
					}catch (Exception e){

					}


					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (listener != null) {
					listener.onLetterDismiss();
				}
				currentIndex=-1;
				invalidate();
				break;
			default:

				break;
		}
		return true;
	}

	private OnLetterChangedListener listener;

	public interface OnLetterChangedListener {
		void onLetterChanged(String letter);

		void onLetterDismiss();
	}

	public void setOnLetterChangedListener(OnLetterChangedListener listener) {
		this.listener = listener;
	}
}
