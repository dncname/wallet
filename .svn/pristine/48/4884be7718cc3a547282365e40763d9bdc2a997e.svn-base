package com.dnc.loc.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dnc.loc.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;


public class PromptDialog extends Dialog {

    public String content;
    public TextView txtContainer;
    public TextView txtTitle;
    public TextView btnYes;
    public TextView btnNo;
    public LinearLayout btnBottom;

    public PromptDialog(@NonNull Context context, int themeStyle) {
        super(context, themeStyle);
        setContentView(R.layout.dialog_msg);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        txtTitle = findViewById(R.id.txt_title);
        txtContainer = findViewById(R.id.txt_container);
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        btnBottom = findViewById(R.id.ll_bottom);
        ScreenAdapterTools.getInstance().loadView(findViewById(R.id.content));

        //initView();
    }

//    private void initView() {
//        //设置弹出位置
//        //getWindow().setGravity(Gravity.CENTER);
//        //设置弹出动画
//        //window.setWindowAnimations(R.style.main_menu_animStyle)
//        //设置对话框大小
//        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        setCanceledOnTouchOutside(true);
//    }

    public void showTextDialog(String title, String content) {
        //txtContainer.setVisibility(View.VISIBLE);
        setCanceledOnTouchOutside(true);

        txtTitle.setText(title);
        txtContainer.setText(content);

        btnBottom.setVisibility(View.GONE);
        show();
    }

    public void showTextDialog(String title, String content, View.OnClickListener cancel, View.OnClickListener sure) {
        //txtContainer.setVisibility(View.VISIBLE);
        txtTitle.setText(title);
        txtContainer.setText(content);

        btnYes.setText("确定");
        btnNo.setText("取消");
        btnYes.setOnClickListener(sure);
        btnNo.setOnClickListener(cancel);
        show();
    }

    public void showTextDialog(String title, String content, String yes, String no, View.OnClickListener cancel, View.OnClickListener sure) {
        //txtContainer.setVisibility(View.VISIBLE);
        txtTitle.setText(title);
        txtContainer.setText(content);
        btnYes.setText(yes);
        btnNo.setText(no);

        btnYes.setOnClickListener(sure);
        btnNo.setOnClickListener(cancel);
        show();
    }


}

