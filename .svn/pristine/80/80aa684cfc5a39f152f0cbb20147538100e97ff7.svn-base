package com.zlw.base.ui.fm;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zlw.base.R;
import com.zlw.base.databinding.FragmentItemListBinding;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class BaseListFragment extends BaseFragment {

    // TODO: Customize parameter argument names
    public static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private PageListener pageListener;
    private SmartRefreshLayout swipeRefreshLayout;
    GridLayoutManager gridLayoutManager;

    public boolean isFirstEnter = true;

    public BaseListFragment() {
    }

    @SuppressWarnings("unused")
    public static BaseListFragment newInstance(int columnCount) {
        BaseListFragment fragment = new BaseListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_COLUMN_COUNT)) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setContentView(R.layout.fragment_item_list);
    }

    // super.onViewCreated(view, savedInstanceState); 前调用
    public void enableFirstLoading(boolean enable) {
        isFirstEnter = enable;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding instanceof FragmentItemListBinding) {
            FragmentItemListBinding listBinding = (FragmentItemListBinding) binding;
            initRecyclerView(listBinding.list);
            swipeRefreshLayout = listBinding.refreshLayout;
            swipeRefreshLayout.setEnableFooterFollowWhenLoadFinished(false);
            swipeRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    if (pageListener == null) return;
                    pageListener.appendData();
                    if (!pageListener.hasNextPage()) {
//                        swipeRefreshLayout.finishLoadMoreWithNoMoreData();

                        swipeRefreshLayout.finishLoadMore();
                    }
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    if (pageListener == null) return;
                    pageListener.onRefresh();
                    if (!pageListener.hasNextPage()) {
                        swipeRefreshLayout.finishLoadMoreWithNoMoreData();

                    }
                }
            });

            loading.setRetryListener(v -> {
                if (pageListener == null) return;
                showContent();
                swipeRefreshLayout.autoRefresh();
            });

            //第一次进入演示刷新
            if (isFirstEnter) {
                loading.showContent();
                isFirstEnter = false;
                swipeRefreshLayout.autoRefresh();
            }
        }
    }

    public void setAppBarView(@LayoutRes int layoutId) {
        ViewStub appBarStub = getView().findViewById(R.id.layout_appbar);
        appBarStub.setLayoutResource(layoutId);
        appBarView = (ViewGroup) appBarStub.inflate();
        addPaddingTopEqualStatusBarHeight();

        ScreenAdapterTools.getInstance().loadView(appBarView);
    }


    public SmartRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public void swipeRefreshLayoutEnabled(boolean enabled) {
        if (swipeRefreshLayout == null) {
            return;
        }
        showContent();
        swipeRefreshLayout.setEnableRefresh(enabled);
        swipeRefreshLayout.setDisableContentWhenRefresh(enabled);
        swipeRefreshLayout.setEnableAutoLoadMore(enabled);
        swipeRefreshLayout.setEnableLoadMore(enabled);
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        swipeRefreshLayout.finishRefresh();
        swipeRefreshLayout.setNoMoreData(false);
        swipeRefreshLayout.finishLoadMore();
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        swipeRefreshLayout.finishRefresh();
//        swipeRefreshLayout.setNoMoreData(false);
        swipeRefreshLayout.finishLoadMore();
    }

    @Override
    public void showContent() {
        super.showContent();
        swipeRefreshLayout.finishRefresh();
        //恢复上拉
//        swipeRefreshLayout.setNoMoreData(false);
        swipeRefreshLayout.finishLoadMore();
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (mColumnCount <= 1) {
            gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), 12);//能满足  1234列布局
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 12;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), mColumnCount));
        }
    }

    public void resetLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
    }

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup sizeLookup) {
        if (gridLayoutManager == null) return;
        gridLayoutManager.setSpanSizeLookup(sizeLookup);
    }

//    public RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
//
//        /**
//         * 最后一个显示的item位置
//         */
//        private int lastVisibleItem;
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            if (adapter == null || listener == null) {
//                super.onScrollStateChanged(recyclerView, newState);
//                return;
//            }
//            //判断scroll已经暂停,并且显示了最后一个item,而且还有下一页
//            if (pageListener == null) {
//                super.onScrollStateChanged(recyclerView, newState);
//                return;
//            }
//
//            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && pageListener.hasNextPage()) {
//                pageListener.appendData();
//            } else {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            if (layoutManager instanceof LinearLayoutManager) {
//                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
//            }
//        }
//    };


    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (recyclerView != null) {
//            recyclerView.removeOnScrollListener(listener);
//        }
    }

    public interface PageListener {

        boolean hasNextPage();

        void appendData();

        void onRefresh();
    }

}
