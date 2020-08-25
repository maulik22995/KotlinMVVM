package com.kotlinmvvm.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is base class for all recycler adapters
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<BaseRecyclerAdapter<T>.RecyclerHolder>(),
    Filterable {
    private var arrayListFiltered = ArrayList<T>()
    private val itemTypeNormal = 1
    private val itemTypeLoader = 2
    private var filteredText = ""
    protected val arrayList = ArrayList<T>()
    protected var previousArrayList = ArrayList<T>()
    protected lateinit var binding : ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        binding = if (viewType == itemTypeNormal) {
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayoutIdForType(viewType), parent, false)
        } else {
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayoutIdForLoading(viewType), parent, false)
        }
        return RecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (isItemLoading(position)) {
            itemTypeLoader
        } else {
            itemTypeNormal
        }
    }


    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(arrayList[position], position)
    }

    /**
     * This is abstract function used to get view type for adapter
     */
    abstract fun getLayoutIdForType(viewType: Int): Int

    /**
     * This fun is used to get layout for loader.
     * @param viewType Int
     * @return Int
     */
    open fun getLayoutIdForLoading(viewType: Int): Int {
        return 0
    }

    /**
     * This is abstract function used to get item click for all the adapter views
     */
    abstract fun onItemClick(view: View?, item : T, adapterPosition: Int)

    /**
     * This is abstract function used to get set data for recycler list items.
     */
    open fun setDataForListItem(binding: ViewDataBinding, dataModel: T, position : Int) {

    }

    /**
     * This is inner class used to set recycler view holder.
     */
    inner class RecyclerHolder(private val viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root), View.OnClickListener {
        /**
         * This function is used to bind recycler data particular row wise.
         */
        fun bind(dataModel: T, position: Int) {
//            viewDataBinding.setVariable(BR.dataModel, dataModel)
            setDataForListItem(viewDataBinding, dataModel, position)
            viewDataBinding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            onItemClick(v,arrayList[position], adapterPosition)
        }

    }

    /**
     * This fun is used to save list
     * @param newList ArrayList<T>
     */
    fun setList(newList: ArrayList<T>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(arrayList, newList))
        if (arrayList.size >= previousArrayList.size) {
            previousArrayList = arrayList.clone() as ArrayList<T>
        }
        arrayList.clear()
        arrayList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * This fun is used to save list
     * @param newList ArrayList<T>
     */
    fun addList(newList: ArrayList<T>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(arrayList, newList))
        if (arrayList.size >= previousArrayList.size) {
            previousArrayList = arrayList.clone() as ArrayList<T>
        }
        arrayList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }


    /**
     * This fun is used to add all items in list
     * @param newList ArrayList<T>
     */
    fun addAllItem(newList: ArrayList<T>) {
        val newTempList = ArrayList(arrayList)
        newTempList.addAll(newList)
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(arrayList, newTempList))
        arrayList.clear()
        arrayList.addAll(newTempList)
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * This fun is used to remove item from list
     * @param item T
     */
    fun removeItem(item: T) {
        val newList = ArrayList(arrayList)
        newList.remove(item)
        setList(newList)
    }

    /**
     * This fun is used to add item
     * @param item T
     */
    fun addItem(item: T) {
        val newList = ArrayList(arrayList)
        newList.add(item)
        setList(newList)
    }

    /**
     * This fun is used to add item at specific position
     * @param index Int
     * @param item T
     */
    fun addItemAt(index: Int, item: T) {
        val newList = ArrayList(arrayList)
        newList.add(index, item)
        setList(newList)
    }

    /**
     * This fun is used to set item at specific position
     * @param index Int
     * @param item T
     */
    fun setItemAt(index: Int, item: T) {
        arrayList[index] = item
        notifyItemChanged(index)
    }

    /**
     * This fun is used to remove item from specific position
     * @param index Int
     */
    fun removeItem(index: Int) {
        val newList = ArrayList(arrayList)
        newList.removeAt(index)
        setList(newList)
    }

    /**
     * This class is used to get all data from adapter
     * @return ArrayList<T>
     */
    fun getListItems(): ArrayList<T> {
        return arrayList
    }

    /**
     * This fun is used to clear list
     */
    fun clearList() {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(arrayList, ArrayList()))
        arrayList.clear()
        arrayList.addAll(ArrayList())
        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * This class is used as diff util base class.
     * @property oldList ArrayList<T>
     * @property newList ArrayList<T>
     * @constructor
     */
    inner class BaseDiffUtil(private val oldList: ArrayList<T>, private val newList: ArrayList<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsSame(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }

    }

    /**
     * This fun is used to get list item same or not.
     * @param oldItem T
     * @param newItem T
     * @return Boolean
     */
    abstract fun areItemsSame(oldItem: T, newItem: T): Boolean


    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * This fun is return loader item
     * @return T?
     */
    protected open fun getLoaderItem(): T? {
        return null
    }

    /**
     * This fun is used to add pagination loader.
     */
    fun addLoader() {
        if (!isLoading()) {
            val newList = ArrayList<T>(arrayList)
            getLoaderItem()?.let { newList.add(it) }
            setList(newList)
        }
    }

    /**
     * This fun is used to pagination remove loader
     */
    fun removeLoader() {
        if (isLoading()) {
            if (arrayList.isNotEmpty()) {
                val newList = ArrayList<T>(arrayList)
                newList.remove(getLoaderItem())
                setList(newList)
            }
        }
    }

    /**
     * This fun is used to know item is loading or not.
     * @return Boolean
     */
    internal fun isLoading(): Boolean {
        return arrayList.isEmpty() || isLastItemLoading()
    }

    /**
     * This fun is used to returns that last item is loading or not
     * @return Boolean
     */
    open fun isLastItemLoading(): Boolean {
        return false
    }

    /**
     * This fun is used to get particular item is loading or not.
     * @param position Int
     * @return Boolean
     */
    open fun isItemLoading(position: Int): Boolean {
        return false
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                filteredText = charSequence.toString()
                arrayListFiltered = if (filteredText.isBlank()) {
                    previousArrayList
                } else {
                    getFilteredResults(filteredText)
                }
                val filterResults = FilterResults()
                filterResults.values = arrayListFiltered
                filterResults.count = arrayListFiltered.size
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                arrayListFiltered = filterResults.values as ArrayList<T>
                setList(arrayListFiltered)
            }
        }
    }

    /**
     * This fun is used to get filtered result.
     * @param constraint String
     * @return ArrayList<T>
     */
    open fun getFilteredResults(constraint: String): ArrayList<T> {
        return arrayList
    }
}