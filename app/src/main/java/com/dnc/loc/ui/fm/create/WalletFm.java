package com.dnc.loc.ui.fm.create;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.BR;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.data.model.WalletBalance;
import com.dnc.loc.databinding.ItemWalletBinding;
import com.dnc.loc.databinding.LayoutFmWalletBinding;
import com.dnc.loc.ui.aty.MainActivity;
import com.dnc.loc.ui.aty.ScanAty;
import com.dnc.loc.ui.widget.HGSoftInput;
import com.dnc.loc.utils.Click;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.ui.adapter.BaseRecyclerAdapter;
import com.zlw.base.ui.fm.BaseFragment;
import com.zlw.base.ui.utils.LoadingProgress;
import com.zlw.base.ui.utils.StatusBarUtils;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

@Route(path = RouteConst.FM_WALLET)
public class WalletFm extends BaseFragment<WalletVM, LayoutFmWalletBinding> implements WalletContract.View {

    private static final String TAG = "_TAG_WalletFm";

    private HGSoftInput mHGSoftInput;
    private Dialog feeDialog;
    private WalletContract.Presenter mPresenter;

    private WalletBalance balance = new WalletBalance(); //钱包余额
    private BaseRecyclerAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.layout_fm_wallet);
        StatusBarUtils.setDrawable(getActivity(), R.drawable.shape_toolbar_bg);
        setPresenter();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        WalletVM wallet = WalletManage.getInstance().getCurrentWallet();
        if(TextUtils.isEmpty(wallet.userName)) {
            // 清空账号, 退出
            RouteConst.setUserStatus(RouteConst.STATE_SIGN_OUT);
            ARouter.getInstance().build(RouteConst.AV_PATH_LOGIN).navigation(getContext());
            //
            getActivity().finish();
            return;
        }
        else if(!viewModel.userName.equals(wallet.userName)) {
            // 切换账号
            getActivity().finish();
            Intent intent = getActivity().getIntent();
            startActivity(intent);
            return;
        }
        //
        initHook();
    }

    private void initHook() {
        viewModel.setCallback((status, msg) -> {
            if (status != 0) {
                // 只显示一次
                Activity act = (Activity)getContext();
                if(act instanceof MainActivity) {
                    if("Import".equals(MainActivity.from) || "Switch".equals(MainActivity.from)) {
                        ToastUtils.showShort(msg);
                        MainActivity.from = "";
                    }
                }
            }
            if (status == 100) {
                updateBalance();
            }
            else {
                showContent();
            }
        });

        // 联网获取余额
        if (!TextUtils.isEmpty(viewModel.userName)) {
            viewModel.checkBalance(false);
        }
    }

    private void initView() {
        WalletVM wallet = WalletManage.getInstance().getCurrentWallet();
        bindViewModel(BR.viewModel, wallet);

        //binding.ivBack.setOnClickListener(v -> {
        //    exitApp();
        //});

        //账号管理
        binding.llWalletManager.setOnClickListener(v -> {
            ARouter.getInstance().build(RouteConst.AV_ACCOUNT_MANAGE).navigation(getContext());
        });

        //收款
        binding.ivWalletReceive.setOnClickListener(v -> {
            ARouter.getInstance().build(RouteConst.AV_ACCOUNT_RECEIVE)
                    .withString(RouteConst.WALLET_USER_NAME , viewModel.userName)
                    .navigation(getContext());
        });

        //为好友创建
        binding.ivWalletCreate.setOnClickListener(view -> new IntentIntegrator(getActivity()).setCaptureActivity(ScanAty.class)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setRequestCode(REQUEST_CODE)
                .setPrompt("")
                .setCameraId(0)
                .setBeepEnabled(true)
                .setBarcodeImageEnabled(false)
                .initiateScan());

        //
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.page_blue));
        binding.refreshLayout.setOnRefreshListener(() -> load());

        // set listView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.list.setLayoutManager(layoutManager);

        //钱包余额
        balance.appendBalance(null, "");
        adapter = new BaseRecyclerAdapter<WalletBalance.BalanceInfo>(balance.infos, R.layout.item_wallet) {

            @Override
            protected void bindItem(BaseRecyclerAdapter.BaseViewHolder holder, int position) {
                ((ItemWalletBinding) holder.binding).setBalance(getData(position));
            }

            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder baseViewHolder = super.onCreateViewHolder(parent, viewType);
                ScreenAdapterTools.getInstance().loadView(baseViewHolder.itemView);
                return baseViewHolder;
            }
        };
        //item点击事件
        adapter.setOnItemClickListener(position -> {
            // 转帐记录
            ARouter.getInstance().build(RouteConst.AV_TRANSACTION_RECORD).navigation(getContext());
        });

        binding.list.setAdapter(adapter);
    }

    private void updateBalance() {
        showContent();
        balance.appendBalance(viewModel.balanceMap, viewModel.userName);
        adapter.clean();
        adapter.update(balance.infos);
    }

    public void showLoading() {
        binding.loading.showLoading();
    }

//    public void showError(String error) {
//        binding.refreshLayout.setRefreshing(false);
//        binding.loading.setErrorText(error);
//        binding.loading.showError();
//    }

    public void showContent() {
        binding.refreshLayout.setRefreshing(false);
        binding.loading.showContent();
    }

//    public void showEmpty() {
//        binding.refreshLayout.setRefreshing(false);
//        binding.loading.setEmptyText("暂无应用数据");
//        binding.loading.showEmpty();
//    }

    private void load() {
        showLoading();

        //
        if (!TextUtils.isEmpty(viewModel.userName)) {
            viewModel.checkBalance(true);
        }
    }

//    private Long clickTime = 0L; //记录第一次点击的时间
//    private void exitApp() {
//        if (System.currentTimeMillis() - clickTime > 2000) {
//            ToastUtils.showShort("再次点击退出");
//            clickTime = System.currentTimeMillis();
//        } else {
//            LifecycleCallbacks.AppExit(getContext());
//        }
//    }

    /**
     * 输入密码，创建手续费 -> 确认 -> 创建
     *
     * @return
     */
    @Override
    public void showDialog(String newUserPubKey, String newUserName) {
        String balance = viewModel.getBalance("DNC");
        //LogUtils.e(TAG, " balance: " + balance );
        if(viewModel.isHttpError){
            ToastUtils.showShort("无法获取余额，请稍后再试");
            return;
        }
        if (Double.valueOf(balance) < 0.01) {
            ToastUtils.showShort("账户余额不足，请先购买DNC");
            return;
        }
        if (mHGSoftInput != null) {
            mHGSoftInput.dismiss();
        }
        mHGSoftInput = new HGSoftInput(getActivity());
        mHGSoftInput.setData(key -> {
            String text = viewModel.getCurrentPriKey();
            if (TextUtils.isEmpty(text)) {
                return;
            }
            byte[] bytes = EncryptUtils.decryptHexStringAES(text,
                    EncryptUtils.encryptMD5ToString(key.getBytes()).getBytes(),
                    "AES/CBC/PKCS5Padding",
                    viewModel.hexString2Bytes(viewModel.iv));
            if (bytes == null || bytes.length == 0) {
                ToastUtils.showLong("密码错误");
                mHGSoftInput.clearInput();
                return;
            }
            String pri = new String(bytes);

            if(mPresenter != null) {
                LoadingProgress.showProgress();
                mPresenter.createFee(pri, viewModel.userName, newUserPubKey, newUserName, (isSuccess, o) -> {
                    LoadingProgress.dismissProgress();
                    if (isSuccess) {
                        String fee = (String) o;
                        // "1.0000 EOS"
                        fee = fee.replace("EOS", "DNC");
                        showFeeDialog(newUserPubKey, newUserName, fee);
                        //LogUtils.e(TAG, " 获取手续费成功: " + o.toString() );
                    } else {
                        //LogUtils.e(TAG, " 获取手续费失败: " + o.toString() );
                        ToastUtils.showLong(o.toString());
                    }
                });
            }
            mHGSoftInput.dismiss();
        });
        mHGSoftInput.show();
    }

    @Override
    public void showFeeDialog(String newUserPubKey, String newUserName, String fee) {

        if (feeDialog != null) {
            feeDialog.dismiss();
        }
        feeDialog = new Dialog(getContext(), R.style.dialog_card);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fee, null);
        feeDialog.setContentView(view);
        feeDialog.setCanceledOnTouchOutside(true);

        ImageView iv_back = (ImageView) view.findViewById(R.id.iv_back);
        TextView txt_fee = (TextView) view.findViewById(R.id.txt_fee);
        TextView txt_username = (TextView) view.findViewById(R.id.txt_username);
        TextView txt_pub = (TextView) view.findViewById(R.id.txt_pub);
        Button btn_create = (Button) view.findViewById(R.id.btn_create);

        iv_back.setOnClickListener(v -> {
            feeDialog.dismiss();

        });
        txt_fee.setText(fee);
        txt_username.setText(newUserName);
        txt_pub.setText(newUserPubKey);
        btn_create.setOnClickListener(v -> {
            if(Click.isFastClick()) return;

            if(mPresenter != null) {
                LoadingProgress.showProgress();
                mPresenter.createAccount("","", newUserPubKey, newUserName, (isSuccess, o) -> {
                    LoadingProgress.dismissProgress();
                    if (isSuccess) {
                        String info = (String) o;
                        //LogUtils.e(TAG, " 创建成功: " + o.toString() );
                        ToastUtils.showLong(o.toString());
                    } else {
                        //LogUtils.e(TAG, " 创建失败: " + o.toString() );
                        ToastUtils.showLong(o.toString());
                    }
                });
            }

            feeDialog.dismiss();
        });

        feeDialog.getWindow().setGravity(Gravity.BOTTOM);
        feeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (!feeDialog.isShowing()) {
            feeDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String resultStr = result.getContents();
            if(resultStr.contains(",")) {
                String[] infos = resultStr.split(",");
                if(infos.length >= 2) {
                    // 检查公钥未创建 -> 检查账号可用 -> 密码 -> 创建手续费 -> 确认 -> 创建
                    String createPubKey = infos[0];
                    String createUserName = infos[1];
                    checkNewAccount(createPubKey, createUserName);
                }
            }
            else {
                // 转帐
                ARouter.getInstance().build(RouteConst.AV_TRANSACTION)
                        .withString(RouteConst.TOKEN_NAME, "DNC")
                        .withString(RouteConst.WALLET_USER_NAME, resultStr)
                        .navigation(getContext());
            }
        } else {
            //本地
            if (resultCode == ScanAty.RESULT_CODE_PICK_IMAGE) {
                if (data.hasExtra("result")) {
                    String resultStr = data.getStringExtra("result");
                    if(resultStr.contains(",")) {
                        String[] infos = resultStr.split(",");
                        if(infos.length >= 2) {
                            // 检查公钥未创建 -> 检查账号可用 -> 密码 -> 创建手续费 -> 确认 -> 创建
                            String createPubKey = infos[0];
                            String createUserName = infos[1];
                            checkNewAccount(createPubKey, createUserName);
                        }
                    }
                    else {
                        // 转帐
                        ARouter.getInstance().build(RouteConst.AV_TRANSACTION)
                                .withString(RouteConst.TOKEN_NAME, "DNC")
                                .withString(RouteConst.WALLET_USER_NAME, resultStr)
                                .navigation(getContext());
                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setPresenter() {
        this.mPresenter = new WalletPresenter(this);
    }

    @Override
    public void checkNewAccount(String createPubKey, String createUserName) {
        if(mPresenter != null) {
            mPresenter.getNewKeyAccounts(createPubKey, createUserName);
        }
    }

    @Override
    public void onback(boolean isSuccess, Object o) {
        ToastUtils.showLong(o.toString());
    }
}
