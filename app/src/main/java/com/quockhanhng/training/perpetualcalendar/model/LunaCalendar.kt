package com.quockhanhng.training.perpetualcalendar.model

import java.util.*
import java.util.Date

data class LunaCalendar(
    val year: Int, val month: Int, val day: Int,
    val isToday: Boolean, val isWeekend: Boolean
) {

    private fun getDate(): Date {
        val calendar: Calendar = GregorianCalendar(year, month - 1, day)
        return calendar.time
    }

    fun getMillis(): Long {
        return getDate().time
    }
}