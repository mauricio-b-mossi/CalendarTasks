package com.example.canvasexperimentation.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Returns the first day of the week of the month.
 */
fun Month.getFirstDayOfWeekOfMonth(year: Int): DayOfWeek {
    return LocalDate.of(year, this, 1).dayOfWeek
}

/**
 * Returns the number of days in the current month.
 */
fun Month.getDaysInMonth(isLeapYear: Boolean): Int {
    return when (this) {
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

/**
 * Returns the number of days in the current month.
 */
fun Month.getDaysInMonth(year: Int): Int {
    return when (this) {
        Month.JANUARY -> 31
        Month.FEBRUARY -> if (year.isLeapYear()) 29 else 28
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
