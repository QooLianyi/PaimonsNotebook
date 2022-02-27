package com.lianyi.paimonsnotebook.lib.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewPagerPreloadListener(val layoutManager: LinearLayoutManager,val visibleThreshold:Int = 2):RecyclerView.OnScrollListener() {
    private var previousTotal = 0 // The total number of items in the dataset after the last load  总数据
    private var loading =true // True if we are still waiting for the last set of data to load. 是否提前加载
//    private var visibleThreshold = 2 // The minimum amount of items to have below your current scroll position before loading more.

    private var firstVisibleItem = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0
    private var current_page = 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount
            <= firstVisibleItem + visibleThreshold
        ) {
            // End has been reached

            // Do something
            onLoadMore(current_page)
            current_page++
            loading = true
        }
    }

    abstract fun onLoadMore(current_page: Int)

    fun reset(previousTotal: Int, loading: Boolean) {
        this.previousTotal = previousTotal
        this.loading = loading
    }

}