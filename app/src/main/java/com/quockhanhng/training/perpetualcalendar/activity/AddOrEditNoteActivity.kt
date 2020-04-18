package com.quockhanhng.training.perpetualcalendar.activity

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.SharedPrefUtil
import com.quockhanhng.training.perpetualcalendar.SharedPrefUtil.KEY_LIST_NOTE
import com.quockhanhng.training.perpetualcalendar.model.Note
import kotlinx.android.synthetic.main.activity_add_or_edit_note.*

class AddOrEditNoteActivity : AppCompatActivity() {

    private lateinit var noteList: ArrayList<Note>
    private var isExistNote: Boolean = false
    private lateinit var selectedNote: Note
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_note)
        if (intent.getSerializableExtra("Selected Note") != null) {
            selectedNote = intent.getSerializableExtra("Selected Note") as Note
            index = intent.getIntExtra("Index", -1)
            isExistNote = true
        }

        val noteListString = SharedPrefUtil.getData(KEY_LIST_NOTE)
        noteList = ArrayList()
        if (!noteListString.equals(""))
            noteList = Gson().fromJson(noteListString, object : TypeToken<List<Note>>() {}.type)

        spinnerIsLunar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                p1: View?,
                pos: Int,
                p3: Long
            ) {
                Log.d(
                    "Note",
                    "OnItemSelectedListener : " + adapterView?.getItemAtPosition(pos).toString()
                )
            }
        }
        spinnerReminder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?, p1: View?, pos: Int, p3: Long
            ) {
                if (pos == 0)
                    hourContainer.visibility = View.GONE
                else
                    hourContainer.visibility = View.VISIBLE
                Log.d(
                    "Note",
                    "OnItemSelectedListener : " + adapterView?.getItemAtPosition(pos).toString()
                )
            }

        }

        if (isExistNote) {
            if (selectedNote.isLunar) spinnerIsLunar.setSelection(1)
            else spinnerIsLunar.setSelection(0)
            val arr = selectedNote.date.split("-")
            datePicker.updateDate(
                arr[2].trim().toInt(),
                arr[1].trim().toInt() - 1,
                arr[0].trim().toInt()
            )
            spinnerReminder.setSelection(selectedNote.reminder)
            var hourString: String = if (selectedNote.reminderHour < 10)
                "0${selectedNote.reminderHour} : "
            else
                "${selectedNote.reminderHour} : "
            hourString += if (selectedNote.reminderMinute < 10)
                "0${selectedNote.reminderMinute}"
            else
                "${selectedNote.reminderMinute}"
            edtContent.setText(selectedNote.content)
            tvHour.text = hourString
        }
    }

    fun saveNote(view: View) {
        val spinner1Option = spinnerIsLunar.selectedItem.toString()
        val spinner2Option = spinnerReminder.selectedItem.toString()
        val date = "${datePicker.dayOfMonth} - ${datePicker.month} - ${datePicker.year}"
        val content = edtContent.text.toString()
        val isLunar = (spinner1Option == "Theo Âm lịch")
        val reminder =
            resources.getStringArray(R.array.spinner_reminder_options).indexOf(spinner2Option)
        val hour = tvHour.text.split(":")[0].trim().toInt()
        val minute = tvHour.text.split(":")[1].trim().toInt()
        val timeCreated = System.currentTimeMillis()
        val note = Note(content, date, isLunar, reminder, hour, minute, timeCreated)

        if (isExistNote)
            noteList[index] = note
        else
            noteList.add(note)
        SharedPrefUtil.setData(KEY_LIST_NOTE, Gson().toJson(noteList))
        finish()
    }

    fun deleteNote(view: View) {
        if (isExistNote) {
            val builder = AlertDialog.Builder(this).apply {
                setTitle(R.string.app_name)
                setMessage("Bạn có muốn xóa ghi chú?")
                setPositiveButton(android.R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    noteList.removeAt(index)
                    SharedPrefUtil.setData(KEY_LIST_NOTE, Gson().toJson(noteList))
                    finish()
                }
                setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            builder.show()
        } else {
            edtContent.text.clear()
        }
    }

    fun chooseTime(view: View) {
        TimePickerDialog(
            this, 3,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val hourString: String =
                    if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                val minuteString: String = if (minute < 10) "0$minute" else minute.toString()
                tvHour.text = "$hourString : $minuteString"
            },
            tvHour.text.split(":")[0].trim().toInt(),
            tvHour.text.split(":")[1].trim().toInt(),
            true
        ).show()
    }
}
