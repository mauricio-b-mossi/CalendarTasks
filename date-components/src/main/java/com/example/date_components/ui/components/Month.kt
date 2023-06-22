package com.example.date_components.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import com.example.date_components.ui.extensions.drawDaysOfMonth
import com.example.date_components.ui.extensions.drawTitleAndLabels
import com.example.date_components.utils.extensions.getDaysInMonth
import com.example.date_components.utils.extensions.getFirstDayOfWeekOfMonth
import com.example.date_components.utils.extensions.withIn
import java.time.LocalDate
import java.time.Month
import kotlin.math.ceil

@OptIn(ExperimentalTextApi::class)
@Composable
fun Month(
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    month: Month,
    year: Int,
    activeColor: Color,
    inactiveColor: Color,
    selectedColor: Color,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    BoxWithConstraints(modifier) {

        val width = with(density) { (maxWidth / 7).toPx() }
        val offset = 2 * width
        val maxWidth = maxWidth
        val rows =
            (month.getDaysInMonth(year) + month.getFirstDayOfWeekOfMonth(year).value) / 2 + (offset / width)

        // Extract Box to other side
        Box(Modifier.pointerInput(true) {
            detectTapGestures { touch ->
                if (touch.y > offset) {
                    val row = ceil(touch.y / width - 2).toInt()
                    val col = ceil(touch.x / width).toInt()
                    val dayOfMonth = getDateFromPosition(row, col, month, year)
                    if (dayOfMonth != 0) {
                        onDateSelect(LocalDate.of(year, month, dayOfMonth))
                    }
                }
            }
        }) {
            Canvas(
                Modifier
                    .width(maxWidth)
                    .height(maxWidth / 7 * rows)
            ) {
                drawTitleAndLabels(
                    containsSelectedDate = date.withIn(month, year),
                    month = month,
                    year = year,
                    textMeasurer = textMeasurer,
                    width = width,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    activeDay = date.dayOfWeek
                )
                drawDaysOfMonth(
                    containsSelectedDate = date.withIn(month, year),
                    dayOfMonth = date.dayOfMonth,
                    daysInMonth = month.getDaysInMonth(year),
                    firstDayOfWeekOfMonth = month.getFirstDayOfWeekOfMonth(year),
                    width = width,
                    offset = offset,
                    textMeasurer = textMeasurer,
                    activeColor = activeColor,
                    selectedColor = selectedColor
                )
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun Month(
    selectedDate: LocalDate,
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    selectedColor: Color,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    BoxWithConstraints(modifier) {

        val width = with(density) { (maxWidth / 7).toPx() }
        val offset = 2 * width

        Box(Modifier.pointerInput(true) {
            detectTapGestures { touch ->
                if (touch.y > offset) {
                    val row = ceil(touch.y / width - 2).toInt()
                    val col = ceil(touch.x / width).toInt()
                    val dayOfMonth = getDateFromPosition(row, col, date)
                    if (dayOfMonth != 0) {
                        onDateSelect(LocalDate.of(date.year, date.month, dayOfMonth))
                    }
                }
            }
        }) {
            Canvas(
                modifier.fillMaxSize()
            ) {
                drawTitleAndLabels(
                    selectedDate = selectedDate,
                    date = date,
                    textMeasurer = textMeasurer,
                    width = width,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                )
                drawDaysOfMonth(
                    selectedDate = selectedDate,
                    date = date,
                    width = width,
                    offset = offset,
                    textMeasurer = textMeasurer,
                    activeColor = activeColor,
                    selectedColor = selectedColor
                )
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun Month(
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    selectedColor: Color,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    BoxWithConstraints(modifier) {

        val width = with(density) { (maxWidth / 7).toPx() }
        val offset = 2 * width

        Box(Modifier.pointerInput(true) {
            detectTapGestures { touch ->
                if (touch.y > offset) {
                    val row = ceil(touch.y / width - 2).toInt()
                    val col = ceil(touch.x / width).toInt()
                    val dayOfMonth = getDateFromPosition(row, col, date)
                    if (dayOfMonth != 0) {
                        onDateSelect(LocalDate.of(date.year, date.month, dayOfMonth))
                    }
                }
            }
        }) {
            Canvas(
                modifier.fillMaxSize()
            ) {
                drawTitleAndLabels(
                    month = date.month,
                    year = date.year,
                    textMeasurer = textMeasurer,
                    width = width,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    activeDay = date.dayOfWeek
                )
                drawDaysOfMonth(
                    month = date.month,
                    dayOfMonth = date.dayOfMonth,
                    isLeapYear = date.isLeapYear,
                    firstDayOfWeekOfMonth = date.getFirstDayOfWeekOfMonth(),
                    width = width,
                    offset = offset,
                    textMeasurer = textMeasurer,
                    activeColor = activeColor,
                    selectedColor = selectedColor
                )
            }
        }
    }
}


private fun getDateFromPosition(row: Int, col: Int, date: LocalDate): Int {
    val dateOffset = date.getFirstDayOfWeekOfMonth()
    val daysInMonth = date.getDaysInMonth()
    val dayOfMonth = (row - 1) * 7 + col - dateOffset.ordinal
    return if (dayOfMonth in 1..daysInMonth) dayOfMonth else 0
}

private fun getDateFromPosition(row: Int, col: Int, month: Month, year: Int): Int {
    val date = LocalDate.of(year, month, 1)
    val dateOffset = date.dayOfWeek
    val daysInMonth = date.getDaysInMonth()
    val dayOfMonth = (row - 1) * 7 + col - dateOffset.ordinal
    return if (dayOfMonth in 1..daysInMonth) dayOfMonth else 0
}
