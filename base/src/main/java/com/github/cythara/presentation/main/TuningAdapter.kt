package com.github.cythara.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.cythara.R
import kotlinx.android.synthetic.main.list_item_tuning.view.*


class TuningAdapter(clickListener: (String, Int) -> Unit) : BaseAdapter<String>(clickListener) {

    init {
        selection = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        return GroupHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        super.onBindViewHolder(holder, position)
        (holder as GroupHolder).setSelection(position == selection)
    }

    override fun itemClick(bean: String, position: Int) {
        super.itemClick(bean, position)
        notifyDataSetChanged()
    }
}

class GroupHolder(parent: ViewGroup) : BaseViewHolder<String>(parent.inflate(R.layout.list_item_tuning)) {

    override fun bind(item: String) {
        itemView.title.text = item
    }

    fun setSelection(value: Boolean) {
        if (value) {
            itemView.selection.setImageResource(R.drawable.ic_music_outline)
        } else {
            itemView.selection.setImageResource(R.drawable.ic_cirle_outline)
        }
        itemView.selection.show(true)
    }
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.show(value: Boolean) {
    this.visibility = if (value) {
        View.VISIBLE
    } else {
        View.GONE
    }
}