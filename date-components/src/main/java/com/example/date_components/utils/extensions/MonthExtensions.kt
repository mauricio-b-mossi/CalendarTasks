package com.example.date_components.utils.extensions

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

/**
 * Returns the first day of the week for the specified year and month.
 *
 * @param year the year
 * @return the first day of the week for the specified year and month
 */
fun Month.getFirstDayOfWeekOfMonth(year: Int): DayOfWeek {
    return LocalDate.of(year, this, 1).dayOfWeek
}

/**
 * Returns the number of days in the month based on the given leap year status.
 *
 * @param isLeapYear a boolean indicating whether the month is part of a leap year
 * @return the number of days in the month
 */
internal fun Month.getDaysInMonth(isLeapYear: Boolean): Int {
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
 * Returns the number of days in the month based on the given year.
 *
 * @param year the year
 * @return the number of days in the month
 */
internal fun Month.getDaysInMonth(year: Int): Int {
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
