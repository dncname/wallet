package com.dnc.loc.ui.aty.account;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SizeUtils;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.databinding.ActivityAccountReceiveBinding;
import com.dnc.loc.ui.dialog.PromptDialog;
import com.dnc.loc.utils.QrCreateUtils;
import com.dnc.loc.utils.ShareUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_ACCOUNT_RECEIVE)
public class AccountReceiveAty extends BaseActivity<BaseViewModel, ActivityAccountReceiveBinding> {

    @Autowired(name = RouteConst.WALLET_USER_NAME)
    public String userName;

    //
    private ViewGroup mShareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_account_receive);
        setAppBarView(binding.appbar);
        initView();
    }

    private void initView() {
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        // 账户
        binding.txtReceiveAccount.setText("钱包地址：" + userName);

        // 更新二维码
        Bitmap bpQr = QrCreateUtils.createQRCodeBitmap(userName, SizeUtils.dp2px(198), "UTF-8", "H", "0", Color.BLACK, Color.WHITE, null, null, 0F);
        binding.ivReceiveQrCode.setImageBitmap(bpQr);

        binding.txtReceiveAccount.setOnClickListener(v -> {
            // 复制文字
            String code = userName;
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(code);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText( code, code);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
            
//            CopyUtils.copy(v.getContext(), "transactionId", getData(position).value);
//            ToastUtils.showShort("复制成功");
        });

        mShareView = binding.clReceiveShare;
        binding.btnReceiveShare.setOnClickListener(v -> {
            // 分享文字
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, userName);
            startActivity(Intent.createChooser(shareIntent, "分享文字"));

            // 分享图片
            //requestStoragePermissions(AccountReceiveAty.this, 1);
        });

        binding.btnReceiveSave.setOnClickListener(v -> {
            // (1)保存在私有目录，不需要权限
//            savePhoto(AccountPay.this, mShareView);
//            Toast.makeText(AccountPay.this, "保存", Toast.LENGTH_SHORT).show();

            // (2)现在为了方便保存在公共目录
            requestStoragePermissions(AccountReceiveAty.this, 2);

        });
    }

    private void savePhoto() {
        if(null != mShareView) {
            // 背景变黑
            mShareView.setBackgroundColor(ContextCompat.getColor(AccountReceiveAty.this, R.color.color_bg_F5F5F5));

            String picPath = new ShareUtils().captureWindow(mShareView)
                 .savePic(this, "1", false);
            Toast.makeText(AccountReceiveAty.this, "文件已保存至" + picPath, Toast.LENGTH_SHORT).show();

            mShareView.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

//    private void sharePhoto() {
//        if(null != mShareView) {
//            // 背景变黑
//            //mShareView.setBackgroundColor(ContextCompat.getColor(LoginAty.this, R.color.color_bg_F5F5F5));
//
//            Sharetils share = new Sharetils(AccountReceiveAty.this, mShareView);
//            Intent shareIntent = share.sharePhoto("receive_01");
//            startActivity(Intent.createChooser(shareIntent, "分享图片"));
//        }
//    }

    private void requestPermissionsEvent(int status) {
        switch(status) {
//            case 1:
//                // 分享
//                sharePhoto();
//                break;
            case 2:
                // 保存
                savePhoto();
                break;
        }
    }

    private PromptDialog warnPermissionsDialog;
    private int requestPermissionsTime;
    public void requestStoragePermissions(Activity activity, int status) {
        /**
         * 在这里返回的 reference 变量是系统为当前的下载请求分配的一个唯一的ID，
         * 我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操作
         * 或者查询下载的状态以及取消下载等等
         */            //启动下载,该方法返回系统为当前下载请求分配的一个唯一的ID
        AndPermission.with(activity)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    requestPermissionsEvent(status);
                }).onDenied(permissions -> {
            requestPermissionsTime++;
            if (requestPermissionsTime < 3) {
                requestStoragePermissions(activity, status);
            } else {
                if (warnPermissionsDialog == null) {
                    warnPermissionsDialog = new PromptDialog(this, R.style.prompt_dialog);
                }
                warnPermissionsDialog.showTextDialog(activity.getString(R.string.request_permission_prompt), activity.getString(R.string.to_share_you_need_to_use), v -> {
                    warnPermissionsDialog.dismiss();
                }, v1 -> {
                    requestStoragePermissions(activity, status);
                    warnPermissionsDialog.dismiss();
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
