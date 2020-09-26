package com.dnc.loc.ui.aty;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.data.model.ResultPageInfo;
import com.dnc.loc.databinding.ActivityTransactionResultBinding;
import com.dnc.loc.databinding.ItemResultBinding;
import com.dnc.loc.utils.CopyUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.ui.adapter.BaseRecyclerAdapter;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_TRANSACTION_RESULT)
public class TransactionResultAty extends BaseActivity<BaseViewModel, ActivityTransactionResultBinding> {

    @Autowired
    public String pageTitle;
    @Autowired
    public String bigTitle;
    @Autowired
    public String warn;
    @Autowired
    public ResultPageInfo pageInfo;
    @Autowired
    public String quantity;
    @Autowired
    public String colorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_transaction_result);
        initView();
    }

    private void initView() {
        setAppBarView(binding.appbar);
        binding.txtName.setText(pageTitle);
        binding.txtBigTitle.setText(bigTitle);
        binding.txtWarn.setText(warn);
        if("1".equals(colorType+"")) {
            binding.txtQuantity.setText("" + quantity);
            binding.txtQuantity.setTextColor(getResources().getColor(R.color.cl_3574fa));
            binding.txtQuantity.setVisibility(View.VISIBLE);
        }
        else if("2".equals(colorType+"")) {
            binding.txtQuantity.setText("" + quantity);
            binding.txtQuantity.setTextColor(getResources().getColor(R.color.cl_20dc86));
            binding.txtQuantity.setVisibility(View.VISIBLE);
        }
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.list.setLayoutManager(layoutManager);

        binding.list.setAdapter(new BaseRecyclerAdapter<ResultPageInfo.ResultInfo>(pageInfo.infos, R.layout.item_result) {

            @Override
            protected void bindItem(BaseViewHolder holder, int position) {
                ((ItemResultBinding) holder.binding).setResult(getData(position));
                ((ItemResultBinding) holder.binding).btnDo.setOnClickListener(v -> {
                    if (getData(position).isExp) {
                        ToastUtils.showLong(getData(position).value);
                    } else {
                        CopyUtils.copy(v.getContext(), "transactionId", getData(position).value);
                        ToastUtils.showShort("复制成功");
                    }
                });
            }

            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BaseViewHolder baseViewHolder = super.onCreateViewHolder(parent, viewType);
                ScreenAdapterTools.getInstance().loadView(baseViewHolder.itemView);
                return baseViewHolder;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageInfo = null;
        pageTitle = null;
        bigTitle = null;
        warn = null;
    }
}