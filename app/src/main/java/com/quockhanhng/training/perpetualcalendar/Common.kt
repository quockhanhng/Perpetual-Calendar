package com.quockhanhng.training.perpetualcalendar

val WEEKDAYS =
    arrayOf("Thứ Bảy", "Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu")
val WEEKDAYS_SHORT =
    arrayOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")

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

private fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
}

private fun getDayInMonth(year: Int, month: Int): Int {
    if (month == 4 || month == 6 || month == 9 || month == 11) {
        return 30
    }
    return if (month == 2) {
        if (isLeapYear(year)) {
            29
        } else 28
    } else 31
}

fun getCalendarTable(month: Int, year: Int): IntArray {
    val res = IntArray(42)
    val start = zeller(
        1,
        getMonth4Zeller(month),
        getYear4Zeller(year),
        getCentury4Zeller(year)
    )
    val end = getDayInMonth(year, month)
    for (i in start until end + start) {
        res[i] = i - start + 1
    }
    return res
}

fun getGoodDaysInMonth(month: Int): String {
    return when (month) {
        1, 7 -> {
            "Tý, Sửu, Tị, Mùi"
        }
        2, 8 -> {
            "Dần, Mão, Mùi, Dậu"
        }
        3, 9 -> {
            "Thìn, Tị, Dậu, Hợi"
        }
        4, 10 -> {
            "Ngọ, Mùi, Sửu, Dậu"
        }
        5, 11 -> {
            "Thân, Dậu, Sửu, Mão"
        }
        6, 12 -> {
            "Tuất, Hợi, Mão, Tị"
        }
        else -> ""
    }
}

fun getBadDaysInMonth(month: Int): String {
    return when (month) {
        1, 7 -> {
            "Ngọ, Mão, Hợi, Dậu"
        }
        2, 8 -> {
            "Thân, Tị, Sửu, Hợi"
        }
        3, 9 -> {
            "Tuất, Mùi, Sửu, Hợi"
        }
        4, 10 -> {
            "Tý, Dậu, Tị, Mão"
        }
        5, 11 -> {
            "Dần, Hợi, Mùi, Tị"
        }
        6, 12 -> {
            "Thìn, Sửu, Dậu, Mùi"
        }
        else -> ""
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
