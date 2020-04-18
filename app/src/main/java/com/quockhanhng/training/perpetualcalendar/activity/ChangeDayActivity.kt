package com.quockhanhng.training.perpetualcalendar.activity

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.quockhanhng.training.perpetualcalendar.*
import com.quockhanhng.training.perpetualcalendar.model.MyDate
import kotlinx.android.synthetic.main.activity_change_day.*
import java.util.*

class ChangeDayActivity : AppCompatActivity() {

    private lateinit var displayDate: String
    private lateinit var lunarDate: MyDate
    private var rightNow = Date()
    private val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_day)

        displayDate = intent.getStringExtra("Current day") ?: ""
        tvToday.text = sdf.format(rightNow).split("/")[0]

        val spl = displayDate.split("/")
        val dd = spl[0].toInt()
        val mm = spl[1].toInt()
        val yyyy = spl[2].toInt()

        datePicker.init(yyyy, mm - 1, dd) { _, year, month, day ->
            displayDate = "$day/${month + 1}/$year"
            display()
        }
        display()
    }

    private fun display() {
        val spl = displayDate.split("/")
        val dd = spl[0].toInt()
        val mm = spl[1].toInt()
        val yyyy = spl[2].toInt()

        lunarDate = MyDate(dd, mm, yyyy)

        val lunarArr = lunarDate.getLunar()
        lunarDatePicker.updateDate(lunarArr[2], lunarArr[1] - 1, lunarArr[0])

        val newLunarDay = lunarDate.getLunarDayCanChiName(dd, mm, yyyy)
        val newLunarMonth = lunarDate.getLunarMonthCanChiName(lunarArr[2], lunarArr[1])
        val newLunarYear = lunarDate.getCANCHI()
        val lunarDateInfo = "Ngày $newLunarDay, tháng $newLunarMonth, năm $newLunarYear"
        tvLunarInfo.text = lunarDateInfo

        val chi = newLunarDay?.split(" ")?.get(1)
        val goodDays = getGoodDaysInMonth(lunarArr[1])
        val badDays = getBadDaysInMonth(lunarArr[1])
        val isSpecial = when {
            goodDays.contains(chi) -> {
                "Ngày Hoàng Đạo"
            }
            badDays.contains(chi) -> {
                "Ngày Hắc Đạo"
            }
            else -> "Ngày thường"
        }
        val weekDay = getWeekDay(dd, mm, yyyy)
        tvInfo.text = "$displayDate - $weekDay - $isSpecial"
    }

    fun goBackToday(view: View) {
        displayDate = sdf.format(rightNow)
        val arr = displayDate.split("/")
        datePicker.updateDate(arr[2].toInt(), arr[1].toInt() - 1, arr[0].toInt())
        display()
    }

    fun close(view: View) {
        finish()
    }

    fun goToThisDay(view: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("Chosen day", displayDate)
        startActivity(intent)
    }
}
