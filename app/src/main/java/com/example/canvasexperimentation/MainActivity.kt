package com.example.canvasexperimentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
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
import com.example.date_components.ui.components.Calendar
import com.example.date_components.ui.components.CalendarOrientation
import com.example.date_components.ui.components.CalendarRow
import com.example.date_components.ui.components.rememberCalendarState
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

            var date by remember {
                mutableStateOf(LocalDate.now())
            }

            // Create rememberCalendar
            val calendarState =
                rememberCalendarState(date = LocalDate.now(), range = 3, CalendarOrientation.ROW)

            val textMeasurer = rememberTextMeasurer()

            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(calendarBackgroundColor)
                    }
            ) {
                Calendar(
                    calendarState = calendarState,
                    date = date,
                    onDateChange = { date = it },
                    textMeasurer = textMeasurer,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    selectedColor = selectedColor,
                    itemPadding = 35.dp,
                    //modifier = Modifier.fillMaxHeight(.5f)
                )

                BottomSheet(
                    bottomSheetState = bottomSheetState,
                    onDefault = {calendarState.calendarOrientation = CalendarOrientation.ROW},
                    onExpanded = { Log.d("Event", "OnExpanded") },
                    onCollapsed = {calendarState.calendarOrientation = CalendarOrientation.COLUMN}
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
