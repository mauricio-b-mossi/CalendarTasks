package com.example.canvasexperimentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.canvasexperimentation.ui.homeScreen.components.Calendar
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
            var date by remember {
                mutableStateOf(LocalDate.now())
            }

            LazyColumn() {
                items(12) {
                    BoxWithConstraints {
                        val padding = 16.dp
                        val size = maxWidth - padding
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(calendarBackgroundColor)
                                .padding(padding)
                        ) {
                            Month(
                                date = date,
                                onDateSelect = { localDate -> date = localDate },
                                textMeasurer = textMeasurer,
                                modifier = Modifier.size(size, size)
                            )
                        }
                    }
                }
            }
        }
    }
}
