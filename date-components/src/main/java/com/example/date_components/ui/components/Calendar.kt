package com.example.date_components.ui.components


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalTextApi::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarRow(
    dateRange: List<LocalDate>,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    state: LazyListState = rememberLazyListState(),
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    activeColor: Color,
    inactiveColor: Color,
    selectedColor: Color,
    itemPadding: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val maxWidth = maxWidth
        LazyRow(
            state = state,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
        ) {
            items(dateRange) { localDate ->
                Column(modifier = Modifier.padding(horizontal = itemPadding)) {
                    Month(
                        date = date,
                        onDateSelect = { localDate ->
                            onDateChange(localDate)
                        },
                        activeColor = activeColor,
                        inactiveColor = inactiveColor,
                        selectedColor = selectedColor,
                        month = localDate.month,
                        year = localDate.year,
                        textMeasurer = textMeasurer,
                        modifier = Modifier.width(maxWidth - itemPadding * 2)
                    )
                }
            }
        }
    }

}

/**
 * Generates a list of LocalDate objects representing a calendar range.
 *
 * @param date  the reference date from which the range is generated (default: current date)
 * @param range the number of years before and after the reference date to include in the range
 * @return a list of LocalDate objects representing the calendar range
 */
fun generateCalendarRange(date: LocalDate = LocalDate.now(), range: Int): List<LocalDate> {
    val start = LocalDate.of(date.year - range, java.time.Month.JANUARY, 1)
    val end = LocalDate.of(date.year + range, java.time.Month.DECEMBER, 1)
    val months = start.until(end, ChronoUnit.MONTHS)
    return buildList {
        for (i in 0..months) {
            add(LocalDate.of(start.year.plus(i / 12).toInt(), ((i % 12) + 1).toInt(), 1))
        }
    }
}

/**
 * Calculates the month index between the current date and a specified from date.
 *
 * @param currentDate the current date (default: current system date)
 * @param fromDate    the from date to compare against
 * @return the month index between the current date and the from date
 */
fun getMonthIndex(currentDate: LocalDate = LocalDate.now(), fromDate: LocalDate): Int {
    return if (currentDate.month.value >= fromDate.month.value && currentDate.year >= fromDate.year) {
        (currentDate.month.value - fromDate.month.value) + (currentDate.year - fromDate.year) * 12
    } else if (currentDate.month.value < fromDate.month.value && currentDate.year >= fromDate.year) {
        (currentDate.year - fromDate.year) * 12 - (fromDate.month.value - currentDate.month.value)
    } else if (currentDate.month.value < fromDate.month.value && currentDate.year < fromDate.year) {
        (fromDate.year - currentDate.year) * 12 - (currentDate.month.value - fromDate.month.value)
    } else {
        (fromDate.month.value - currentDate.month.value) + (fromDate.year - currentDate.year) * 12
    }
}
