package com.zlw.base.ui.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;

public class RecycleViewUtils {

    private WeakReference<RecyclerView> recycleView;
//
    private RecycleViewUtils utils;

    public int lastOffsetTop;

    public int lastOffsetLeft;

    public int lastPosition;

    public RecycleViewUtils() {

    }


    public RecycleViewUtils(RecyclerView recyclerView) {
        this.recycleView = new WeakReference<>(recyclerView);
    }

    public RecycleViewUtils with(RecyclerView recyclerView) {
        if (utils == null) utils = new RecycleViewUtils(recyclerView);
        return utils;
    }

    public void getPositionAndOffset() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recycleView.get().getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if(topView != null) {
            //获取与该view的顶部的偏移量
            lastOffsetTop = topView.getTop();

            lastOffsetLeft = topView.getLeft();
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView);
        }
    }

}
