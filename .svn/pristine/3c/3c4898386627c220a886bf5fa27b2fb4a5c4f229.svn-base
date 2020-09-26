package com.dnc.loc.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.BR;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.data.eos.EosTransferManger;
import com.dnc.loc.data.model.ResultPageInfo;
import com.dnc.loc.databinding.ActivityTransactionBinding;
import com.dnc.loc.ui.widget.HGSoftInput;
import com.dnc.loc.utils.Click;
import com.dnc.loc.utils.EosUtils;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.utils.LoadingProgress;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

@Route(path = RouteConst.AV_TRANSACTION)
public class TransactionAty extends BaseActivity<WalletVM, ActivityTransactionBinding> { // implements EosAccountRefListener

    private static final String TAG = "_TAG_TransactionAty";

    @Autowired(name = RouteConst.TOKEN_NAME)
    public String tokenName;

    @Autowired(name = RouteConst.WALLET_USER_NAME)
    public String userName;

    private String balance;
    private String fee;
    private HGSoftInput mHGSoftInput;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_transaction);
        //EosAccountManger.getInstance().addEosAccountRefListeners(this);
        init();
        initView();
    }

    private void init() {
        WalletManage walletManage = WalletManage.getInstance();
        WalletVM wallet = walletManage.getCurrentWallet();
        bindViewModel(BR.viewModel, wallet);
    }

    private void initView() {
        setAppBarView(binding.appbar);
        
        binding.ivBack.setOnClickListener(view -> onBackPressed());

        balance = viewModel.getBalance("DNC");
        binding.txtBalance.setText(balance + "DNC");
        //binding.txtBalance.setText("（余额 " + EosAccountManger.getInstance().getBalance(tokenName) + "）");

        binding.txtName.setText(tokenName.replace("EOS", "DNC") + "转账");
        if(!TextUtils.isEmpty(userName)) {
            binding.editTo.setText(userName);
        }

        binding.btnDelete.setOnClickListener(view -> binding.editAmount.setText(""));
        binding.btnAll.setOnClickListener(view -> binding.editAmount.setText(balance));

        binding.btnScan.setOnClickListener(view -> new IntentIntegrator(this).setCaptureActivity(ScanAty.class)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setRequestCode(REQUEST_CODE)
                .setPrompt("")
                .setCameraId(0)
                .setBeepEnabled(true)
                .setBarcodeImageEnabled(false)
                .initiateScan());

        binding.btnDo.setOnClickListener(view -> {
            if(Click.isFastClick()) return;

            String to = binding.editTo.getText().toString();
            if (TextUtils.isEmpty(to)) {
                ToastUtils.showShort("请输入收款账户！");
                return;
            }
            to = to.trim();
            String strAmount = binding.editAmount.getText().toString();

            if (TextUtils.isEmpty(strAmount)) {
                ToastUtils.showShort("请输入转账额度！");
                return;
            }
            if (new BigDecimal(strAmount).compareTo(new BigDecimal(balance)) == 1) {
                ToastUtils.showShort("可用余额不足！");
                return;
            }
            strAmount = strAmount.trim();
            strAmount = formatAmount(strAmount) + " " + tokenName;
//            quantity = formatAmount(quantity) + " EOS";

            String remark = binding.editRemark.getText().toString();
            transfer(remark, to, strAmount);
        });

        binding.txtRequiredPay.setVisibility(TextUtils.equals(tokenName, "DNC") ? View.VISIBLE : View.GONE);
        binding.txtRequiredPay.setVisibility(TextUtils.equals(tokenName, "EOS") ? View.VISIBLE : View.GONE);

//        EosTransferManger.getInstance().getRequiredFee(TextUtils.equals(tokenName, "DNC") ? "eosio" : "eosio.token", "查询交易手续费", "test", "0.01 EOS", SPUtils.getInstance().getString(SpConst.WALLET_NAME), (isSuccess, o) -> {
//            fee = ((TransactionRequest) o).fee;
//            binding.txtRequiredFee.setText("区块收取" + fee.replace("EOS", "DNC") + "作为手续费");
//        });

        binding.editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.equals(tokenName, "DNC") || TextUtils.equals(tokenName, "EOS")) {
                    if (TextUtils.isEmpty(binding.editAmount.getText().toString())) return;
                    if (TextUtils.isEmpty(fee)) return;
                    String strFee = fee.split(" ")[0];
                    Double intFee = Double.valueOf(strFee);
                    String amount = binding.editAmount.getText().toString();
                    if (!TextUtils.isEmpty(amount) && amount.startsWith(".")) {
                        amount = "0" + amount;
                        binding.editAmount.setText(amount);
                        binding.editAmount.setSelection(amount.indexOf(".") + 1);
                    }
                    double needBi = intFee + Double.valueOf(amount);
                    binding.txtRequiredPay.setText("需支付：" + needBi + " DNC");
                }
            }
        });
    }

    private void transfer(String remark, String to, String strAmount) {
        if (mHGSoftInput != null) {
            mHGSoftInput.dismiss();
        }
        mHGSoftInput = new HGSoftInput(this);
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

            String action = "eosio";
            if (!TextUtils.equals(tokenName, "DNC") && !TextUtils.equals(tokenName, "EOS")) {
                action = "eosio.token";
            }

            LoadingProgress.showProgress();
            EosTransferManger.getInstance().setCreator(viewModel.userName).setCreatorPriKey(pri)
                    .pay(action, remark, to.trim(), strAmount, viewModel.userName, (isSuccess, o) -> {
                        LoadingProgress.dismissProgress();
                        if (isSuccess) {
                            String info = (String) o;
                            String[] infos = info.split("time=");
                            //ToastUtils.showLong("交易成功! transaction_id=" + infos[0]);
                            ToastUtils.showLong("交易成功!");

                            WalletManage.getInstance().getCurrentWallet().checkBalance(true);

                            String balance = viewModel.getBalance(tokenName);
                            if(!TextUtils.equals(balance, "获取不到")){
                                serverCallback(viewModel.userName, viewModel.getBalance(tokenName), tokenName, viewModel.userName, to,
                                        binding.editAmount.getText().toString(), (String) o, remark, infos[1]);
                            }
                            onBackPressed();
                        } else {
                            ToastUtils.showLong("交易失败" + o.toString());
                        }
                        //
                        mHGSoftInput.dismiss();
                    });
        });

        mHGSoftInput.show();
    }

    private String formatAmount(String amount) {
        Double d = Double.valueOf(amount);
        DecimalFormat nf = new DecimalFormat("##0.0000");
        nf.setMaximumFractionDigits(4);
        return nf.format(d);
    }

    // 转账完没有刷新全额
    public void serverCallback(String name, String balance, String currency,
                               String payer, String remitter, String money, String transactionId,
                               String remar, String time) {

        ResultPageInfo pageInfo = new ResultPageInfo();
        List<ResultPageInfo.ResultInfo> infos = new ArrayList<>();
        infos.add(new ResultPageInfo.ResultInfo("转账类型", currency));
        String strTime = time.substring(0, time.length() - 4);
        try {
            infos.add(new ResultPageInfo.ResultInfo("转账时间", EosUtils.UTCToCST(strTime, "yyyy-MM-dd'T'HH:mm:ss")));
        } catch (ParseException e) {
            infos.add(new ResultPageInfo.ResultInfo("转账时间", strTime));
            e.printStackTrace();
        }
        infos.add(new ResultPageInfo.ResultInfo("备注内容", remar));
        infos.add(new ResultPageInfo.ResultInfo("收款账户", remitter));
        infos.add(new ResultPageInfo.ResultInfo("交易单号", transactionId, false, true));
        pageInfo.infos = infos;
        ARouter.getInstance().build(RouteConst.AV_TRANSACTION_RESULT)
                .withSerializable("pageInfo", pageInfo)
                .withString("pageTitle", "转账结果")
                .withString("bigTitle", "转账成功")
                .withString("warn", "转账有可能延迟到账，请耐心等待")
                .withString("quantity", "- " + money + " " + currency)
                .withString("colorType", "2")
                .navigation(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            binding.editTo.setText(result.getContents());
        } else {
            //本地
            if (resultCode == ScanAty.RESULT_CODE_PICK_IMAGE) {
                if (data.hasExtra("result")) {
                    binding.editTo.setText(data.getStringExtra("result"));
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onDestroy() {
        if(mHGSoftInput != null) {
            mHGSoftInput.dismiss();
        }
        //EosAccountManger.getInstance().removeEosAccountRefListeners(this);
        super.onDestroy();
    }

//    @Override
//    public void blockFlushed() {
//
//    }
//
//    @Override
//    public void balanceFlushed() {
//        binding.txtBalance.setText("（余额 " + EosAccountManger.getInstance().getBalance(tokenName) + "）");
//    }
}
