package com.quockhanhng.training.perpetualcalendar

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val WEEKDAYS =
    arrayOf("Thứ Bảy", "Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu")

val WEEKDAYS_SHORT =
    arrayOf("T7", "CN", "T2", "T3", "T4", "T5", "T6")


private fun getCentury4Zeller(year: Int): Int {
    return year / 100
}

private fun getMonth4Zeller(month: Int): Int {
    return if (month < 3) month + 10 else month
}

private fun getYear4Zeller(year: Int): Int {
    return year % 100
}

private fun zeller(day: Int, month: Int, year: Int, century: Int): Int {
    return Math.floorMod(
        day + 13 * (month + 1) / 5 + year + year / 4 + century / 4 - 2 * century, 7
    )
}

// Get the day of week
fun getWeekDay(day: Int, month: Int, year: Int): String? {
    val i = zeller(
        day,
        getMonth4Zeller(month),
        getYear4Zeller(year),
        getCentury4Zeller(year)
    )
    return WEEKDAYS[i]
}

fun getWeekDayShort(day: Int, month: Int, year: Int): String? {
    val i = zeller(
        day,
        getMonth4Zeller(month),
        getYear4Zeller(year),
        getCentury4Zeller(year)
    )
    return WEEKDAYS_SHORT[i]
}

fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
}

fun getDayInMonth(year: Int, month: Int): Int {
    if (month == 4 || month == 6 || month == 9 || month == 11) {
        return 30
    }
    return if (month == 2) {
        if (isLeapYear(year)) {
            29
        } else 28
    } else 31
}

fun getCalendarTable(month: Int, year: Int): ArrayList<String> {
    val res = ArrayList<String>()
    val start = zeller(
        1,
        getMonth4Zeller(month),
        getYear4Zeller(year),
        getCentury4Zeller(year)
    )
    val end = getDayInMonth(year, month)
    for (i in 0 until start)
        res.add("0")
    for (i in start until end + start) {
        val d = i - start + 1
        res.add("$d/$month/$year")
    }
    for (i in end + start until 42)
        res.add("0")
    return res
}

fun getGoodDaysInMonth(month: Int): List<String> {
    return when (month) {
        1, 7 -> {
            arrayListOf("Tý", " Sửu", "Tị", "Mùi")
        }
        2, 8 -> {
            arrayListOf("Dần", "Mão", "Mùi", "Dậu")
        }
        3, 9 -> {
            arrayListOf("Thìn", "Tị", "Dậu", "Hợi")
        }
        4, 10 -> {
            arrayListOf("Ngọ", "Mùi", "Sửu", "Dậu")
        }
        5, 11 -> {
            arrayListOf("Thân", "Dậu", "Sửu", "Mão")
        }
        6, 12 -> {
            arrayListOf("Tuất", "Hợi", "Mão", "Tị")
        }
        else -> arrayListOf("")
    }
}

fun getBadDaysInMonth(month: Int): List<String> {
    return when (month) {
        1, 7 -> {
            arrayListOf("Ngọ", "Mão", "Hợi", "Dậu")
        }
        2, 8 -> {
            arrayListOf("Thân", "Tị", "Sửu", "Hợi")
        }
        3, 9 -> {
            arrayListOf("Tuất", "Mùi", "Sửu", "Hợi")
        }
        4, 10 -> {
            arrayListOf("Tý", " Dậu", "Tị", "Mão")
        }
        5, 11 -> {
            arrayListOf("Dần", "Hợi", "Mùi", "Tị")
        }
        6, 12 -> {
            arrayListOf("Thìn", "Sửu", "Dậu", "Mùi")
        }
        else -> arrayListOf("")
    }
}

fun getGoodHourInDay(day: Int): String {
    return when (day) {
        1, 7 -> {
            "Tý, Sửu, Mão, Ngọ, Thân, Dậu"
        }
        2, 8 -> {
            "Dần, Mão, Tị, Thân, Tuất, Hợi"
        }
        3, 9 -> {
            "Tý, Sửu, Thìn, Tị, Mùi, Tuất"
        }
        4, 10 -> {
            "Tý, Dần, Mão, Ngọ, Mùi, Dậu"
        }
        5, 11 -> {
            "Dần, Thìn, Tị, Thân, Dậu, Hợi"
        }
        6, 12 -> {
            "Sửu, Thìn, Ngọ, Mùi, Tuất, Hợi"
        }
        else -> ""
    }
}

fun getPreviousDay(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return calendar.time
}

fun getNextDay(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}

fun dateToMilliseconds(dateString: String): Long {
    val sdf = SimpleDateFormat("dd - MM - yyyy")
    val date = sdf.parse(dateString)!!
    return date.time
}
