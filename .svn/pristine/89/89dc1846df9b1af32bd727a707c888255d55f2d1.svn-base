package com.zlw.base.ui.utils;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zlw.base.R;

/**
 */
public class RecyclerViewHelper {

    private PageListener pageListener;
    private RecyclerView view;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout contentView;

    public RecyclerViewHelper(SwipeRefreshLayout contentView, RecyclerView view, RecyclerView.Adapter adapter) {
        this.view = view;
        this.adapter = adapter;
        this.contentView = contentView;

        this.view.setAdapter(adapter);
        this.view.addOnScrollListener(listener);
        this.contentView.setColorSchemeColors(view.getResources().getColor(R.color.colorAccent));
        this.contentView.setOnRefreshListener(() -> {
            if (pageListener != null) {
                pageListener.onRefresh();
            }
        });

    }

    public RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        /**
         * 最后一个显示的item位置
         */
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (adapter == null || listener == null) {

                super.onScrollStateChanged(recyclerView, newState);
                return;
            }
            //判断scroll已经暂停,并且显示了最后一个item,而且还有下一页
            if (pageListener == null) {
                super.onScrollStateChanged(recyclerView, newState);
                return;
            }

            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && pageListener.hasNextPage()) {
                pageListener.appendData();
            } else {
                super.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
        }
    };


    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    public interface PageListener {

        boolean hasNextPage();

        void appendData();

        void onRefresh();
    }
}
