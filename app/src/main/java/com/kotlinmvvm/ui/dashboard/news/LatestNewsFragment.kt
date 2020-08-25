package com.kotlinmvvm.ui.dashboard.news

import android.os.Bundle
import androidx.lifecycle.observe
import com.kotlinmvvm.R
import com.kotlinmvvm.base.BaseFragment
import com.kotlinmvvm.databinding.FragmentLatesNewsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class LatestNewsFragment :
    BaseFragment<FragmentLatesNewsBinding, LatestNewsViewModel>(R.layout.fragment_lates_news) {
    override val viewModel: LatestNewsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
        viewModel.getLocalizedNews("in")
    }

    private fun observer() {
       viewModel.recentNewsLiveData.observe(this){

       }
    }
}