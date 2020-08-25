package com.kotlinmvvm.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kotlinmvvm.BR

abstract class BaseActivity<BINDING : ViewDataBinding,VIEWMODEL : BaseViewModel>(@LayoutRes val layoutRes : Int) : AppCompatActivity() {
    protected abstract val viewModel: VIEWMODEL
    protected lateinit var binding: BINDING


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBinding()
    }

    private fun initializeBinding() {
        DataBindingUtil.setContentView<BINDING>(this,layoutRes).apply {
            binding = this
            lifecycleOwner = this@BaseActivity
            setVariable(BR.viewModel,viewModel)
            executePendingBindings()
        }
    }

    /**
     * Display Toast
     * @param message String
     * @param duration Int
     */
    fun toast(message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, message, duration).show()
    }

    /**
     * Display Toast
     * @param message Int : String resource ID
     */
    fun toast(@StringRes message: Int) {
        toast(getString(message))
    }
}