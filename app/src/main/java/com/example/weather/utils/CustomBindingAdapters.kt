package com.example.weather.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

object CustomBindingAdapters {
    @BindingAdapter("setUnixDate")
    @JvmStatic
    fun TextView.setUnixDate(date : Int) {
        text = UnixTimeToDate().convertUnixTimestampToDate(date)
    }
}