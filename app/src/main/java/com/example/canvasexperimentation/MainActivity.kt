package com.example.canvasexperimentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.canvasexperimentation.ui.homeScreen.HomeScreen
import com.example.canvasexperimentation.ui.homeScreen.components.Calendar
import com.example.canvasexperimentation.ui.homeScreen.components.Month
import com.example.canvasexperimentation.ui.theme.activeDate
import com.example.canvasexperimentation.ui.theme.calendarBackground
import com.example.canvasexperimentation.ui.theme.inactiveDate
import com.example.canvasexperimentation.ui.theme.selectedDate
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTextApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val textMeasurer = rememberTextMeasurer()
            var localDate by remember {
                mutableStateOf(LocalDate.now())
            }
            var offset by remember {
                mutableStateOf(0f)
            }
            Column(
                Modifier
                    .background(calendarBackground)
            ) {
                BoxWithConstraints(modifier = Modifier.pointerInput(true) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        offset += dragAmount
                    }
                }) {
                    val maxWidth = maxWidth
                    Row(
                        Modifier
                            .requiredWidth(maxWidth * 3)
                            .graphicsLayer {
                                translationX = offset
                            }) {
                        Month(
                            date = localDate.minusMonths(1),
                            onDateSelect = { localDate = it },
                            modifier = Modifier.widthIn(max = maxWidth)
                        )
                        Month(
                            date = localDate,
                            onDateSelect = { localDate = it },
                            modifier = Modifier.widthIn(max = maxWidth)
                        )
                        Month(
                            date = localDate.plusMonths(1),
                            onDateSelect = { localDate = it },
                            modifier = Modifier.widthIn(max = maxWidth)
                        )
                    }
                }
            }
        }
    }
}