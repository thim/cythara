package com.github.cythara.presentation.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.NO_ID

abstract class BaseAdapter<T>(internal val clickListener: (T, position: Int) -> Unit) : RecyclerView.Adapter<BaseViewHolder<T>>() {
    private val mList: ArrayList<T> = ArrayList()

    var selection: Int? = null

    constructor(listener: (T) -> Unit) : this(object : (T, Int) -> Unit {
        override fun invoke(item: T, pos: Int) {
            listener(item)
        }
    })

    open fun addItems(items: List<T>) {
        mList.clear()
        mList.addAll(items)
    }

    fun getItem(pos: Int): T {
        return mList[pos]
    }

    fun getItems(): List<T> {
        return mList
    }

    fun clear() {
        mList.clear()
    }

    open fun itemClick(bean: T, position: Int) {
        clickListener(bean, position)
        selection = position
    }

    override fun getItemId(position: Int): Long {
        return mList[position]?.hashCode()?.toLong() ?: NO_ID.toLong()
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val bean: T = mList[position]
        holder.bind(bean)
        holder.itemView.setOnClickListener { itemClick(bean, position) }
    }
}

abstract class BaseViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}