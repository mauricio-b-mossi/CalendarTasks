package com.example.canvasexperimentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
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
import com.example.canvasexperimentation.ui.homeScreen.components.Month
import com.example.canvasexperimentation.ui.homeScreen.components.rememberBottomSheetState
import com.example.canvasexperimentation.ui.theme.calendarBackgroundColor
import com.example.canvasexperimentation.ui.theme.selectedColor
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit

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

            var bottomSheetState = rememberBottomSheetState()

            Box(
                Modifier
                    .fillMaxSize()
                    .background(calendarBackgroundColor),
            ) {
                CalendarSecondRow(
                    dateRange = calendarRange,
                    date = date,
                    onDateChange = { date = it },
                    state = lazyListState,
                    textMeasurer = textMeasurer,
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


@OptIn(ExperimentalTextApi::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarSecondRow(
    dateRange: List<LocalDate>,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    state: LazyListState = rememberLazyListState(),
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val maxWidth = maxWidth
        LazyRow(
            state = state,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
        ) {
            items(dateRange) { localDate ->
                Column(modifier = Modifier.padding(horizontal = 25.dp)) {
                    Month(
                        date = date,
                        onDateSelect = { localDate ->
                            onDateChange(localDate)
                        },
                        month = localDate.month,
                        year = localDate.year,
                        textMeasurer = textMeasurer,
                        modifier = Modifier.width(maxWidth - 50.dp)
                    )
                }
            }
        }
    }

}


fun generateCalendarRange(date: LocalDate = LocalDate.now(), range: Int): List<LocalDate> {
    val start = LocalDate.of(date.year - range, Month.JANUARY, 1)
    val end = LocalDate.of(date.year + range, Month.DECEMBER, 1)
    val months = start.until(end, ChronoUnit.MONTHS)
    return buildList {
        for (i in 0..months) {
            add(LocalDate.of(start.year.plus(i / 12).toInt(), ((i % 12) + 1).toInt(), 1))
        }
    }
}

fun getMonthIndex(currentDate: LocalDate = LocalDate.now(), fromDate: LocalDate): Int {
    return if (currentDate.month.value >= fromDate.month.value && currentDate.year >= fromDate.year) {
        (currentDate.month.value - fromDate.month.value) + (currentDate.year - fromDate.year) * 12
    } else if (currentDate.month.value < fromDate.month.value && currentDate.year >= fromDate.year) {
        (currentDate.year - fromDate.year) * 12 - (fromDate.month.value - currentDate.month.value)
    } else if (currentDate.month.value < fromDate.month.value && currentDate.year < fromDate.year) {
        (fromDate.year - currentDate.year) * 12 - (currentDate.month.value - fromDate.month.value)
    } else {
        (fromDate.month.value - currentDate.month.value) + (fromDate.year - currentDate.year) * 12
    }
}
