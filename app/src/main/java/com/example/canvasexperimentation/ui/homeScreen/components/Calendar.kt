package com.example.canvasexperimentation.ui.homeScreen.components

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.Dp
import java.time.LocalDate

@OptIn(ExperimentalTextApi::class)
@Composable
fun Calendar(
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    textMeasurer: TextMeasurer,
    modifier: Modifier = Modifier
) {

    var offset by remember {
        mutableStateOf(0f)
    }

    val density = LocalDensity.current

    BoxWithConstraints(Modifier.pointerInput(true) {
        detectHorizontalDragGestures { _, dragAmount ->
            offset += dragAmount
        }
    }) {
        CalendarPagination(
            date = date,
            onDateSelect = { onDateSelect(it) },
            calendarWidth = maxWidth
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun CalendarPagination(date: LocalDate, onDateSelect: (LocalDate) -> Unit, calendarWidth: Dp) {
    Row(Modifier.requiredWidth(calendarWidth * 3)) {
        repeat(3) {
            Month(date = date, onDateSelect = { onDateSelect(it) })
        }
    }
}

