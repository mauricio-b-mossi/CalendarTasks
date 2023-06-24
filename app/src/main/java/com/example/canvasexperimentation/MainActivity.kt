package com.example.canvasexperimentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bottomsheet_component.ui.BottomSheet
import com.example.bottomsheet_component.ui.BottomSheetStage
import com.example.bottomsheet_component.ui.BottomSheetStagePercentage
import com.example.bottomsheet_component.ui.rememberBottomSheetState
import com.example.canvasexperimentation.data.Task
import com.example.canvasexperimentation.ui.theme.activeColor
import com.example.canvasexperimentation.ui.theme.calendarBackgroundColor
import com.example.canvasexperimentation.ui.theme.inactiveColor
import com.example.canvasexperimentation.ui.theme.selectedColor
import com.example.date_components.ui.components.CalendarRow
import com.example.date_components.ui.components.generateCalendarRange
import com.example.date_components.ui.components.getMonthIndex
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTextApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val bottomSheetState = rememberBottomSheetState(
                startStage = BottomSheetStage.DEFAULT,
                bottomSheetStagePercentage = BottomSheetStagePercentage(
                    DEFAULT = 0.5F,
                    EXPANDED = 1F,
                    COLLAPSED = 0.1F
                )
            )
            val textMeasurer = rememberTextMeasurer()
            var date by remember {
                mutableStateOf(LocalDate.now())
            }
            val calendarRange = remember {
                generateCalendarRange(LocalDate.now(), 3)
            }
            val lazyListState = rememberLazyListState(getMonthIndex(date, calendarRange.first()))

            val systemUiController = rememberSystemUiController()

            val isExpandedAndIsDragging by remember {
                derivedStateOf {
                    with(bottomSheetState){
                        stage == BottomSheetStage.EXPANDED && !isDragging && screenPercentage.value > (bottomSheetStagePercentage.DEFAULT + bottomSheetStagePercentage.EXPANDED) / 2
                    }
                }
            }


            LaunchedEffect(bottomSheetState.stage, bottomSheetState.isDragging) {
                if (isExpandedAndIsDragging) {
                    systemUiController.setStatusBarColor(Color.White)
                } else {
                    systemUiController.setStatusBarColor(calendarBackgroundColor)
                }
            }


            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(if (isExpandedAndIsDragging) Color.White else calendarBackgroundColor)
                    }
            ) {
                CalendarRow(
                    dateRange = calendarRange,
                    date = date,
                    onDateChange = { date = it },
                    state = lazyListState,
                    textMeasurer = textMeasurer,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    selectedColor = selectedColor,
                    itemPadding = 35.dp,
                    modifier = Modifier.fillMaxHeight(.5f)
                )

                BottomSheet(
                    bottomSheetState = bottomSheetState,
                    onDefault = { Log.d("Event", "onDefault") },
                    onExpanded = { Log.d("Event", "OnExpanded") },
                    onCollapsed = { Log.d("Event", "OnCollapsed") }
                ) {
                    TodoBottomSheet(date = date)
                }
            }
        }
    }
}


@Composable
fun TodoBottomSheet(
    date: LocalDate,
    tasks: List<Task> = emptyList(),
    onAddTask: (Task) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (date == LocalDate.now()) {
            Text(
                text = "TODAY", style = TextStyle(
                    color = selectedColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default,
                    letterSpacing = 0.5.sp
                )
            )
        } else {
            //Day of Week, Month dayOfMonth, year
            Text(
                text = "${date.dayOfWeek}, ${date.month} ${date.dayOfMonth}, ${date.year}",
                style = TextStyle(
                    color = selectedColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default,
                    letterSpacing = 0.5.sp
                )
            )
        }

        Spacer(Modifier.height(16.dp))


    }
}
