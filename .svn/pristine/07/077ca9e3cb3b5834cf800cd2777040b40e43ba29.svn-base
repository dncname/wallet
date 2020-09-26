package com.zlw.base.ui.listentry;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {
    public List<Object> items = new ArrayList<>();
    protected HashMap<Integer, ListEntryViewHolder> mapHolder;
    protected int layoutFootViewId;
    public static final int TYPE_FOOTER = 999;


    public EntryAdapter(HashMap<Integer, ListEntryViewHolder> mapHolder) {
        this.mapHolder = mapHolder;
    }

    public EntryAdapter(List<Object> items, HashMap<Integer, ListEntryViewHolder> mapHolder) {
        this.mapHolder = mapHolder;
        this.items = items;
    }

    public void initFootView(int layoutFootViewId) {
        this.layoutFootViewId = layoutFootViewId;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size() + (layoutFootViewId == 0 ? 0 : 1);
    }


    @Override
    public int getItemViewType(int position) {
        if (layoutFootViewId != 0 && position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }
        String clazzName = items.get(position).getClass().getName();
        return clazzName.hashCode();
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutFootViewId, parent, false);
            return new EntryViewHolder(binding);
        } else {
            if (mapHolder == null) {
                return null;
            }
            LogUtils.d("onCreateViewHolder" + viewType);
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mapHolder.get(Integer.valueOf(viewType)).resId, parent, false);
            return mapHolder.get(viewType).newEntry(binding);
        }
    }
    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            return;
        }

        holder.bindItem(items.get(position));
        holder.setPosition(position);
    }

    @Override
    public void onViewRecycled(EntryViewHolder holder) {
        holder.onRecycle();
        super.onViewRecycled(holder);
    }

    public void update(List objects) {
        items.clear();
        items.addAll(objects);
        notifyDataSetChanged();
    }

    public void addItem(Object object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void addAll(List objects) {
        insertAll(objects);
    }

    public void insertAll(List objects) {
        items.addAll(objects);
        notifyItemRangeChanged(getItemCount(), getItemCount() + objects.size());
    }

    public void insertItem(int positon, Object object) {
        items.add(positon, object);
        notifyItemChanged(positon);
    }

    public void updateItem(int positon, Object object) {
        items.set(positon, object);
        notifyItemChanged(positon);
    }

    public void deleteItem(int positon) {
        items.remove(positon);
        notifyItemRemoved(positon);
    }

    public void clean() {
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Object> getItems() {
        return items;
    }
}