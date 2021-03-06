package com.zlw.base.ui.adapter;

import androidx.viewpager.widget.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zlw.base.R;

import java.util.LinkedList;

public abstract class ReusePagerAdapter<VH extends ReusePagerAdapter.Holder> extends PagerAdapter {
    private SparseArray<LinkedList<VH>> holders = new SparseArray<>(1);

    /**
     * 获取 item count
     *
     * @return count
     */
    public abstract int getItemCount();

    /**
     * 获取 view type
     *
     * @param position position
     * @return type
     */
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * 创建 holder
     *
     * @param parent   parent
     * @param viewType type
     * @return holder
     */
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定 holder
     *
     * @param holder   holder
     * @param position position
     */
    public abstract void onBindViewHolder(VH holder, int position);

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
// 获取 position 对应的 type
        int itemViewType = getItemViewType(position);
// 根据 type 找到缓存的 list
        LinkedList<VH> holderList = holders.get(itemViewType);
        VH holder;
        if (holderList == null) {
// 如果 list 为空,表示没有缓存
// 调用 onCreateViewHolder 创建一个 holder
            holder = onCreateViewHolder(container, itemViewType);
            holder.itemView.setTag(R.id.holder_id, holder);
        } else {
            holder = holderList.pollLast();
            if (holder == null) {
// 如果 list size = 0,表示没有缓存
// 调用 onCreateViewHolder 创建一个 holder
                holder = onCreateViewHolder(container, itemViewType);
                holder.itemView.setTag(R.id.holder_id, holder);
            }
        }
        holder.position = position;
        holder.viewType = itemViewType;
// 调用 onBindViewHolder 对 itemView 填充数据
        onBindViewHolder(holder, position);
        container.addView(holder.itemView);
        return holder.itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        VH holder = (VH) view.getTag(R.id.holder_id);
        int itemViewType = holder.viewType;
        LinkedList<VH> holderList = holders.get(itemViewType);
        if (holderList == null) {
            holderList = new LinkedList<>();
            holders.append(itemViewType, holderList);
        }
// 缓存 holder
        holderList.push(holder);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public static abstract class Holder {
        public View itemView;
        public int viewType;
        public int position;

        public Holder(View view) {
            if (view == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            itemView = view;
        }
    }
}