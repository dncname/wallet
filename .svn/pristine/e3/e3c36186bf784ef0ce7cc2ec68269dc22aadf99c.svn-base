package com.dnc.loc.ui.aty.account;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.R;
import com.dnc.loc.base.BaseContract;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.databinding.ActivityAccountManageBinding;
import com.dnc.loc.databinding.ItemAccountBinding;
import com.dnc.loc.ui.aty.MainActivity;
import com.dnc.loc.ui.dialog.PromptDialog;
import com.dnc.loc.vm.WalletManage;
import com.dnc.loc.vm.WalletVM;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.ui.adapter.BaseRecyclerAdapter;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_ACCOUNT_MANAGE)
public class AccountManageActivity extends BaseActivity<BaseViewModel, ActivityAccountManageBinding> {

    private PromptDialog switchDialog;
    private BaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);
        setAppBarView(binding.appbar);
        initView();
        initAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 刷新缓存账号，自动检查新增账号
        WalletManage  walletManage = WalletManage.getInstance();
        if(TextUtils.isEmpty(walletManage.getCurrentWallet().userName)) {
            // 清空账号
            finish();
            return;
        }
        walletManage.checkLoginStatus((isSuccess, o) -> {
            if(isSuccess) {
                // 更新新增账号
                ToastUtils.showLong("账号" + o + "已创建");
                updateAccount();
            }
            else {
                // 从备份界面返回，可能已删除某个账号，更新列表
                updateAccount();
            }
        });
    }

    private void initView() {
//        binding.ivSet.setOnClickListener(v -> {
//            // 转帐记录
//            ARouter.getInstance().build(RouteConst.AV_TRANSACTION_RECORD).navigation(this);
//        });

        binding.ivBack.setOnClickListener(v -> onBackPressed());
        // 导入
        binding.llImport.setOnClickListener(v -> {
            WalletManage walletManage = WalletManage.getInstance();
            if(walletManage.datas.size() >= 5) {
                ToastUtils.showLong(getString(R.string.account_max_number));
                return;
            }
            ARouter.getInstance().build(RouteConst.AV_ACCOUNT_IMPORT).navigation(this);
        });

        // 新建
        binding.llCreate.setOnClickListener(v -> {
            WalletManage walletManage = WalletManage.getInstance();
            if(walletManage.datas.size() >= 5) {
                ToastUtils.showLong(getString(R.string.account_max_number));
                return;
            }

            // 随机有可能已激活
            if (RouteConst.checkUserStatus(false) == RouteConst.STATE_CREATE_OK) {
                //WalletManage walletManage = WalletManage.getInstance();
                walletManage.checkLoginStatus((isSuccess, o) -> {
                    if(isSuccess) {
                        // 更新新增账号
                        ToastUtils.showLong("账号" + o + "已创建");
                        updateAccount();
                    }
                    else {
                        ARouter.getInstance().build(RouteConst.AV_ACCOUNT_CREATE)
                                .withInt(RouteConst.USER_STATE , RouteConst.checkUserStatus(false))
                                .navigation(this);
                    }
                });
            }
            else {
                ARouter.getInstance().build(RouteConst.AV_ACCOUNT_CREATE)
                        .withInt(RouteConst.USER_STATE , RouteConst.checkUserStatus(false))
                        .navigation(this);
            }
        });
    }

    private void updateAccount() {
        WalletManage  walletManage = WalletManage.getInstance();
        adapter.clean();
        adapter.update(walletManage.datas);
    }

    private void initAccount() {
        WalletManage  walletManage = WalletManage.getInstance();
        if(walletManage.datas.size() == 0) {
            // 不可能为空
            finish();
            return;
        }
        for(int i=0; i<walletManage.datas.size(); i++) {
            walletManage.datas.get(i).background = String.format("@mipmap/bg_manager_item%d", i/4);
        }
        int[] resArr = new int[]{ R.mipmap.bg_manager_item0,
                R.mipmap.bg_manager_item1,
                R.mipmap.bg_manager_item2,
                R.mipmap.bg_manager_item3};

        // set listView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.list.setLayoutManager(layoutManager);
//        binding.list.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                //outRect.bottom = SizeUtils.dp2px(12);
//                int spaceBottom = SizeUtils.dp2px(12);
//                outRect.set(0, 0, 0, spaceBottom);
//            }
//        });

        adapter = new BaseRecyclerAdapter<WalletVM>(walletManage.datas, R.layout.item_account) {

            @Override
            protected void bindItem(BaseRecyclerAdapter.BaseViewHolder holder, int position) {
                ItemAccountBinding binding = (ItemAccountBinding) holder.binding;
                WalletVM wallet = getData(position);
                binding.setWallet(wallet);
                binding.llAccountItem.setBackgroundResource(resArr[ position % resArr.length ]);
                binding.ivAccountBack.setOnClickListener(v -> {
                    //设置
                    ARouter.getInstance().build(RouteConst.AV_ACCOUNT_SET)
                            .withInt("walletId", position)
                            .navigation(getContext());
                });

                WalletVM currentWallet = WalletManage.getInstance().getCurrentWallet();
                binding.txtAccountState.setVisibility(wallet.userName.equals(currentWallet.userName)?View.VISIBLE:View.GONE);
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
            WalletVM data = (WalletVM)adapter.getData(position);
            // 切换
            showD(data.getUserName());
        });

        binding.list.setAdapter(adapter);
    }

    private void showD(String userName) {
        WalletVM wallet = WalletManage.getInstance().getCurrentWallet();
        if (wallet.getUserName().equals(userName)) {
            //ToastUtils.showShort("请切换其他账户");
            return;
        }

        if (switchDialog != null){
            switchDialog.dismiss();
        }
        switchDialog = new PromptDialog(this, R.style.prompt_dialog);

        String content = getString(R.string.content_switch) + userName;
        switchDialog.showTextDialog(getString(R.string.title_switch), content, v -> {
            switchDialog.dismiss();
        }, v1 -> {
            MainActivity.from = "Switch";
            WalletManage wtmg = WalletManage.getInstance();
            wtmg.setCurrentWallet(userName);
            //
            switchDialog.dismiss();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}