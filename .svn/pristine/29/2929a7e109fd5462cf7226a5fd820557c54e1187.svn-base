package com.dnc.loc.ui.aty;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dnc.loc.BR;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.data.eos.model.ChainRecord;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.data.model.ResultPageInfo;
import com.dnc.loc.data.model.WalletBalance;
import com.dnc.loc.databinding.ActivityTransactionRecordBinding;
import com.dnc.loc.databinding.ItemTransforRecordBinding;
import com.dnc.loc.utils.EosUtils;
import com.dnc.loc.utils.RxUtils;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.ui.adapter.BaseRecyclerAdapter;
import com.zlw.base.ui.aty.BaseActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Route(path = RouteConst.AV_TRANSACTION_RECORD)
public class TransactionRecordAty extends BaseActivity<WalletVM, ActivityTransactionRecordBinding> {

    private WalletBalance balance = new WalletBalance(); //钱包余额
    private BaseRecyclerAdapter adapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_transaction_record);
        initView();
        initAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        initHook();
    }

    private void initHook() {
        viewModel.setCallback((status, msg) -> {
            if (status != 0) {
                // 不提示
                //ToastUtils.showShort(msg);
            }
            if (status == 100) {
                String balance = viewModel.getBalance("DNC");
                binding.txtRecordBalance.setText(balance + " DNC");
            }
            else {
                //onCompleted();
            }
        });

        // 联网获取余额
        if (!TextUtils.isEmpty(viewModel.userName)) {
            viewModel.checkBalance(false);
        }
        page = 1;
        load();
    }

    private void initView() {
        setAppBarView(binding.appbar);

        WalletManage walletManage = WalletManage.getInstance();
        bindViewModel(BR.viewModel, walletManage.getCurrentWallet());

        String balance = viewModel.getBalance("DNC");
        binding.txtRecordBalance.setText(balance + " DNC");

        // set title ...

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        binding.llReceive.setOnClickListener(v -> {
            // 收款(功能好像重复)
            ARouter.getInstance().build(RouteConst.AV_ACCOUNT_RECEIVE)
                    .withString(RouteConst.WALLET_USER_NAME , viewModel.userName)
                    .navigation(this);
        });

        binding.llTransfer.setOnClickListener(v -> {
            // 转帐
            ARouter.getInstance().build(RouteConst.AV_TRANSACTION)
                    .withString(RouteConst.TOKEN_NAME, "DNC")
                    .navigation(v.getContext());
        });
    }

    private void initAccount() {
        //
        //binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.page_blue));
        //binding.refreshLayout.setSaveEnabled(true);
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            load();
        });
        binding.refreshLayout.autoRefresh();
        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            load();
        });

        // set listView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.list.setLayoutManager(layoutManager);

        List<ChainRecord.RecordItem> records = viewModel.getRecord();
        adapter = new BaseRecyclerAdapter<ChainRecord.RecordItem>(records, R.layout.item_transfor_record) {

            @Override
            protected void bindItem(BaseViewHolder holder, int position) {
                ItemTransforRecordBinding binding = (ItemTransforRecordBinding) holder.binding;
                binding.setRecord(getData(position));
                ChainRecord.RecordItem record = getData(position);
                if(record.isIncome(viewModel.userName)) {
                    binding.ivRecordType.setImageDrawable(getResources().getDrawable(R.mipmap.icon_record_form));
                    binding.txtAddress.setText(record.from);
                    binding.txtMoney.setText("+ " + record.quantity + " DNC");
                    binding.txtMoney.setTextColor(getResources().getColor(R.color.cl_3574fa));
                }
                else {
                    binding.ivRecordType.setImageDrawable(getResources().getDrawable(R.mipmap.icon_record_to));
                    binding.txtAddress.setText(record.to);
                    binding.txtMoney.setText("- " + record.quantity + " DNC");
                    binding.txtMoney.setTextColor(getResources().getColor(R.color.cl_20dc86));
                }
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
            ChainRecord.RecordItem data = (ChainRecord.RecordItem)adapter.getData(position);
            String title = "";
            String quantity = "";
            String colorType = "";
            if(data.isIncome(viewModel.userName)) {
                title = "收款成功";
                quantity = "+ " + data.quantity + " DNC";
                colorType = "1";
            }
            else {
                title = "转账成功";
                quantity = "- " + data.quantity + " DNC";
                colorType = "2";
            }

            ResultPageInfo pageInfo = new ResultPageInfo();
            List<ResultPageInfo.ResultInfo> infos = new ArrayList<>();
            infos.add(new ResultPageInfo.ResultInfo("From", data.from));
            infos.add(new ResultPageInfo.ResultInfo("To", data.to));

            String strTime = data.block_time.substring(0, data.block_time.length() - 4);
            try {
                infos.add(new ResultPageInfo.ResultInfo("交易时间", EosUtils.UTCToCST(strTime, "yyyy-MM-dd'T'HH:mm:ss")));
            } catch (ParseException e) {
                infos.add(new ResultPageInfo.ResultInfo("交易时间", strTime));
                e.printStackTrace();
            }

            infos.add(new ResultPageInfo.ResultInfo("备注内容", data.memo));
            infos.add(new ResultPageInfo.ResultInfo("矿工费", data.fee.replace("EOS", "DNC")));
            infos.add(new ResultPageInfo.ResultInfo("哈希值", data.tx, false, true));

            pageInfo.infos = infos;
            ARouter.getInstance().build(RouteConst.AV_TRANSACTION_RESULT)
                    .withSerializable("pageInfo", pageInfo)
                    .withString("pageTitle", "账簿详情")
                    .withString("bigTitle", title)
                    .withString("quantity", quantity)
                    .withString("colorType", colorType)
                    .navigation(this);
        });

        binding.list.setAdapter(adapter);

    }

    public void getRecordList(int page, List<ChainRecord.RecordItem> recordInfo) {
        if (page == 1) {
            adapter.update(recordInfo);
        } else {
            adapter.addAll(recordInfo);
        }
    }

    private void onCompleted() {
        //showEmpty()

        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
    }

    private void load() {
        if (page == 1) {
            if (!TextUtils.isEmpty(viewModel.userName)) {
                viewModel.checkBalance(true);
            }
        }

        // record
        getTransRecord(page, 10, "trans", viewModel.userName);
    }

    @SuppressLint("CheckResult")
    private void getTransRecord(int page, int pagesize, String table, String user) {
        EosHttp.app.getTransRecord(page, pagesize, table, user).compose(RxUtils.observableAsync()).subscribe(chainRecord -> {
            if (chainRecord != null && chainRecord.total != null && chainRecord.rows.size() > 0) {
                getRecordList(page, chainRecord.rows);
            }
            onCompleted();
        }, e -> {
            onCompleted();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}