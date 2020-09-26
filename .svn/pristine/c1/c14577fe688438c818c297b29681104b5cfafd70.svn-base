package com.dnc.loc.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dnc.loc.R;
import com.dnc.loc.utils.Click;

public class HGSoftInput extends LinearLayout implements View.OnClickListener {
    private PopupWindow mPopupWindow;

    private ImageView ivInput1;
    private ImageView ivInput2;
    private ImageView ivInput3;
    private ImageView ivInput4;
    private ImageView ivInput5;
    private ImageView ivInput6;

    private Activity activity;
    private ViewGroup rootView;
    private InputCallback listener;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private String pwd = "";
    private int mInputCount ;

    Runnable task = new Runnable() {
        @Override
        public void run() {
            if (listener != null)
                listener.validate(pwd);
        }
    };

    public HGSoftInput setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return this;
    }

    public interface InputCallback {
        void validate(String pwd);
    }

    public void setData(InputCallback l) {
        this.listener = l;
    }

    public HGSoftInput(Activity activity) {
        super(activity);
        this.activity = activity;
        init(null);
    }

    public HGSoftInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HGSoftInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    //初始化
    private void init(AttributeSet attrs) {
        //hasDot = true;
    }

    //消失
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            setBackgroundAlpha(0f);
            mPopupWindow.dismiss();
        }
    }

    //消失
    public void clearInput() {
        pwd = "";
        mInputCount = 0;
        ivInput1.setVisibility(View.GONE);
        ivInput2.setVisibility(View.GONE);
        ivInput3.setVisibility(View.GONE);
        ivInput4.setVisibility(View.GONE);
        ivInput5.setVisibility(View.GONE);
        ivInput6.setVisibility(View.GONE);
    }

    //弹出
    public void show() {
        rootView = (ViewGroup) View.inflate(getContext(), R.layout.view_soft_input, null);
        mPopupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        setBackgroundAlpha(1f);
        findByIdAndSetClick(rootView);
        mPopupWindow.showAtLocation(((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content),
                Gravity.LEFT | Gravity.TOP, 0, 0);
    }

    private void findByIdAndSetClick(View view) {
        ivInput1 = view.findViewById(R.id.input_1);
        ivInput2 = view.findViewById(R.id.input_2);
        ivInput3 = view.findViewById(R.id.input_3);
        ivInput4 = view.findViewById(R.id.input_4);
        ivInput5 = view.findViewById(R.id.input_5);
        ivInput6 = view.findViewById(R.id.input_6);

        TextView one = view.findViewById(R.id.one);
        TextView two = view.findViewById(R.id.two);
        TextView three = view.findViewById(R.id.three);
        TextView four = view.findViewById(R.id.four);
        TextView five = view.findViewById(R.id.five);
        TextView six = view.findViewById(R.id.six);
        TextView seven = view.findViewById(R.id.seven);
        TextView eight = view.findViewById(R.id.eight);
        TextView nine = view.findViewById(R.id.nine);
        TextView zero = view.findViewById(R.id.zero);

        RelativeLayout del = view.findViewById(R.id.del);
        ImageView close = view.findViewById(R.id.iv_back);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        del.setOnClickListener(this);
        close.setOnClickListener(this);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                setBackgroundAlpha(1f);
                if(mOnDismissListener != null)
                    mOnDismissListener.onDismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setBackgroundAlpha(0f);
                mPopupWindow.dismiss();
                break;
            case R.id.del:
                deleteInputVisibility();
                if(mInputCount > 0 && !pwd.isEmpty()) {
                    mInputCount -= 1;
                    pwd = pwd.substring(0, pwd.length() - 1);
                }
                break;
            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.four:
            case R.id.five:
            case R.id.six:
            case R.id.seven:
            case R.id.eight:
            case R.id.nine:
            case R.id.zero:
                addInputVisibility();
                mInputCount += 1;
                pwd = pwd + ((TextView) v).getText().toString();

                if(mInputCount == 6) {
                    if(Click.isFastClick()) return;
                    ivInput1.postDelayed(task, 100);
                }
                break;
        }
    }

    private void addInputVisibility() {
        switch(mInputCount) {
            case 0:
                ivInput1.setVisibility(View.VISIBLE);
                break;
            case 1:
                ivInput2.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivInput3.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivInput4.setVisibility(View.VISIBLE);
                break;
            case 4:
                ivInput5.setVisibility(View.VISIBLE);
                break;
            case 5:
                ivInput6.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void deleteInputVisibility() {
        switch(mInputCount) {
            case 1:
                ivInput1.setVisibility(View.GONE);
                break;
            case 2:
                ivInput2.setVisibility(View.GONE);
                break;
            case 3:
                ivInput3.setVisibility(View.GONE);
                break;
            case 4:
                ivInput4.setVisibility(View.GONE);
                break;
            case 5:
                ivInput5.setVisibility(View.GONE);
                break;
            case 6:
                ivInput6.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        if (mTimeCount != null)
//            mTimeCount.cancel();
    }

    private void setBackgroundAlpha(float alpha) {
        /*捕获当前activity的布局视图, 因为我们要动态模糊, 所以这个布局一定要是最新的,
         *这样我们把模糊后的布局盖到屏幕上时, 才能让用户感觉不出来变化*/
        View decorView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap bitmap = getBitmapByView(decorView);//这里是将view转成bitmap

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, false);
        Bitmap blurBitmap = getBlurBitmap(activity, scaledBitmap, 5);
        rootView.setAlpha(alpha);
        rootView.setBackgroundDrawable(new BitmapDrawable(blurBitmap));

//        final Window window = ((Activity) getContext()).getWindow();
//        final WindowManager.LayoutParams lp = window.getAttributes();
//        if (alpha == 1f) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
//        }
//        lp.alpha = alpha;
//        window.setAttributes(lp);
    }

    /**
     * 得到bitmap位图, 传入View对象
     */
    public static Bitmap getBitmapByView(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
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
}

