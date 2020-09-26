package com.dnc.loc.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

import com.dnc.loc.R;

/**
 * 总结:
 * 在MainActivity中执行:
 * mMatrixImageView.setImageMatrix(matrix);
 * 时此自定义View会先调用setImageMatrix(Matrix matrix)
 * 然后调用onDraw(Canvas canvas)
 */
public class MatrixImageView extends androidx.appcompat.widget.AppCompatImageView{

	private Matrix mMatrix;
    private Bitmap mBitmap;

	public MatrixImageView(Context context) {
		super(context);
		mMatrix=new Matrix();
	}

	public MatrixImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MatrixImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mMatrix=new Matrix();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//画原图
		//canvas.drawBitmap(mBitmap, 0, 0, null);
		//画经过Matrix变化后的图
		canvas.drawBitmap(mBitmap, mMatrix, null);
		super.onDraw(canvas);
	}

	@Override
	public void setImageMatrix(Matrix matrix) {
		this.mMatrix.set(matrix);
		super.setImageMatrix(matrix);
	}
	
	public void setBitmap(int id){
		mBitmap=BitmapFactory.decodeResource(getResources(), id);
	}
 
}
