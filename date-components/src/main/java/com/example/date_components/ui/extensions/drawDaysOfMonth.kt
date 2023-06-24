package com.example.date_components.ui.extensions


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import java.time.DayOfWeek

@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.drawDaysOfMonth(
    containsSelectedDate: Boolean,
    dayOfMonth: Int,
    daysInMonth: Int,
    firstDayOfWeekOfMonth: DayOfWeek,
    width: Float,
    offset: Float,
    textMeasurer: TextMeasurer,
    activeColor: Color,
    selectedColor: Color,
    textStyle: TextStyle = TextStyle.Default.copy(
        color = activeColor,
        fontWeight = FontWeight.SemiBold
    )
) {

    val dateOffset = firstDayOfWeekOfMonth.ordinal

    var row: Byte = 1;
    for (i in 1..daysInMonth) {
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
            style = textStyle
        )

        if ((i + dateOffset) % 7 == 0) row++
    }
}
