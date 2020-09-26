package com.dnc.loc.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.dnc.loc.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.ui.utils.StatusBarUtils;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Created by zhaoshuang on 16/8/29.
 * 弹出动画的popupwindow
 */
public class HintPopupWindow {

    private Activity activity;
    private WindowManager.LayoutParams params;
    private boolean isShow;
    private WindowManager windowManager;
    private ViewGroup rootView;
    private ViewGroup clDialog;

    private final int animDuration = 250;//动画执行时间
    private boolean isAniming;//动画是否在执行

    /**
     * 文字和click事件的list是对应绑定的
     */
    public HintPopupWindow(Activity activity){
        this.activity = activity;
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
    }
    public void initLayout(ViewGroup rootView, ViewGroup clDialog){
        this.rootView = rootView;
        this.clDialog = clDialog;
        initLayoutParams();
    }

    public void initLayoutParams(){
//        btn_wait_save.setVisibility(View.INVISIBLE);


        //这里给你根布局设置背景透明, 为的是让他看起来和activity的布局一样
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;//背景透明
        params.gravity = Gravity.LEFT | Gravity.TOP;

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //如果是显示状态那么隐藏视图
                if(keyCode == KeyEvent.KEYCODE_BACK && isShow) dismissPopupWindow();
                return isShow;
            }
        });
    }

    /**
     * 弹出选项弹窗
     * 默认在该view的下方弹出, 和popupWindow类似
     */
    public void showPopupWindow(){
        Log.i("Log.i", "showPopupWindow: "+isAniming);
        isShow = true;
        if(!isAniming) {
            isAniming = true;
            try {
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
//                int[] arr = new int[2];
//                locationView.getLocationOnScreen(arr);
                clDialog.measure(0, 0);
                Rect frame = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                // 坐标有点问题，可能超边界
//                float x = arr[0] + locationView.getWidth() - clDialog.getMeasuredWidth();
//                float y = arr[1] - frame.top + locationView.getHeight();
                float x = 0f;
                float y = 0f;
                clDialog.setX(x);
                clDialog.setY(y);

            /*捕获当前activity的布局视图, 因为我们要动态模糊, 所以这个布局一定要是最新的,
            *这样我们把模糊后的布局盖到屏幕上时, 才能让用户感觉不出来变化*/
                View decorView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                Bitmap bitmap = getBitmapByView(decorView);//这里是将view转成bitmap
                setBlurBackground(bitmap);//这里是模糊图片, 这个是重点我会单独讲的, 因为效率很重要啊!!!

                //这里就是使用WindowManager直接将我们处理好的view添加到屏幕最前端
                windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                windowManager.addView(rootView, params);

                // 导致部分机型出现字体过大:
                //ScreenAdapterTools.getInstance().loadView(clDialog);
                //StatusBarUtils.setDrawable(activity, R.drawable.shape_toolbar_bg);

                //这一步就是有回弹效果的弹出动画, 我用属性动画写的, 很简单
                alphaAnim(rootView, 0, 1, animDuration, true);
                showAnim(clDialog, 0, 1, animDuration, true);

                //视图被弹出来时得到焦点, 否则就捕获不到Touch事件
                rootView.setFocusable(true);
                rootView.setFocusableInTouchMode(true);
                rootView.requestFocus();
                rootView.requestFocusFromTouch();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到bitmap位图, 传入View对象
     */
    public static Bitmap getBitmapByView(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    private void setBlurBackground(Bitmap bitmap) {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, false);
        Bitmap blurBitmap = getBlurBitmap(activity, scaledBitmap, 5);
        rootView.setAlpha(0);
        rootView.setBackgroundDrawable(new BitmapDrawable(blurBitmap));
    }

    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap, int radius) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return blurBitmap(context, bitmap, radius);
        }
        return bitmap;
    }

    /**
     * android系统的模糊方法
     * @param bitmap 要模糊的图片
     * @param radius 模糊等级 >=0 && <=25
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, int radius) {

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            //Let's create an empty bitmap with the same size of the bitmap we want to blur
            Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //Instantiate a new Renderscript
            RenderScript rs = RenderScript.create(context);
            //Create an Intrinsic Blur Script using the Renderscript
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
            //Set the radius of the blur
            blurScript.setRadius(radius);
            //Perform the Renderscript
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
            //Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap);
            //recycle the original bitmap
            bitmap.recycle();
            //After finishing everything, we destroy the Renderscript.
            rs.destroy();
            return outBitmap;
        }else{
            return bitmap;
        }
    }

    public void dismissPopupWindow(){
        Log.i("Log.i", "dismissPopupWindow: "+isAniming);
        if(!isAniming) {
            isAniming = true;
            isShow = false;
            alphaAnim(rootView, 1, 0, animDuration/3, false);
            showAnim(clDialog, 1, 0, animDuration/3, false);
        }
    }

    public WindowManager.LayoutParams getLayoutParams(){
        return params;
    }

    public ViewGroup getLayout(){
        return clDialog;
    }

    /**
     * popupwindow是否是显示状态
     */
    public boolean isShow(){
        return isShow;
    }

    private void alphaAnim(final View view, int start, int end, int duration, final boolean isShow){

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow){
                    //关闭
                    try {
                        windowManager.removeViewImmediate(rootView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    isAniming = false;
                }
            }
        });
        va.start();
    }

    private void showAnim(final View view, float start, final float end, int duration, final boolean isShow) {

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
//                view.setPivotX(view.getWidth());
//                view.setPivotY(0);
                view.setPivotX(0);
                view.setPivotY(view.getHeight());
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isShow) {
                    isAniming = false;
                }else{
                }
            }
        });
        va.start();
    }
}