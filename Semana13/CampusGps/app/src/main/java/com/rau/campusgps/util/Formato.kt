package com.rau.campusgps.util

import java.util.Locale
import kotlin.math.roundToInt

fun formatearDistancia(metros: Float): String =
    if (metros < 1000f) {
        "${metros.roundToInt()} m"
    } else {
        String.format(Locale.getDefault(), "%.1f km", metros / 1000f)
    }