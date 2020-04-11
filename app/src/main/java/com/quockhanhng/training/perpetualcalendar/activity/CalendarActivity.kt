package com.quockhanhng.training.perpetualcalendar.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.adapter.CalendarGridAdapter
import com.quockhanhng.training.perpetualcalendar.getBadDaysInMonth
import com.quockhanhng.training.perpetualcalendar.getCalendarTable
import com.quockhanhng.training.perpetualcalendar.getGoodDaysInMonth
import com.quockhanhng.training.perpetualcalendar.model.MyDate
import kotlinx.android.synthetic.main.activity_calendar.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var goodDays: List<String>
    private lateinit var badDays: List<String>
    private lateinit var currentDay: String
    private lateinit var lunarDay: MyDate
    private lateinit var displayDate: Calendar
    lateinit var chosenDay: String
    private val today = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        currentDay = intent.getStringExtra("Current day")!!
        lunarDay = intent.getSerializableExtra("Lunar day") as MyDate
        val dateSplit = currentDay.split("/")
        val dd = dateSplit[0].toInt()
        val mm = dateSplit[1].toInt()
        val yyyy = dateSplit[2].toInt()

        displayDate = Calendar.getInstance()
        displayDate.set(Calendar.YEAR, yyyy)
        displayDate.set(Calendar.MONTH, mm - 1)
        displayDate.set(Calendar.DAY_OF_MONTH, dd)

        chosenDay = currentDay

        tvToday.text = SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(today).split("/")[0]
        updateInformation(dd, mm, yyyy, lunarDay)
        updateGrid(dd, mm, yyyy)
    }

    fun moveToPreviousMonth(view: View) {
        displayDate.add(Calendar.MONTH, -1)
        val s =
            SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(displayDate.time).split("/")
        updateGrid(-1, s[1].toInt(), s[2].toInt())
    }

    fun moveToNextMonth(view: View) {
        displayDate.add(Calendar.MONTH, 1)
        val s =
            SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(displayDate.time).split("/")
        updateGrid(-1, s[1].toInt(), s[2].toInt())
    }

    fun updateInformation(dd: Int, mm: Int, yyyy: Int, lunarDay: MyDate) {
        tvDay.text = "Dương lịch: $dd - $mm - $yyyy"
        val todayLunar = lunarDay.getLunar()
        tvLunarDay.text =
            "Âm lịch: " + todayLunar[0] + " - " + todayLunar[1] + " - " + todayLunar[2]
        val newLunarDay = lunarDay.getLunarDayCanChiName(dd, mm, yyyy)
        val newLunarMonth = lunarDay.getLunarMonthCanChiName(todayLunar[2], todayLunar[1])
        val newLunarYear = lunarDay.getCANCHI()
        tvLunarDate.text =
            "Ngày " + newLunarDay + ", tháng " + newLunarMonth + ", năm " + newLunarYear

        val chi = newLunarDay?.split(" ")?.get(1)
        goodDays = getGoodDaysInMonth(todayLunar[1])
        badDays = getBadDaysInMonth(todayLunar[1])
        when {
            goodDays.contains(chi) -> {
                tvIsGoodDay.visibility = View.VISIBLE
                tvIsGoodDay.text = "Ngày Hoàng Đạo"
                tvIsGoodDay.setTextColor(Color.YELLOW)
            }
            badDays.contains(chi) -> {
                tvIsGoodDay.visibility = View.VISIBLE
                tvIsGoodDay.text = "Ngày Hắc Đạo"
                tvIsGoodDay.setTextColor(Color.DKGRAY)
            }
            else -> tvIsGoodDay.visibility = View.GONE
        }
    }

    private fun updateGrid(dd: Int, mm: Int, yyyy: Int) {
        tvMonth.text = "$mm/$yyyy"
        calendar_grid.layoutManager = GridLayoutManager(this, 7)
        val calendarTable = getCalendarTable(mm, yyyy)
        var index = -1
        if (dd != -1) {
            index = 0
            while ("$dd/$mm/$yyyy" != calendarTable[index])
                index++
        }
        calendar_grid.adapter = CalendarGridAdapter(
            this,
            calendarTable,
            SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(today),
            index
        )
    }

    fun goToDate(view: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("Chosen day", chosenDay)
        startActivity(intent)
    }

    fun goBackToday(view: View) {
        displayDate.time = today
        goToChosenDay()
    }

    fun openDatePicker(view: View) {
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                displayDate.set(Calendar.YEAR, year)
                displayDate.set(Calendar.MONTH, month)
                displayDate.set(Calendar.DAY_OF_MONTH, day)
                goToChosenDay()
            },
            displayDate.get(Calendar.YEAR),
            displayDate.get(Calendar.MONTH),
            displayDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun goToChosenDay() {
        val dd = displayDate.get(Calendar.DAY_OF_MONTH)
        val mm = displayDate.get(Calendar.MONTH) + 1
        val yyyy = displayDate.get(Calendar.YEAR)
        lunarDay = MyDate(dd, mm, yyyy)

        chosenDay = SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(displayDate.time)

        updateInformation(dd, mm, yyyy, lunarDay)
        updateGrid(dd, mm, yyyy)
    }
}
