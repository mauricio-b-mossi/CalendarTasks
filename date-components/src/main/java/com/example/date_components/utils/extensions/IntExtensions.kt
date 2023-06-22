package com.example.date_components.utils.extensions

fun Int.isLeapYear(): Boolean {
    return if (this % 4 == 0 && this % 400 == 0) {
        true
    } else if (this % 100 == 0) {
        false
    } else {
        this % 4 == 0
    }
}