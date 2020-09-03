package com.kotlinmvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kotlinmvvm.BR
import com.kotlinmvvm.R
import com.kotlinmvvm.databinding.ItemNewsBinding
import com.kotlinmvvm.model.Article
import com.kotlinmvvm.model.Source
import com.kotlinmvvm.utils.listners.OnItemClickListener


class NewsAdapter(
    private val newArticle: ArrayList<Article>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<NewsAdapter.NewViewHolder>() {

    private val itemTypeNormal = 1
    private val itemTypeLoader = 2
    protected var previousArrayList = ArrayList<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        if (viewType == itemTypeNormal) {
            return NewViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_news, parent, false
                )
            )
        } else {
            return NewViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_progress, parent, false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return newArticle.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (newArticle[position].title.equals("")){
            return itemTypeLoader
        }else{
            itemTypeNormal
        }
    }
    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        if(getItemViewType(position) != itemTypeLoader){
            holder.bind(newArticle[position])
            (holder.viewBinding as ItemNewsBinding).root.setOnClickListener {
                listener.onItemClick(it,newArticle[position],position)
            }
        }
    }

    fun setData(newData: ArrayList<Article>,isForRefresh : Boolean) {
//        val diffResult = DiffUtil.calculateDiff(MyDiffUtilCallBack(newData, newArticle))
//        diffResult.dispatchUpdatesTo(this)
        if (newArticle.size >= previousArrayList.size) {
            previousArrayList = newArticle.clone() as ArrayList<Article>
        }
        if(isForRefresh){
            newArticle.clear()
            previousArrayList.clear()
            newArticle.addAll(newData)
            notifyDataSetChanged()
        }else{
            newArticle.addAll(newData)
            notifyItemRangeInserted(previousArrayList.size,newArticle.size)

        }
    }


    /**
     * This fun is return loader item
     * @return T?
     */
    protected open fun getLoaderItem(): Article? {
        return Article("", "", "", "", Source("", ""), "", "", "", false)
    }

    /**
     * This fun is used to add pagination loader.
     */
    fun addLoader() {
        getLoaderItem()?.let { newArticle.add(it) }
        notifyItemInserted(newArticle.size - 1)
    }

    /**
     * This fun is used to pagination remove loader
     */
    fun removeLoader() {
        if (newArticle.isNotEmpty()) {
            newArticle.removeAt(newArticle.size - 1)
            notifyItemRemoved(newArticle.size)
        }
    }

    class NewViewHolder(val viewBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewBinding.root){
        fun bind(article: Article) {
            viewBinding.setVariable(BR.dataModel, article)
            viewBinding.executePendingBindings()
        }
    }

    class MyDiffUtilCallBack(val oldList: ArrayList<Article>, var newList: ArrayList<Article>) :
        DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

    }
}