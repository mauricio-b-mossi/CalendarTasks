package com.example.canvasexperimentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canvasexperimentation.data.Status
import com.example.canvasexperimentation.data.Task
import com.example.canvasexperimentation.ui.homeScreen.components.BottomSheet
import com.example.canvasexperimentation.ui.homeScreen.components.BottomSheetStage
import com.example.canvasexperimentation.ui.homeScreen.components.rememberBottomSheetState
import com.example.canvasexperimentation.ui.theme.activeColor
import com.example.canvasexperimentation.ui.theme.calendarBackgroundColor
import com.example.canvasexperimentation.ui.theme.inactiveColor
import com.example.canvasexperimentation.ui.theme.selectedColor
import com.example.date_components.ui.components.CalendarRow
import com.example.date_components.ui.components.generateCalendarRange
import com.example.date_components.ui.components.getMonthIndex
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
            val calendarRange = remember {
                generateCalendarRange(LocalDate.now(), 3)
            }
            val lazyListState = rememberLazyListState(getMonthIndex(date, calendarRange.first()))

            val density = LocalDensity.current

            val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

            val dragSurfaceHeight = remember {
                with(density) {
                    screenHeightDp.toPx()
                }
            }

            val bottomSheetState = rememberBottomSheetState()

            Box(
                Modifier
                    .fillMaxSize()
                    .background(calendarBackgroundColor),
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
                    onDefault = { bottomSheetState.stage = BottomSheetStage.DEFAULT },
                    onCollapsed = { bottomSheetState.stage = BottomSheetStage.COLLAPSED },
                    onExpanded = { bottomSheetState.stage = BottomSheetStage.EXPANDED },
                    onDragStart = { bottomSheetState.isDragging = true },
                    onDragEnd = { bottomSheetState.isDragging = false },
                    dragSurfaceHeight = dragSurfaceHeight,
                ) {
                    TodoBottomSheet(
                        date = date,
                        tasks = listOf(
                            Task(
                                "Marketing meeting",
                                calendarBackgroundColor,
                                Status.DONE,
                            ),
                            Task(
                                "Client meeting",
                                Color.Red,
                                Status.IN_PROGRESS,
                            )
                        ),
                        onAddTask = {},
                        modifier = Modifier.padding(25.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun TodoBottomSheet(
    date: LocalDate,
    tasks: List<Task>,
    onAddTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        // Header:
        if (date == LocalDate.now()) {
            Text(
                text = "Today", style = TextStyle(
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

        //Tasks

    }
}
