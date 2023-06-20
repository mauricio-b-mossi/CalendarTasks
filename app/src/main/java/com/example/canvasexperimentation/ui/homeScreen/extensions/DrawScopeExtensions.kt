package com.example.canvasexperimentation.ui.homeScreen.extensions

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.canvasexperimentation.utils.getDaysInMonth
import com.example.canvasexperimentation.utils.getFirstDayOfWeekOfMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawTitleAndLabels(
    selectedDate: LocalDate,
    date: LocalDate,
    textMeasurer: TextMeasurer,
    width: Float,
    activeColor: Color,
    inactiveColor: Color,
) {
    var startingOffset = 0
    for (day in DayOfWeek.values()) {
        val text: String = day.name.slice(0 until 3)
        val textSize = textMeasurer.measure(AnnotatedString(text)).size
        if (day.value == 1) startingOffset = textSize.width / 2

        drawText(
            textMeasurer,
            text,
            Offset(
                width * day.value - width / 2 - textSize.width / 2,
                width / 2 - textSize.height / 2 + width
            ),
            style = if (selectedDate.month == date.month && selectedDate.year == date.year && selectedDate.dayOfWeek == day) TextStyle.Default.copy(
                color = activeColor,
                fontWeight = FontWeight.SemiBold
            ) else TextStyle.Default.copy(
                color = inactiveColor,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
    val text = "${date.month.name} ${date.year}"
    val textSize = textMeasurer.measure(AnnotatedString(text)).size
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = Offset(startingOffset.toFloat(), width / 2 - textSize.height / 2),
        style = TextStyle(
            color = activeColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    )
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawTitleAndLabels(
    month: Month,
    year: Int,
    textMeasurer: TextMeasurer,
    width: Float,
    activeColor: Color,
    inactiveColor: Color,
    activeDay: DayOfWeek,
) {
    var startingOffset = 0
    for (day in DayOfWeek.values()) {
        val text: String = day.name.slice(0 until 3)
        val textSize = textMeasurer.measure(AnnotatedString(text)).size
        if (day.value == 1) startingOffset = textSize.width / 2

        drawText(
            textMeasurer,
            text,
            Offset(
                width * day.value - width / 2 - textSize.width / 2,
                width / 2 - textSize.height / 2 + width
            ),
            style = if (activeDay.value == day.value) TextStyle.Default.copy(
                color = activeColor,
                fontWeight = FontWeight.SemiBold
            ) else TextStyle.Default.copy(
                color = inactiveColor,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
    val text = "${month.name} $year"
    val textSize = textMeasurer.measure(AnnotatedString(text)).size
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = Offset(startingOffset.toFloat(), width / 2 - textSize.height / 2),
        style = TextStyle(
            color = activeColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    )
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawTitle(
    month: Month,
    year: Int,
    textMeasurer: TextMeasurer,
    width: Float,
    color: Color
) {
    val text = "${month.name} $year"
    val textSize = textMeasurer.measure(AnnotatedString(text)).size
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = Offset(20f, width / 2 - textSize.height / 2),
        style = TextStyle(
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    )
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawDaysOfWeek(
    textMeasurer: TextMeasurer,
    width: Float,
    offset: Float,
    activeColor: Color,
    inactiveColor: Color,
    activeDay: DayOfWeek,
) {
    for (day in DayOfWeek.values()) {
        val text: String = day.name.slice(0 until 3)
        val textSize = textMeasurer.measure(AnnotatedString(text)).size

        // Use value (1-7) because we draw text in the center.
        drawText(
            textMeasurer,
            text,
            Offset(
                width * day.value - width / 2 - textSize.width / 2,
                width / 2 - textSize.height / 2 + offset
            ),
            style = if (activeDay.value == day.value) TextStyle.Default.copy(color = activeColor) else TextStyle.Default.copy(
                color = inactiveColor
            )
        )
    }
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawDaysOfMonth(
    selectedDate: LocalDate,
    date: LocalDate,
    width: Float,
    offset: Float,
    textMeasurer: TextMeasurer,
    activeColor: Color,
    selectedColor: Color,
) {
    val dateOffset = date.getFirstDayOfWeekOfMonth().ordinal


    var row: Byte = 1;
    for (i in 1..date.month.getDaysInMonth(date.isLeapYear)) {
        val text = i.toString()
        val textSize = textMeasurer.measure(AnnotatedString(text)).size
        if (i == selectedDate.dayOfMonth && selectedDate.month == date.month && selectedDate.year == date.year) {
            val col =
                if ((selectedDate.dayOfMonth + dateOffset) % 7 == 0) 7 else ((((selectedDate.dayOfMonth + dateOffset) - 1) % 7) + 1)
            val row =
                if ((selectedDate.dayOfMonth + dateOffset) % 7 == 0) ((selectedDate.dayOfMonth + dateOffset) / 7) else ((selectedDate.dayOfMonth + dateOffset) / 7) + 1
            drawCircle(
                selectedColor,
                width / 3,
                Offset(col * width - width / 2, row * width + offset - width / 2)
            )
        }
        drawText(
            textMeasurer,
            text,
            Offset(
                width * (if ((i + dateOffset) % 7 == 0) 7 else (i + dateOffset) % 7) - width / 2 - textSize.width / 2,
                row * width + offset - width / 2 - textSize.height / 2
            ),
            style = TextStyle.Default.copy(
                color = activeColor,
                fontWeight = FontWeight.SemiBold
            )
        )

        if ((i + dateOffset) % 7 == 0) row++
    }
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawDaysOfMonth(
    containsSelectedDate : Boolean,
    dayOfMonth: Int,
    firstDayOfWeekOfMonth: DayOfWeek,
    width: Float,
    offset: Float,
    textMeasurer: TextMeasurer,
    activeColor: Color,
    selectedColor: Color,
) {
    val dateOffset = firstDayOfWeekOfMonth.ordinal

    var row: Byte = 1;
    for (i in 1..dayOfMonth) {
        val text = i.toString()
        val textSize = textMeasurer.measure(AnnotatedString(text)).size
        if (containsSelectedDate && i == dayOfMonth) {
            val col =
                if ((dayOfMonth + dateOffset) % 7 == 0) 7 else ((((dayOfMonth + dateOffset) - 1) % 7) + 1)
            val row =
                if ((dayOfMonth + dateOffset) % 7 == 0) ((dayOfMonth + dateOffset) / 7) else ((dayOfMonth + dateOffset) / 7) + 1
            drawCircle(
                selectedColor,
                width / 3,
                Offset(col * width - width / 2, row * width + offset - width / 2)
            )
        }
        drawText(
            textMeasurer,
            text,
            Offset(
                width * (if ((i + dateOffset) % 7 == 0) 7 else (i + dateOffset) % 7) - width / 2 - textSize.width / 2,
                row * width + offset - width / 2 - textSize.height / 2
            ),
            style = TextStyle.Default.copy(
                color = activeColor,
                fontWeight = FontWeight.SemiBold
            )
        )

        if ((i + dateOffset) % 7 == 0) row++
    }
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawDaysOfMonth(
    date: LocalDate,
    width: Float,
    offset: Float,
    textMeasurer: TextMeasurer,
    isDaySelected: Boolean = false
) {

    var row: Byte = 1;
    for (i in 1..date.getDaysInMonth()) {
        if (isDaySelected && date.dayOfMonth == i) {
            val col = ((date.dayOfMonth - 1) % 7) + 1
            val row = (date.dayOfMonth / 7) + 1
            drawCircle(
                Color.Green,
                width / 3,
                Offset(col * width - width / 2, row * width + offset - width / 2)
            )
        }
        val text = i.toString()
        val textSize = textMeasurer.measure(AnnotatedString(text)).size
        drawText(
            textMeasurer,
            text,
            Offset(
                width * (if (i % 7 == 0) 7 else i % 7) - width / 2 - textSize.width / 2,
                row * width + offset - width / 2 - textSize.height / 2
            )
        )
        if (i % 7 == 0) row++
    }
}
