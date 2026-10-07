package com.saludplus.app.navigation

/**
 * Definición centralizada de rutas y constructores de argumentos de navegación para Clínica Salud+.
 */
sealed class Screen(val route: String) {

    /**
     * Pantalla principal con lista de médicos y filtros por especialidad.
     */
    object Home : Screen("home")

    /**
     * Pantalla de detalle del médico seleccionado.
     */
    object DoctorDetail : Screen("doctor_detail/{$DOCTOR_ID_ARG}") {
        fun createRoute(doctorId: Int): String = "doctor_detail/$doctorId"
    }

    /**
     * Pantalla para agendar cita (selección de fecha y hora).
     */
    object BookAppointment : Screen("book_appointment/{$DOCTOR_ID_ARG}") {
        fun createRoute(doctorId: Int): String = "book_appointment/$doctorId"
    }

    /**
     * Pantalla de confirmación con el resumen de la cita agendada.
     */
    object Confirmation : Screen("confirmation/{$APPOINTMENT_ID_ARG}") {
        fun createRoute(appointmentId: Int): String = "confirmation/$appointmentId"
    }

    /**
     * Pantalla del listado de citas del usuario (Confirmadas y Completadas).
     */
    object MyAppointments : Screen("my_appointments")

    /**
     * Pantalla con el historial médico del paciente.
     */
    object MedicalHistory : Screen("medical_history")

    companion object {
        const val DOCTOR_ID_ARG = "doctorId"
        const val APPOINTMENT_ID_ARG = "appointmentId"
    }
}
