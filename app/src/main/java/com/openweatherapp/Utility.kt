package com.openweatherapp

import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utility {

    private const val DATE_FORMAT = "yyyyMMdd"

    public fun  getFriendlyDayString(
        context: Context,
        dateStr: String
    ): String? {
        val todayDate = Date()
        val todayStr: String = getDbDateString(todayDate)
        val inputDate: Date? = getDateFromDb(dateStr)

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (todayStr == dateStr) {
            val today = context.getString(R.string.today)
            val formatId: Int = R.string.format_full_friendly_date
            return String.format(context.getString(formatId, today, getFormattedMonthDay(context, dateStr)))
        } else {
            val cal = Calendar.getInstance()
            cal.time = todayDate
            cal.add(Calendar.DATE, 7)
            val weekFutureString: String = getDbDateString(cal.time).toString()
            if (dateStr < weekFutureString) {
                // If the input date is less than a week in the future, just return the day name.
                return getDayName(context, dateStr)
            } else {
                val shortenedDateFormat =
                    SimpleDateFormat("EEE MMM dd")
                return   shortenedDateFormat.format(inputDate)
            }
        }
    }

    private fun getFormattedMonthDay(
        context: Context?,
        dateStr: String?
    ): String? {
        val dbDateFormat =
            SimpleDateFormat(DATE_FORMAT)
        return try {
            val inputDate = dbDateFormat.parse(dateStr)
            val monthDayFormat = SimpleDateFormat("MMMM dd")
            monthDayFormat.format(inputDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    private fun getDayName(context: Context, dateStr: String?): String? {
        val dbDateFormat =
            SimpleDateFormat(DATE_FORMAT)
        return try {
            val inputDate = dbDateFormat.parse(dateStr)
            val todayDate = Date()
            if (getDbDateString(todayDate).equals(dateStr)) {
                context.getString(R.string.today)
            } else {
                // If the date is set for tomorrow, the format is "Tomorrow".
                val cal = Calendar.getInstance()
                cal.time = todayDate
                cal.add(Calendar.DATE, 1)
                val tomorrowDate = cal.time
                if (getDbDateString(tomorrowDate).equals(dateStr)) {
                    context.getString(R.string.tomorrow)
                } else {
                    // Otherwise, the format is just the day of the week (e.g "Wednesday".
                    val dayFormat = SimpleDateFormat("EEEE")
                    dayFormat.format(inputDate)
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }
    private fun getDateFromDb(dateText: String?): Date? {
        val dbDateFormat =
            SimpleDateFormat(DATE_FORMAT)
        return try {
            dbDateFormat.parse(dateText)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
    public fun getDbDateString(date: Date?): String {
        val sdf =
            SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date)
    }
}