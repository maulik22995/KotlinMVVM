package com.kotlinmvvm.utils.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("coverImage")
fun loadImage(imageView: ImageView,imageUrl : String?){
    Glide.with(imageView.context)
        .setDefaultRequestOptions(RequestOptions().centerCrop())
        .load(imageUrl)
        .centerCrop()
        .into(imageView)
}