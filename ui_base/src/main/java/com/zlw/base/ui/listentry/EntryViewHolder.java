package com.zlw.base.ui.listentry;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.zlw.base.ui.utils.RecycleBitmapInLayout;

public class EntryViewHolder<Item> extends RecyclerView.ViewHolder {
    public ViewDataBinding binding;

    private EntryViewHolder(){
        super(null);
    }

    public EntryViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindItem(Item data){
    }

    public void isSameDataNext(boolean isSameDataNext){ //下个元素的类型是否一致

    }

    public void bindItemWithStatic(Item data, String eventId, String kV){
    }

    public void setPosition(int position){
    }

    public void onRecycle() {
        if(itemView instanceof ViewGroup){
            RecycleBitmapInLayout.getInstance().recycle((ViewGroup) itemView);
        }
    }

}
