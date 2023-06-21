package com.example.canvasexperimentation.ui.homeScreen.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import com.example.canvasexperimentation.ui.theme.calendarBackgroundColor
import java.time.LocalDate

@OptIn(ExperimentalTextApi::class)
@Composable
fun CalendarRow(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    items: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val maxWidth = maxWidth
        LazyRow(
            Modifier
                .fillMaxSize()
                .background(calendarBackgroundColor),
        ) {
            items(items) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Month(
                        date = date,
                        onDateSelect = { localDate ->
                            onDateChange(localDate)
                        },
                        month = date.month.plus(it.toLong()),
                        year = date.year.plus((it + date.month.value) / 12),
                        modifier = Modifier.width(maxWidth - 32.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun CalendarColumn(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    items: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val maxWidth = maxWidth
        LazyRow(
            Modifier
                .fillMaxSize()
                .background(calendarBackgroundColor),
        ) {
            items(items) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Month(
                        date = date,
                        onDateSelect = { localDate ->
                            onDateChange(localDate)
                        },
                        month = date.month,
                        year = date.year,
                        modifier = Modifier.width(maxWidth - 32.dp)
                    )
                }
            }
        }
    }
}

//TODO Implement vertical grid
@Composable
fun CalendarVerticalGrid() {

}