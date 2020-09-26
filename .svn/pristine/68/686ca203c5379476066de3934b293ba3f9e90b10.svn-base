package com.zlw.base.ui.listentry;//package tac.android.base.listentry;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class EntryTypedAdapter extends BaseAdapter {
//    public List<Object> items = new ArrayList<Object>();
//    private ListEntryViewHolder[] _types;
//
//    public EntryTypedAdapter(ListEntryViewHolder[] types) {
//        _types = types;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return _types.length;
//    }
//
//    @Override
//    public int getCount() {
//        return items.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return items.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        ListEntry entry = ListEntry.from(view, parent, _types[getItemViewType(position)]);
//        entry.position = position;
//        entry.item = getItem(position);
//        entry.refresh();
//        return entry.root;
//    }
//}