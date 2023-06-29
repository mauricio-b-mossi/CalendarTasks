package com.example.date_components.ui.components


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    calendarState: CalendarState,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
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
            state = calendarState.lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = calendarState.lazyListState)
        ) {
            items(calendarState.dateRange) { localDate ->
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
                        modifier = Modifier
                            .width(maxWidth - itemPadding * 2)
                            .height(400.dp)
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalTextApi::class, ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    calendarState: CalendarState,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    activeColor: Color,
    inactiveColor: Color,
    selectedColor: Color,
    itemPadding: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val maxWidth = maxWidth

        if (calendarState.calendarOrientation == CalendarOrientation.ROW) {
            LazyRow(
                state = calendarState.lazyListState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = calendarState.lazyListState)
            ) {
                items(calendarState.dateRange) { localDate ->
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
                            modifier = Modifier
                                .width(maxWidth - itemPadding * 2)
                        )
                    }
                }
            }
        } else if (calendarState.calendarOrientation == CalendarOrientation.COLUMN) {
            LazyColumn(
                state = calendarState.lazyListState,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(calendarState.dateRange) { localDate ->
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
                            modifier = Modifier
                                .width(maxWidth - itemPadding * 2)
                        )
                    }
                }
            }
        }
    }

}

enum class CalendarOrientation {
    ROW,
    COLUMN,
    GRID,
}

@Composable
fun rememberCalendarState(
    date: LocalDate,
    range: Int,
    initialOrientation: CalendarOrientation = CalendarOrientation.ROW
): CalendarState {
    return remember {
        CalendarState(date = date, range = range, initialOrientation = initialOrientation)
    }
}

@Stable
class CalendarState(
    date: LocalDate,
    range: Int,
    initialOrientation: CalendarOrientation,
) {
    val dateRange = generateCalendarRange(date, range)
    val startingIndex = getMonthIndex(date, dateRange.first())
    val lazyListState = LazyListState(startingIndex)
    var calendarOrientation by mutableStateOf(initialOrientation)
}


/**
 * Generates a list of LocalDate objects representing a calendar range.
 *
 * @param date  the reference date from which the range is generated (default: current date)
 * @param range the number of years before and after the reference date to include in the range
 * @return a list of LocalDate objects representing the calendar range
 */
internal fun generateCalendarRange(date: LocalDate = LocalDate.now(), range: Int): List<LocalDate> {
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
internal fun getMonthIndex(currentDate: LocalDate = LocalDate.now(), fromDate: LocalDate): Int {
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
