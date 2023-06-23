package com.example.date_components.ui.extensions

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
import java.time.Month

@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.drawTitleAndLabels(
    containsSelectedDate: Boolean,
    month: Month,
    year: Int,
    textMeasurer: TextMeasurer,
    width: Float,
    activeColor: Color,
    inactiveColor: Color,
    activeDay: DayOfWeek,
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
            style = if (containsSelectedDate && activeDay.value == day.value) labelTextStyle.copy(
                color = activeColor,
            ) else labelTextStyle.copy(
                color = inactiveColor,
            )
        )
    }
    val text = "${month.name} $year"
    val textSize = textMeasurer.measure(AnnotatedString(text)).size
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = Offset(startingOffset.toFloat(), width / 2 - textSize.height / 2),
        style = titleTextStyle
    )
}