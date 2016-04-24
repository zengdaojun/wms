package com.zdjer.utils.view.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zdjer.utils.R;
import com.zdjer.utils.view.AppContext;
import com.zdjer.utils.view.adapter.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zdj
 */
public abstract class BaseListActivity<T> extends BaseActivity
        implements OnRefreshListener,
        OnItemClickListener, OnScrollListener {

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView lvListView;
    protected BaseListAdapter<T> baseListAdapter;
    protected int currentPage = 0;//当前页

    //状态
    protected static final int STATE_NONE = 0;//无
    protected static final int STATE_REFRESH = 1;//刷新
    protected static final int STATE_LOADMORE = 2;//加载更多
    protected static final int STATE_NOMORE = 3;//没有更多
    protected static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态

    protected static int state = STATE_NONE;//状态

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();
    protected abstract ListView getListView();
    protected abstract BaseListAdapter<T> getListAdapter();
    protected abstract List<T> getListData();

    /**
     * 初始化视图
     */
    @Override
    public void initView() {

        //获得相关组件
        swipeRefreshLayout = getSwipeRefreshLayout();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.zdjer_swiperefresh_color1, R.color.zdjer_swiperefresh_color2,
                R.color.zdjer_swiperefresh_color3, R.color.zdjer_swiperefresh_color4);

        lvListView = getListView();
        lvListView.setOnItemClickListener(this);
        lvListView.setOnScrollListener(this);

        if(baseListAdapter!=null){
            lvListView.setAdapter(baseListAdapter);
        }else{
            baseListAdapter = getListAdapter();
            lvListView.setAdapter(baseListAdapter);
            state = STATE_NONE;//初始化为无数据状态
        }
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        loadData();
    }

    /**
     * 加载数据
     */
    public void loadData() {
        List<T> lstData = getListData();
        if (lstData == null) {
            lstData = new ArrayList<T>();
        }

        if (currentPage == 0) {
            baseListAdapter.clear();
        }
        int adapterState = BaseListAdapter.STATE_EMPTY_ITEM;
        if ((baseListAdapter.getCount() + lstData.size()) == 0) {
            adapterState = BaseListAdapter.STATE_EMPTY_ITEM;
        } else if (lstData.size() == 0
                || (lstData.size() < getPageSize() && currentPage == 0)) {
            adapterState = BaseListAdapter.STATE_NO_MORE;
            baseListAdapter.notifyDataSetChanged();
        } else {
            adapterState = BaseListAdapter.STATE_LOAD_MORE;
        }
        baseListAdapter.setState(adapterState);
        baseListAdapter.addData(lstData);
        // 判断等于是因为最后有一项是listview的状态
        if (baseListAdapter.getCount() == 1) {
            baseListAdapter.setState(BaseListAdapter.STATE_EMPTY_ITEM);
            baseListAdapter.notifyDataSetChanged();
        }
        loadFinish();
    }

    /**
     * 完成刷新
     */
    protected void loadFinish() {
        setSwipeRefreshLoadedState();
        state = STATE_NONE;
    }

    /**
     * 设置顶部加载完毕的状态
     */
    protected void setSwipeRefreshLoadedState() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * 获得一页数据数量
     * @return
     */
    protected int getPageSize() {

        return AppContext.PAGE_SIZE;
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        if (state == STATE_REFRESH) {
            return;
        }
        // 设置顶部正在刷新
        lvListView.setSelection(0);
        setSwipeRefreshLoadingState();
        currentPage = 0;
        state = STATE_REFRESH;
        loadData();
    }

    /**
     * 设置顶部正在加载的状态
     */
    protected void setSwipeRefreshLoadingState() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setEnabled(false);// 防止多次重复刷新
        }
    }

    /**
     * 滚动
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (baseListAdapter == null || baseListAdapter.getCount() == 0) {
            return;
        }
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (state == STATE_LOADMORE || state == STATE_REFRESH) {
            return;
        }
        // 判断是否滚动到底部
        boolean scrollEnd = false;
        try {
            if (view.getPositionForView(baseListAdapter.getFooterView()) == view
                    .getLastVisiblePosition())
                scrollEnd = true;
        } catch (Exception e) {
            scrollEnd = false;
        }

        if (state == STATE_NONE && scrollEnd) {
            if (baseListAdapter.getState() == BaseListAdapter.STATE_LOAD_MORE
                    || baseListAdapter.getState() == BaseListAdapter.STATE_NETWORK_ERROR) {
                currentPage++;
                state = STATE_LOADMORE;
                loadData();
                baseListAdapter.setFooterViewLoading();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }


    /**
     * 列表项点击
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    }

    @Override
    public void onClick(View v) {

    }
}
