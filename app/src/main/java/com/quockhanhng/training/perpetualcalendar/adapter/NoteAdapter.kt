package com.quockhanhng.training.perpetualcalendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.dateToMilliseconds
import com.quockhanhng.training.perpetualcalendar.model.Note
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter(
    override var items: ArrayList<Note>,
    override var clickItemListener: (Note, Int) -> Unit
) : BaseAdapter<Note, NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false),
        clickItemListener
    )

    fun removeNote(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    fun updateNote(Note: Note, index: Int) {
        items[index] = Note
        notifyItemChanged(index)
    }

    fun sortByTimeCreated() {
        items.sortBy { Note -> Note.timeCreated }
        notifyDataSetChanged()
    }

    fun sortByTimeCreatedDescending() {
        items.sortByDescending { Note -> Note.timeCreated }
        notifyDataSetChanged()
    }

    fun sortByDate() {
        items.sortBy { Note ->
            dateToMilliseconds(Note.date)
        }
        notifyDataSetChanged()
    }

    fun sortByDateDescending() {
        items.sortByDescending { Note ->
            dateToMilliseconds(Note.date)
        }
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView: View, override var clickItemListener: (Note, Int) -> Unit) :
        BaseViewHolder<Note>(itemView) {
        override fun bindData(item: Note, index: Int) {
            super.bindData(item, index)
            itemView.apply {
                if (dateToMilliseconds(item.date) <= Date().time)
                    ivIsPassBy.setImageResource(R.drawable.ic_checked)
                else
                    ivIsPassBy.setImageResource(R.drawable.ic_pending)
                tvContent.text = item.content
                if (item.isLunar)
                    tvDate.text = "* Ngày: ${item.date} (Âm lịch)"
                else
                    tvDate.text = "* Ngày: ${item.date} (Dương lịch)"
                if (item.reminder == 0)
                    tvReminder.text = "* Không nhắc"
                else {
                    val timeReminder = ", lúc ${item.reminderHour}:${item.reminderMinute}"
                    tvReminder.text =
                        resources.getStringArray(R.array.spinner_reminder_options)[item.reminder] + timeReminder
                }
            }
        }
    }
}
