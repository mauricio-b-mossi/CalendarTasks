package com.example.canvasexperimentation.ui.homeScreen.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.example.canvasexperimentation.ui.homeScreen.extensions.drawDaysOfMonth
import com.example.canvasexperimentation.ui.homeScreen.extensions.drawTitleAndLabels
import com.example.canvasexperimentation.utils.getDaysInMonth
import com.example.canvasexperimentation.utils.getFirstDayOfWeekOfMonth
import java.time.LocalDate
import kotlin.math.ceil

@OptIn(ExperimentalTextApi::class)
@Composable
fun Month(
    selectedDate: LocalDate,
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    activeColor: Color = com.example.canvasexperimentation.ui.theme.activeColor,
    inactiveColor: Color = com.example.canvasexperimentation.ui.theme.inactiveColor,
    selectedColor: Color = com.example.canvasexperimentation.ui.theme.selectedColor,
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
                    Log.d("Event", "Row: $row, Col: $col")
                    val dayOfMonth = getDateFromPosition(row, col, date)
                    Log.d("Event", dayOfMonth.toString())
                    if (dayOfMonth != 0) {
                        Log.d("Event", "Sending")
                        onDateSelect(LocalDate.of(date.year, date.month, dayOfMonth))
                    }
                }
            }
        }) {
            Canvas(
                modifier.fillMaxSize()
            ) {
                drawTitleAndLabels(
                    selectedDate =selectedDate,
                    month = date.month,
                    year = date.year,
                    textMeasurer = textMeasurer,
                    width = width,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    activeDay = date.dayOfWeek
                )
                drawDaysOfMonth(
                    selectedDate = selectedDate,
                    month = date.month,
                    dayOfMonth = date.dayOfMonth,
                    isLeapYear = date.isLeapYear,
                    firstDayOfWeekOfMonth = date.getFirstDayOfWeekOfMonth(),
                    width = width,
                    offset = offset,
                    textMeasurer = textMeasurer,
                    activeColor = com.example.canvasexperimentation.ui.theme.activeColor,
                    selectedColor = selectedColor
                )
            }
        }
    }
}


fun getDateFromPosition(row: Int, col: Int, date: LocalDate): Int {
    val dateOffset = date.getFirstDayOfWeekOfMonth()
    val daysInMonth = date.getDaysInMonth()
    val dayOfMonth = (row - 1) * 7 + col - dateOffset.ordinal
    return if (dayOfMonth in 1..daysInMonth) dayOfMonth else 0
}

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
fun MonthPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Month(selectedDate = LocalDate.now(), date = LocalDate.now().plusMonths(0), onDateSelect = { Unit })
    }
}