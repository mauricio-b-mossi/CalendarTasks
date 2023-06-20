package com.example.canvasexperimentation.utils.extensions

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Returns the first day of the week of the month.
 */
fun LocalDate.getFirstDayOfWeekOfMonth(): DayOfWeek {
    return minusDays((dayOfMonth - 1).toLong()).dayOfWeek
}

/**
 * Returns the number of days in the current month.
 */
fun LocalDate.getDaysInMonth(): Int {
    return when (this.month) {
        Month.JANUARY -> 31
        Month.FEBRUARY -> if (isLeapYear) 29 else 28
        Month.MARCH -> 31
        Month.APRIL -> 30
        Month.MAY -> 31
        Month.JUNE -> 30
        Month.JULY -> 31
        Month.AUGUST -> 31
        Month.SEPTEMBER -> 30
        Month.OCTOBER -> 31
        Month.NOVEMBER -> 30
        Month.DECEMBER -> 31
    }
}

fun LocalDate.withIn(month: Month, year: Int): Boolean = this.month == month && this.year == year