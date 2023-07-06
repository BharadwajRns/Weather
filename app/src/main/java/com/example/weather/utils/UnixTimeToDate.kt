package com.example.weather.utils

import java.text.SimpleDateFormat
import java.util.*

class UnixTimeToDate {
    fun convertUnixTimestampToDate(unixTimestamp: Int): String {
        val time = unixTimestamp.toLong()
        val date = Date(time * 1000L) // Convert Unix timestamp to milliseconds
        val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return sdf.format(date)
    }
}