package com.cronelab.riscc.support.common.helper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        //Log.e("PaginationScroller", "scrolling: X: $dx Y: $dy")

        configureIndicatorColor(firstVisibleItemPosition)
        if (dx > 0) { //check for scroll left/ right

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 /*&& totalItemCount >= getTotalPageCount()*/) {
                    loadMoreItems()
                }
            }
        }

        if (dy > 0) { //check for scroll top/ bottom
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 /*&& totalItemCount >= getTotalPageCount()*/) {
                    loadMoreItems()
                }
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    protected abstract fun loadMoreItems()
    protected abstract fun configureIndicatorColor(firstVisibleItemPosition: Int)
    abstract fun getTotalPageCount(): Int
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}