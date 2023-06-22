package com.example.date_components.ui.components


import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.Dp
import java.time.LocalDate

@OptIn(ExperimentalTextApi::class)
@Composable
fun CalendarRow(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    items: Int,
    itemPadding: Dp,
    activeColor: Color,
    inactiveColor: Color,
    selectedColor: Color,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val maxWidth = maxWidth
        LazyRow(
            Modifier.fillMaxSize()
        ) {
            items(items) {
                Column(modifier = Modifier.padding(itemPadding)) {
                    Month(
                        date = date,
                        onDateSelect = { localDate ->
                            onDateChange(localDate)
                        },
                        activeColor = activeColor,
                        inactiveColor = inactiveColor,
                        selectedColor = selectedColor,
                        month = date.month.plus(it.toLong()),
                        year = date.year.plus((it + date.month.value) / 12),
                        modifier = Modifier.width(maxWidth - itemPadding * 2)
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
    itemPadding: Dp,
    activeColor: Color,
    inactiveColor: Color,
    selectedColor: Color,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val maxWidth = maxWidth
        LazyRow(
            Modifier
                .fillMaxSize()
        ) {
            items(items) {
                Column(modifier = Modifier.padding(itemPadding)) {
                    Month(
                        date = date,
                        onDateSelect = { localDate ->
                            onDateChange(localDate)
                        },
                        activeColor = activeColor,
                        inactiveColor = inactiveColor,
                        selectedColor = selectedColor,
                        month = date.month,
                        year = date.year,
                        modifier = Modifier.width(maxWidth - itemPadding * 2)
                    )
                }
            }
        }
    }
}

//TODO Implement vertical grid