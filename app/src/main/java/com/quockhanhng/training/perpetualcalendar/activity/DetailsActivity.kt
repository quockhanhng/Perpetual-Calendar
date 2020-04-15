package com.quockhanhng.training.perpetualcalendar.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.quockhanhng.training.perpetualcalendar.*
import com.quockhanhng.training.perpetualcalendar.model.MyDate
import com.quockhanhng.training.perpetualcalendar.model.MyDate.Companion.CHI
import kotlinx.android.synthetic.main.activity_details.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailsActivity : AppCompatActivity() {

    private lateinit var displayDate: String
    private lateinit var lunarDate: MyDate
    private lateinit var date: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        displayDate = intent.getStringExtra("Current day") ?: ""
        lunarDate = intent.getSerializableExtra("Lunar day") as MyDate

        display()
    }

    fun goToPrevDay(view: View) {
        date.add(Calendar.DATE, -1)
        displayDate = SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(date.time)
        val s = displayDate.split("/")
        lunarDate = MyDate(s[0].toInt(), s[1].toInt(), s[2].toInt())

        display()
    }

    fun goToNextDay(view: View) {
        date.add(Calendar.DATE, 1)
        displayDate = SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(date.time)
        val s = displayDate.split("/")
        lunarDate = MyDate(s[0].toInt(), s[1].toInt(), s[2].toInt())

        display()
    }

    private fun display() {
        val spl = displayDate.split("/")
        val dd = spl[0].toInt()
        val mm = spl[1].toInt()
        val yyyy = spl[2].toInt()

        date = Calendar.getInstance()
        date.set(Calendar.YEAR, yyyy)
        date.set(Calendar.MONTH, mm - 1)
        date.set(Calendar.DAY_OF_MONTH, dd)

        val title = getWeekDay(dd, mm, yyyy) + ", " + displayDate
        tvDayTitle.text = title

        val lunarArr = lunarDate.getLunar()
        val lunarDateString = lunarArr[0].toString() + " - " + lunarArr[1] + " - " + lunarArr[2]
        tvLunarDay.text = lunarDateString

        val newLunarDay = lunarDate.getLunarDayCanChiName(dd, mm, yyyy)
        val newLunarMonth = lunarDate.getLunarMonthCanChiName(lunarArr[2], lunarArr[1])
        val newLunarYear = lunarDate.getCANCHI()
        val lunarDateInfo = "$newLunarDay - $newLunarMonth - $newLunarYear"
        tvLunarDayInfo.text = lunarDateInfo

        val chi = newLunarDay?.split(" ")?.get(1)
        val goodDays = getGoodDaysInMonth(lunarArr[1])
        val badDays = getBadDaysInMonth(lunarArr[1])
        when {
            goodDays.contains(chi) -> {
                isSpecialDay.visibility = View.VISIBLE
                tvIsSpecialDay.text = "Ngày Hoàng Đạo"
                tvIsSpecialDay.setTextColor(Color.YELLOW)
                ivStar1.setImageResource(android.R.drawable.btn_star_big_on)
                ivStar2.setImageResource(android.R.drawable.btn_star_big_on)
            }
            badDays.contains(chi) -> {
                isSpecialDay.visibility = View.VISIBLE
                tvIsSpecialDay.text = "Ngày Hắc Đạo"
                tvIsSpecialDay.setTextColor(Color.DKGRAY)
                ivStar1.setImageResource(android.R.drawable.btn_star_big_off)
                ivStar2.setImageResource(android.R.drawable.btn_star_big_off)
            }
            else -> isSpecialDay.visibility = View.GONE
        }

        val goodHours = getHours(getGoodHourInDay(CHI.indexOf(chi) + 1))
        tvHour1.text = goodHours[0]
        tvHour2.text = goodHours[1]
        tvHour3.text = goodHours[2]
        tvHour4.text = goodHours[3]
        tvHour5.text = goodHours[4]
        tvHour6.text = goodHours[5]

        tvTietKhi.text = lunarDate.getTIETKHI()
    }

    private fun getHours(s: String): ArrayList<String> {
        val result = ArrayList<String>()
        val arr = s.split(",")
        for (hour in arr) {
            val hourTrim = hour.trim()
            result.add(
                when (hourTrim) {
                    "Tý" -> "Tý: (23h - 1h)"
                    "Sửu" -> "Sửu: (1h - 3h)"
                    "Dần" -> "Dần: (3h - 5h)"
                    "Mão" -> "Mão: (5h - 7h)"
                    "Thìn" -> "Thìn: (7h - 9h)"
                    "Tị" -> "Tị: (9h - 11h)"
                    "Ngọ" -> "Ngọ: (11h - 13h)"
                    "Mùi" -> "Mùi: (13h - 15h)"
                    "Thân" -> "Thân: (15h - 17h)"
                    "Dậu" -> "Dậu: (17h - 19h)"
                    "Tuất" -> "Tuất: (19h - 21h)"
                    "Hợi" -> "Hợi: (21h - 23h)"
                    else -> ""
                }
            )
        }
        return result
    }
}
