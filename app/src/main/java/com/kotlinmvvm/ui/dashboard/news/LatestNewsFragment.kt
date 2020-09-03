package com.kotlinmvvm.ui.dashboard.news

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kotlinmvvm.R
import com.kotlinmvvm.adapter.NewsAdapter
import com.kotlinmvvm.base.BaseFragment
import com.kotlinmvvm.databinding.FragmentLatesNewsBinding
import com.kotlinmvvm.model.Article
import com.kotlinmvvm.utils.AppUtils
import com.kotlinmvvm.utils.listners.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_lates_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LatestNewsFragment :
    BaseFragment<FragmentLatesNewsBinding, LatestNewsViewModel>(R.layout.fragment_lates_news),
    OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    override val viewModel: LatestNewsViewModel by viewModel()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var linLayoutManager: LinearLayoutManager
    private var data: ArrayList<Article> = ArrayList()
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getLocalizedNews("in")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        observer()
    }

    private fun initViews() {
        swipeRef.setOnRefreshListener(this)
        linLayoutManager = LinearLayoutManager(activity)
        newsAdapter = NewsAdapter(data, this)
        mReyData.apply {
            layoutManager = linLayoutManager
            adapter = newsAdapter
            addOnScrollListener(scrollListener)
        }
    }


    private fun observer() {
        viewModel.recentNewsLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setAdapter(it)
//                newsAdapter.removeLoader()
            }
        }

        viewModel.isLast.observe(viewLifecycleOwner) {
            isLastPage = it
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            isLoading = it
        }

        viewModel.isSwipeRefLoading.observe(viewLifecycleOwner) {
            swipeRef.isRefreshing = it
        }
    }

    private fun setAdapter(newData: ArrayList<Article>) {
        if (viewModel.pageNo > 1) {
            newsAdapter.removeLoader()
        }
        newsAdapter.setData(newData, viewModel.isSwipeRefLoading.value!!)
        viewModel.isSwipeRefLoading.value = false
//        newsAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, data: Any, position: Int) {
        val article = data as Article

        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this.requireContext(), R.color.toolbarBackground))
        builder.setShowTitle(true)
        builder.setStartAnimations(this.requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(this.requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()
        // check is chrom available
        val packageName = AppUtils.getPackageNameToUse(this.requireContext(), article.url!!)

        when {
            packageName != null -> {
                customTabsIntent.intent.setPackage(packageName)
                customTabsIntent.launchUrl(this.requireContext(), Uri.parse(article.url))
            }
            else -> {
                // if chrome not available open in web view
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstVisibleItemPosition = linLayoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = linLayoutManager.childCount
            val totalItemCount = linLayoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getLocalizedNews("in")
                newsAdapter.addLoader()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onRefresh() {
        viewModel.isSwipeRefLoading.value = true
        viewModel.pageNo = 1
        viewModel.getLocalizedNews("in")
    }
}