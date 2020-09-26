package com.zlw.base.ui.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zlw.base.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasePositionAdapter extends BaseRecyclerAdapter {

    public static final int TYPE_T_ITEM = 0;
    public static final int TYPE_FOOTER = 1;
    public PositionContent mainItem;

    public HashMap<String, PositionContent> positionItemMap = new HashMap<>();
    public List<Integer> typeItems = new ArrayList<>();

    public BasePositionAdapter(List data, PositionContent mainItem, PositionContent... positionContents) {
        super(data, mainItem.resLayoutId);
        this.mainItem = mainItem;
        for (PositionContent positionContent : positionContents) {
            positionItemMap.put(String.valueOf(positionContent.position), positionContent);
            typeItems.add(positionContent.position);
        }
    }

    @Override
    public int getItemCount() {
        return getPageItemCount();
    }

    /**
     * @return 这里为了解决继承造成的问题
     */
    private int getPageItemCount() {
        if (getDatas() != null && getDatas().size() > 0) {
            return getDatas().size() + positionItemMap.size() + 1;
        } else if(positionItemMap.size() > 0) {
            return positionItemMap.size() + 1;
        } else {
            return 0;
        }
    }

    private int deviationPos(int position) {
        int index = 0;
        for (int i = typeItems.size() - 1; i >= 0; i--) {
            if (typeItems.get(i) <= position) {
                index = i + 1;
                break;
            }
        }
        position = position - index;
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getPageItemCount()) {
            return TYPE_FOOTER;
        }
        if (positionItemMap.containsKey(String.valueOf(position))) {
            return positionItemMap.get(String.valueOf(position)).resLayoutId;
        }
        return TYPE_T_ITEM;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_footer, parent, false);
            return new BaseViewHolder(binding);
        }
        if (viewType == TYPE_T_ITEM) {
            return super.onCreateViewHolder(parent, viewType);
        }

        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new BaseViewHolder(binding);
    }

    @Override
    protected void bindItem(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            return;
        }
        if (getItemViewType(position) == TYPE_T_ITEM) {
            mainItem.setData(getDatas().get(deviationPos(position)));
            mainItem.bindItem(holder);
            return;
        }
        if (positionItemMap.containsKey(String.valueOf(position))) {
            positionItemMap.get(String.valueOf(position)).bindItem(holder);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d("byfen", "onDetachedFromRecyclerView");
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (getItemViewType(position) == TYPE_T_ITEM) {
            mainItem.onRecycle((BaseViewHolder) holder);
        } else {
            if (positionItemMap.containsKey(String.valueOf(position))) {
                positionItemMap.get(String.valueOf(position)).onRecycle((BaseViewHolder) holder);
            }
        }
        super.onViewRecycled(holder);
    }
}
