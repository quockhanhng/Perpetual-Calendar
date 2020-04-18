package com.quockhanhng.training.perpetualcalendar.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    abstract var clickItemListener: (T, Int) -> Unit

    open fun bindData(item: T, index : Int) {
        itemView.setOnClickListener { clickItemListener(item, index) }
    }

}
