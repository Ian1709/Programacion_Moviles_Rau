package com.rau.tecsupfit.model

/**
 * Data class que representa una clase de gimnasio disponible en TECSUP Fit.
 */
data class FitnessClass(
    val id: String,
    val title: String,
    val instructor: String,
    val description: String,
    val category: String,
    val isToday: Boolean = true,
    val schedules: List<String>,
    val availableSpots: Int,
    val totalSpots: Int,
    val room: String,
    val iconName: String = "fitness"
)

/**
 * Estado de una reserva realizada por el usuario.
 */
enum class ReservationStatus(val label: String) {
    CONFIRMED("Confirmada"),
    COMPLETED("Completada")
}

/**
 * Data class que representa una reserva realizada por el usuario.
 */
data class Reservation(
    val id: String,
    val fitnessClass: FitnessClass,
    val selectedSchedule: String,
    val date: String,
    val status: ReservationStatus = ReservationStatus.CONFIRMED
)

/**
 * Objeto Mock con datos de prueba iniciales para la aplicación TECSUP Fit.
 */
object MockFitData {
    val sampleClasses = listOf(
        FitnessClass(
            id = "1",
            title = "Spinning Intenso",
            instructor = "Carlos Mendoza",
            description = "Entrenamiento cardiovascular de alta intensidad en bicicleta estática para mejorar la resistencia y quemar calorías.",
            category = "Cardio",
            isToday = true,
            schedules = listOf("07:00 AM - 08:00 AM", "06:00 PM - 07:00 PM"),
            availableSpots = 5,
            totalSpots = 20,
            room = "Sala Spinning",
            iconName = "directions_bike"
        ),
        FitnessClass(
            id = "2",
            title = "Power Crossfit",
            instructor = "Valeria Ramos",
            description = "Rutina funcional de fuerza y acondicionamiento físico con pesas, kettlebells y ejercicios con peso corporal.",
            category = "Fuerza",
            isToday = true,
            schedules = listOf("08:30 AM - 09:30 AM", "07:15 PM - 08:15 PM"),
            availableSpots = 3,
            totalSpots = 15,
            room = "Zona Funcional",
            iconName = "fitness_center"
        ),
        FitnessClass(
            id = "3",
            title = "Yoga & Mindfulness",
            instructor = "Ana María Torres",
            description = "Sesión de estiramiento, flexibilidad y relajación para reducir el estrés y mejorar la postura corporal.",
            category = "Bienestar",
            isToday = false,
            schedules = listOf("09:00 AM - 10:00 AM", "05:00 PM - 06:00 PM"),
            availableSpots = 8,
            totalSpots = 12,
            room = "Sala A (Menta)",
            iconName = "self_improvement"
        ),
        FitnessClass(
            id = "4",
            title = "Pilates Core",
            instructor = "Lucía Fernández",
            description = "Ejercicios enfocados en fortalecer el abdomen, la zona lumbar y mejorar el equilibrio y el control corporal.",
            category = "Flexibilidad",
            isToday = false,
            schedules = listOf("10:00 AM - 11:00 AM", "04:00 PM - 05:00 PM"),
            availableSpots = 2,
            totalSpots = 10,
            room = "Sala B",
            iconName = "accessibility_new"
        ),
        FitnessClass(
            id = "5",
            title = "Boxeo Recreativo",
            instructor = "Diego Paredes",
            description = "Aprende técnicas básicas de boxeo mientras trabajas la coordinación, agilidad y quema de grasa.",
            category = "Combate",
            isToday = true,
            schedules = listOf("06:00 AM - 07:00 AM", "08:00 PM - 09:00 PM"),
            availableSpots = 6,
            totalSpots = 16,
            room = "Ring & Combate",
            iconName = "sports_mma"
        )
    )

    val sampleReservations = mutableListOf(
        Reservation(
            id = "RES-101",
            fitnessClass = sampleClasses[0],
            selectedSchedule = "07:00 AM - 08:00 AM",
            date = "Hoy, 07:00 AM",
            status = ReservationStatus.CONFIRMED
        ),
        Reservation(
            id = "RES-100",
            fitnessClass = sampleClasses[2],
            selectedSchedule = "05:00 PM - 06:00 PM",
            date = "Ayer, 05:00 PM",
            status = ReservationStatus.COMPLETED
        )
    )
}
