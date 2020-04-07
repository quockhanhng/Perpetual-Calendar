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