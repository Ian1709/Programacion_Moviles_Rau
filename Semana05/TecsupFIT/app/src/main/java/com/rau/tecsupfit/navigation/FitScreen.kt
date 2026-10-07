package com.rau.tecsupfit.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Sealed class que define la jerarquía de rutas y pantallas para la navegación en TECSUP Fit.
 */
sealed class FitScreen(val route: String) {
    object Home : FitScreen("home")
    object Reservations : FitScreen("reservations")
    object Routines : FitScreen("routines")
    object Profile : FitScreen("profile")

    object ClassDetail : FitScreen("class_detail/{classId}") {
        fun createRoute(classId: String): String {
            return "class_detail/$classId"
        }
    }

    object Confirmation : FitScreen("confirmation/{classId}/{schedule}") {
        fun createRoute(classId: String, schedule: String): String {
            val encodedSchedule = URLEncoder.encode(schedule, StandardCharsets.UTF_8.name())
            return "confirmation/$classId/$encodedSchedule"
        }
    }
}
