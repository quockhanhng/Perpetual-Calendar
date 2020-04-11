package com.quockhanhng.training.perpetualcalendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.activity.CalendarActivity
import com.quockhanhng.training.perpetualcalendar.getBadDaysInMonth
import com.quockhanhng.training.perpetualcalendar.getGoodDaysInMonth
import com.quockhanhng.training.perpetualcalendar.model.MyDate
import kotlinx.android.synthetic.main.item_grid.view.*
import java.util.*

class CalendarGridAdapter(
    private var context: Context,
    private var items: ArrayList<String>,
    private var today: String,
    private var selectedItem: Int
) :
    RecyclerView.Adapter<CalendarGridAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)

        return CalendarViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = items[position]
        if (item == "0") {
            holder.setBackground(R.color.colorBrownBackground)
        } else {
            val dd = item.split("/")[0].toInt()
            val mm = item.split("/")[1].toInt()
            val yyyy = item.split("/")[2].toInt()
            val myDate = MyDate(dd, mm, yyyy)
            val lunarDate = myDate.getLunar()
            holder.day.text = dd.toString()
            if (position % 7 == 0)
                holder.day.setTextColor(
                    context.resources.getColor(
                        R.color.colorBrown,
                        context.theme
                    )
                )
            if (position % 7 == 1)
                holder.day.setTextColor(
                    context.resources.getColor(
                        R.color.colorRed,
                        context.theme
                    )
                )
            holder.lunarDay.text = lunarDate[0].toString()

            val lunarCanChi = myDate.getLunarDayCanChiName(dd, mm, yyyy)
            val chi = lunarCanChi?.split(" ")?.get(1)
            val goodDays = getGoodDaysInMonth(lunarDate[1])
            val badDays = getBadDaysInMonth(lunarDate[1])
            when {
                goodDays.contains(chi) -> {
                    holder.isSpecialDay.visibility = View.VISIBLE
                    holder.isSpecialDay.setBackgroundResource(android.R.drawable.btn_star_big_on)
                }
                badDays.contains(chi) -> {
                    holder.isSpecialDay.visibility = View.VISIBLE
                    holder.isSpecialDay.setBackgroundResource(android.R.drawable.btn_star_big_off)
                }
                else -> holder.isSpecialDay.visibility = View.GONE
            }

            // Highlight selected day
            holder.setOnClickListener(View.OnClickListener {
                selectedItem = position
                notifyDataSetChanged()
                (context as CalendarActivity).updateInformation(dd, mm, yyyy, myDate)
                (context as CalendarActivity).chosenDay = "$dd/$mm/$yyyy"
            })
            if (item == today) {
                holder.setBackground(R.color.colorTodayBackground)
            }
            if (selectedItem == position) {
                holder.setBackground(R.color.colorSelectBackground)
            } else if (selectedItem != position && item != today) {
                holder.setBackground(R.color.colorNonSelectBackground)
            }
        }
    }

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView = itemView.tvDay
        val lunarDay: TextView = itemView.tvLunarDay
        val isSpecialDay: ImageView = itemView.tvIsSpecialDay

        fun setBackground(id: Int) {
            itemView.setBackgroundResource(id)
        }

        fun setOnClickListener(onClickListener: View.OnClickListener) {
            itemView.setOnClickListener(onClickListener)
        }
    }
}