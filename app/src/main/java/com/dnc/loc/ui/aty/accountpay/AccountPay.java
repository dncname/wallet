package com.dnc.loc.ui.aty.accountpay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.R;
import com.dnc.loc.base.BaseContract;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.constant.SpConst;
import com.dnc.loc.databinding.ActivityAccountPayBinding;
import com.dnc.loc.ui.aty.MainActivity;
import com.dnc.loc.ui.aty.account.LoginAty;
import com.dnc.loc.ui.widget.HintPopupWindow;
import com.dnc.loc.utils.Click;
import com.dnc.loc.utils.QrCreateUtils;
import com.dnc.loc.utils.ShareUtils;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.utils.LifecycleCallbacks;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_ACCOUNT_PAY)
public class AccountPay extends BaseActivity<BaseViewModel, ActivityAccountPayBinding> implements AccountPayContract.View {

    private static final String TAG = "_TAG_AccountPay";

    @Autowired(name = RouteConst.WALLET_PUB_KEY)
    public String pubKey;

    @Autowired(name = RouteConst.WALLET_USER_NAME)
    public String userName;

    private AccountPayContract.Presenter mPresenter;

    private HintPopupWindow shareWindow;
    // 分享这个图片
    private ViewGroup mShareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_account_pay);
        initView();
        setPresenter();
    }

    private void initView() {
        setAppBarView(binding.appbar);

        binding.ivBack.setOnClickListener(v -> {
            if(Click.isFastClick()) return;
            onBackPressed();
        });
        // 公钥
        binding.txtWaitPubValue.setText(pubKey);
        // 地址
        binding.txtWaitUsernameValue.setText(userName);

        // 更新二维码
        //   可能要按付款协议接口显示二维码
        //binding.ivWaitQrCode.setImageBitmap(createBitmap(Base64.encodeToString(userName.getBytes(), Base64.DEFAULT)));
        //Bitmap bpQr = QrCreateUtils.createQRCodeBitmap(userName, SizeUtils.dp2px(198));
        //  修改了"H", 0
        String tmp = pubKey + "," + userName;
        Bitmap bpQr = QrCreateUtils.createQRCodeBitmap(tmp, SizeUtils.dp2px(198), "UTF-8", "H", "0", Color.BLACK, Color.WHITE, null, null, 0F);
        binding.ivWaitQrCode.setImageBitmap(bpQr);
        
        // 请好友创建
        binding.btnWaitPay.setOnClickListener(v -> {
            //弹出选项弹窗
            shareWindow.showPopupWindow();
        });
        initShareWindow();

        // 我已支付
        binding.btnWaitOk.setOnClickListener(v -> {
            if(Click.isFastClick()) return;
            checkAccountPub(pubKey, (isSuccess, o) -> {
                if (isSuccess) {
                    //gotoAccountSet(o.toString());
                    gotoLogin(o.toString());
                }
                else {
                    //ToastUtils.showLong("账户创建未成功!");
                    // 或者网络异常
                    ToastUtils.showLong(o.toString());
                }
            });
        });

        // 用户存在，退出界面
        if(WalletManage.getInstance().containsWallet(userName)) {
            ToastUtils.showShort(userName + "已添加，请更换其他钱包地址");
            finish();
        }
        //
        setAccount();
    }

    private void setAccount() {
        // (界面初始化)创建界面默认判断账号是否存在、我已支付界面默认判断公钥是否创建
        checkAccountPub(pubKey, (isSuccess, o) -> {
            if (isSuccess) {
                //gotoAccountSet(o.toString());
                gotoLogin(o.toString());
            }
            else {
                //ToastUtils.showLong("账户创建未成功!");
            }
        });
    }

    private void gotoLogin(String name) {
        WalletVM viewModel = new WalletVM();
        // 公钥
        viewModel.pubKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_B_K), "None");
        // 私钥(未加密的私钥)
        viewModel.priKey = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.P_K), "None");
        // 用户
        viewModel.userName = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.WALLET_NAME), "");
        if(viewModel.userName.equals(name)) {
            ToastUtils.showLong("账户已创建!");
        }
        else {
            // 可能激活了其他用户名，保存其他用户名; 区别一下不同账号
            viewModel.userName = name;
            ToastUtils.showLong("账户" + name + "已创建!");
        }
        // 密码
        String psw = SPUtils.getInstance().getString(SpConst.getSpCreateKey(SpConst.PSW), "");

        //保存钱包信息，加密保存priKey
        WalletManage.getInstance().addWallet(viewModel.userName, viewModel.priKey, viewModel.pubKey, psw);
        // 记住状态
        RouteConst.setUserStatus(RouteConst.STATE_LOGIN_OK);
        // clear create tmp data, not import
        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_K));
        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.P_B_K));
        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.WALLET_NAME));
        SPUtils.getInstance().remove(SpConst.getSpCreateKey(SpConst.PSW));

        MainActivity.from = "Create";
        ARouter.getInstance().build(RouteConst.AV_MAIN).navigation(this);
        //关闭登录界面
        LifecycleCallbacks.finishActivity(LoginAty.class);

        finish();
    }

//    private void gotoAccountSet(String name) {
//        // 记住状态
//        RouteConst.setUserStatus(RouteConst.STATE_PUSH_OK);
//        // 记住账号
//        SPUtils.getInstance().put(SpConst.getSpCreateKey(SpConst.WALLET_NAME), name, false);
//
//        //已推送(当前状态)
//        ARouter.getInstance().build(RouteConst.AV_ACCOUNT_CREATE)
//                .withInt(RouteConst.USER_STATE , RouteConst.STATE_PUSH_OK)
//                .navigation(this);
//
//        ToastUtils.showLong("账户已创建!");
//        finish();
//    }

    //保存+分享弹窗
    public void initShareWindow() {
        //具体初始化逻辑看下面的图
        shareWindow = new HintPopupWindow(this);
        //这是根布局
        ViewGroup rootView = (ViewGroup) View.inflate(this, R.layout.dialog_account_pay, null);
        ViewGroup clDialog = (ViewGroup) rootView.findViewById(R.id.cl_wait_dialog);
        shareWindow.initLayout(rootView, clDialog);
        //这是分享布局
        mShareView = (ViewGroup) rootView.findViewById(R.id.cl_share);
        // 公钥
        TextView txtPubKey = (TextView) rootView.findViewById(R.id.txt_wait_pub_value);
        txtPubKey.setText(pubKey);
        // 地址
        TextView txtUserName = (TextView) rootView.findViewById(R.id.txt_wait_addr_value);
        txtUserName.setText(userName);

        // 更新二维码
        ImageView ivQrCode = (ImageView) rootView.findViewById(R.id.iv_wait_qr_code);
        String tmp = pubKey + "," + userName;
        Bitmap bpQr = QrCreateUtils.createQRCodeBitmap(tmp, SizeUtils.dp2px(198), "UTF-8", "H", "0", Color.BLACK, Color.WHITE, null, null, 0F);
        ivQrCode.setImageBitmap(bpQr);

        ImageView ivDown = (ImageView) rootView.findViewById(R.id.iv_wait_down);
        ivDown.setOnClickListener(v -> {
            shareWindow.dismissPopupWindow();
        });

        //1)保存
        Button btnWaitSave = (Button) rootView.findViewById(R.id.btn_wait_save);
        btnWaitSave.setOnClickListener(v -> {
            // (1)保存在私有目录，不需要权限
//            savePhoto(AccountPay.this, mShareView);
//            Toast.makeText(AccountPay.this, "保存", Toast.LENGTH_SHORT).show();

            // (2)现在为了方便保存在公共目录
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.Group.STORAGE)
                    .onGranted(permissions -> {
                        savePhoto(clDialog);
                    }).onDenied(permissions -> {

            }).start();

        });

        //2)分享
        Button btnWaitShare = (Button) rootView.findViewById(R.id.btn_wait_share);
        btnWaitShare.setOnClickListener(v -> {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.Group.STORAGE)
                    .onGranted(permissions -> {
                        // 分享
                        sharePhoto(clDialog);
                    }).onDenied(permissions -> {

            }).start();
        });
    }

    private void savePhoto(ViewGroup rootView) {
        if(null != mShareView) {
            // 背景变黑
            mShareView.setBackgroundDrawable(mShareView.getContext().getResources().getDrawable(R.drawable.shape_qrcode_white_bg));

            String picPath = new ShareUtils().captureWindow(mShareView)
                    .savePic(this, "3", false);
            Toast.makeText(AccountPay.this, "文件已保存至" + picPath, Toast.LENGTH_SHORT).show();

            mShareView.setBackgroundDrawable(mShareView.getContext().getResources().getDrawable(R.drawable.shape_qrcode_bg));
        }
    }

    private void sharePhoto(ViewGroup rootView) {
        if(null != mShareView) {
            // 背景变黑
            mShareView.setBackgroundDrawable(mShareView.getContext().getResources().getDrawable(R.drawable.shape_qrcode_white_bg));

            ShareUtils shareUtils = new ShareUtils();
            String picPath = shareUtils.captureWindow(mShareView)
                    .savePic(this, "2", false);
            Intent shareIntent = shareUtils.sharePhoto(this, picPath);
            startActivity(Intent.createChooser(shareIntent, "分享图片"));

            mShareView.setBackgroundDrawable(mShareView.getContext().getResources().getDrawable(R.drawable.shape_qrcode_bg));
        }
    }

    @Override
    public void checkAccountPub(String pubKey, BaseContract.Callback callback) {
        if(mPresenter != null) {
            mPresenter.getKeyAccounts(pubKey, callback);
            //test:
            //mPresenter.getKeyAccounts("EOS8FfDtyMZZKrJyoAah3W1rVrdHTEf9CmEGZZtpoSSncHXEKkWER", callback);
        }
    }

    @Override
    public void setPresenter() {
        this.mPresenter = new AccountPayPresenter(this);
    }

    @Override
    public void onback(boolean isSuccess, Object o) {
        ToastUtils.showLong(o.toString());
    }
}


