package com.example.canvasexperimentation.ui.homeScreen.extensions.drawScope

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
    titleTextStyle: TextStyle = TextStyle(
        color = activeColor,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
    ),
    labelTextStyle: TextStyle = TextStyle.Default.copy(fontWeight = FontWeight.SemiBold),
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
            style = if (selectedDate.month == date.month && selectedDate.year == date.year && selectedDate.dayOfWeek == day) labelTextStyle.copy(
                color = activeColor
            )
            else labelTextStyle.copy(color = inactiveColor)
        )
    }
    val text = "${date.month.name} ${date.year}"
    val textSize = textMeasurer.measure(AnnotatedString(text)).size
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = Offset(startingOffset.toFloat(), width / 2 - textSize.height / 2),
        style = titleTextStyle
    )
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawTitleAndLabels(
    containsSelectedDate: Boolean,
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
            style = if (containsSelectedDate && activeDay.value == day.value) TextStyle.Default.copy(
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
