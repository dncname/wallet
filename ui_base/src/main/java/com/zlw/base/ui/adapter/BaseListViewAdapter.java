package com.zlw.base.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseListViewAdapter<T> extends android.widget.BaseAdapter {

    private int _resLayoutId;
    private List<T> _data;
    private Context _context;

    public Context getContext() {
        return _context;
    }

    public List<T> getDatas() {
        return _data;
    }

    public T getData(int position) {
        if (position < 0 || _data == null || _data.size() == 0) {
            return null;
        }

        if (_data.size() - 1 < position) {
            return null;
        }
        return _data.get(position);
    }

    public BaseListViewAdapter(List<T> data, int resLayoutId) {
        _data = data;
        _resLayoutId = resLayoutId;
    }

    @Override
    public int getCount() {
        return _data == null ? 0 : _data.size();
    }

    @Override
    public T getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(_resLayoutId, parent, false);
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        bindItem(holder, position);
        return convertView;
    }

    protected abstract void bindItem(BaseViewHolder holder, int position);

    public void update(List<T> objects) {
        _data = objects;
        notifyDataSetChanged();
    }

    public void addItem(T object) {
        _data.add(object);
//        notifyItemChanged(_data.size() - 1);
        notifyDataSetChanged();
    }

    public void addAll(List<T> objects) {
        _data.addAll(objects);
        notifyDataSetChanged();
    }

    public void insertItem(int positon, T object) {
        _data.add(positon, object);
        notifyDataSetChanged();
    }

    public void updateItem(int positon, T object) {
        _data.set(positon, object);
        notifyDataSetChanged();
    }

    public void deleteItem(int positon) {
        _data.remove(positon);
        notifyDataSetChanged();
    }

    public void clean() {
        _data = new ArrayList<>();
        notifyDataSetChanged();
    }

    public class BaseViewHolder {
        private View _itemView;

        public View getView(int id) {
            return _itemView.findViewById(id);
        }

        public void setText(int id, String text) {
            if (!(getView(id) instanceof TextView)) {
                return;
            }
            ((TextView) getView(id)).setText(text);
        }

        public void setImage(Picasso picasso,int id, String url) {
            if (!(getView(id) instanceof ImageView)) {
                return;
            }
            picasso.load(url).into((ImageView) getView(id));
        }

        public void setImage(int id, int src) {
            if (!(getView(id) instanceof ImageView)) {
                return;
            }
            ImageView iv = (ImageView) getView(id);
            iv.setImageResource(src);
        }

        public void setBackground(int id, int bgId) {
            getView(id).setBackgroundDrawable(_itemView.getContext().getResources().getDrawable(bgId));
        }

//        public void setBackground(int id, String url) {
//            Picasso.with(getView(id).getContext()).load(url).into((ImageView) getView(id));
//        }

        public void setTag(int id, Object o) {
            getView(id).setTag(o);
        }

        public void setOnClickListener(int id, View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
        }

        public BaseViewHolder(View itemView) {
            _itemView = itemView;
        }
//
//        public ViewDataBinding binding;
//        public BaseViewHolder(ViewDataBinding binding) {
//            this.binding = binding;
//        }
    }
}
