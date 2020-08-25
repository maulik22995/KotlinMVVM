package com.kotlinmvvm.ui.dashboard

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kotlinmvvm.R
import com.kotlinmvvm.base.BaseActivity
import com.kotlinmvvm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<ActivityMainBinding, HomeViewModel>(R.layout.activity_main) {
    override val viewModel: HomeViewModel by viewModel()
    private var mExitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        val appBarConfig = AppBarConfiguration(setOf(R.id.newsHomeFragment))
        val navController: NavController = findNavController(R.id.navHostFragment)
        setupActionBarWithNavController(navController, appBarConfig)
        bottomNavBar.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHostFragment).navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
            finish()
        } else {
            mExitTime = System.currentTimeMillis()
            toast("Click Again To Exit")
        }
    }
}