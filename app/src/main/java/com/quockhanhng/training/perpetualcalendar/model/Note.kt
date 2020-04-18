package com.quockhanhng.training.perpetualcalendar.model

import java.io.Serializable

data class Note(
    var content: String,
    var date: String,
    var isLunar: Boolean,
    var reminder: Int,
    var reminderHour: Int,
    var reminderMinute: Int,
    var timeCreated: Long
) : Serializable