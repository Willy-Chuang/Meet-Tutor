package com.willy.metu.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    /**
     * This singleton converts timestamp to required format
     * Be advised the timestamp contains milliseconds
     */

    @JvmStatic
    fun stampToDate(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToYear(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy")
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToMonthInt(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("MM")
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToDay(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd")
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun dateToStamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
        return simpleDateFormat.parse(date).time
    }

    fun stampToWeekday(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        Log.i("test", "$day")
        return when (day) {
            1 -> "SUN"
            2 -> "MON"
            3 -> "TUE"
            4 -> "WED"
            5 -> "THU"
            6 -> "FRI"
            else -> "SAT"
        }
    }

    fun stampToDayOfMonth(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        Log.i("test", "$date")
        return date.toString()
    }

    fun stampToMonth(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val month = calendar.get(Calendar.MONTH)
        Log.i("test", "$month")
        return when (month) {
            0 -> "JAN"
            1 -> "FEB"
            2 -> "MAR"
            3 -> "APR"
            4 -> "MAY"
            5 -> "JUN"
            6 -> "JUL"
            7 -> "AUG"
            8 -> "SEP"
            9 -> "OCT"
            10 -> "NOV"
            else -> "DEC"
        }
    }

    fun stampToTime(time: Long) : String {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return simpleDateFormat.format(Date(time))
    }

}