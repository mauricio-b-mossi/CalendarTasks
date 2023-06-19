package com.example.canvasexperimentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.canvasexperimentation.ui.homeScreen.components.Month
import com.example.canvasexperimentation.ui.theme.activeColor
import com.example.canvasexperimentation.ui.theme.calendarBackgroundColor
import com.example.canvasexperimentation.ui.theme.inactiveColor
import com.example.canvasexperimentation.ui.theme.selectedColor
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTextApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val textMeasurer = rememberTextMeasurer()
            var selectedDate by remember {
                mutableStateOf(LocalDate.now())
            }
            var offset by remember {
                mutableStateOf(0f)
            }
            Column(
                Modifier.background(calendarBackgroundColor)
            ) {
                Calendar(
                    date = selectedDate,
                    onDateChange = {selectedDate = it},
                    offset = {offset},
                    onOffsetChange = {offset += it}
                )
            }
        }
    }
}
@OptIn(ExperimentalTextApi::class)
@Composable
fun Calendar(
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
            repeat(3) {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Month(
                        date = date.minusMonths((1 - it).toLong()),
                        onDateSelect = {onDateChange(it)},
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
