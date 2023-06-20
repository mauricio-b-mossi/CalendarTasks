package com.example.canvasexperimentation.ui.homeScreen.extensions.drawScope


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import com.example.canvasexperimentation.utils.extensions.getDaysInMonth
import com.example.canvasexperimentation.utils.extensions.getFirstDayOfWeekOfMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

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
    month: Month,
    dayOfMonth: Int,
    isLeapYear: Boolean,
    firstDayOfWeekOfMonth: DayOfWeek,
    width: Float,
    offset: Float,
    textMeasurer: TextMeasurer,
    activeColor: Color,
    selectedColor: Color,
) {
    val dateOffset = firstDayOfWeekOfMonth.ordinal

    var row: Byte = 1;
    for (i in 1..month.getDaysInMonth(isLeapYear)) {
        val text = i.toString()
        val textSize = textMeasurer.measure(AnnotatedString(text)).size
        if (i == dayOfMonth) {
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
    containsSelectedDate: Boolean,
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
