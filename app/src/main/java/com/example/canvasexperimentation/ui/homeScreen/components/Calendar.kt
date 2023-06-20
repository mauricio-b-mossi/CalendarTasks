package com.example.canvasexperimentation.ui.homeScreen.components

import android.util.Log
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.canvasexperimentation.ui.theme.activeColor
import com.example.canvasexperimentation.ui.theme.inactiveColor
import com.example.canvasexperimentation.ui.theme.selectedColor
import java.time.LocalDate


@OptIn(ExperimentalTextApi::class)
@Composable
fun Calendar(
    selectedDate: LocalDate,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    offset: () -> Float,
    onOffsetChange: (Float) -> Unit,
    textMeasurer: TextMeasurer = rememberTextMeasurer()
) {
    BoxWithConstraints(modifier = Modifier.pointerInput(true) {
        detectHorizontalDragGestures { _, dragAmount ->
            onOffsetChange(dragAmount)
        }
    }) {
        val maxWidth = maxWidth
        Row(
            Modifier
                .requiredWidth(maxWidth * 3)
                .graphicsLayer {
                    translationX = offset()
                }) {
            Log.d("Recomp", "Drawing the three comps")
            repeat(3) { position ->
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Month(
                        selectedDate = selectedDate,
                        date = date.minusMonths((1 - position).toLong())
                            .also { Log.d("Recomp", "Item number $position ; Date $it") },
                        onDateSelect = {
                            onDateChange(it)
                        },
                        modifier = Modifier.widthIn(max = maxWidth - 32.dp),
                        activeColor = activeColor,
                        inactiveColor = inactiveColor,
                        selectedColor = selectedColor,
                        textMeasurer = textMeasurer
                    )
                }
            }
        }
    }
}

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

    BoxWithConstraints(Modifier.pointerInput(true) {
        detectHorizontalDragGestures { _, dragAmount ->
            offset += dragAmount
        }
    }) {
        CalendarPagination(
            date = date,
            onDateSelect = { onDateSelect(it) },
            calendarWidth = maxWidth,
            textMeasurer = textMeasurer,
            modifier = modifier

        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun CalendarPagination(
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    calendarWidth: Dp,
    textMeasurer: TextMeasurer,
    modifier: Modifier = Modifier
) {
    Row(Modifier.requiredWidth(calendarWidth * 3)) {
        repeat(3) {
            Month(
                date = date,
                onDateSelect = { onDateSelect(it) },
                textMeasurer = textMeasurer,
                modifier = modifier
            )
        }
    }
}

