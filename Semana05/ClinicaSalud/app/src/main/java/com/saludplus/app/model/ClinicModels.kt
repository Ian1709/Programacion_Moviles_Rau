package com.saludplus.app.model

/**
 * Data class que representa la información de un médico/especialista en Clínica Salud+.
 */
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewsCount: Int,
    val biography: String,
    val avatarUrl: String,
    val consultationFee: Double = 60.0,
    val availableDates: List<String> = listOf("15 de Mayo", "16 de Mayo", "17 de Mayo"),
    val availableTimes: List<String> = listOf("09:00 AM", "11:30 AM", "03:00 PM")
)

/**
 * Data class que representa una cita médica agendada.
 */
data class Appointment(
    val id: Int,
    val doctorId: Int,
    val doctorName: String,
    val doctorSpecialty: String,
    val doctorAvatar: String,
    val patientName: String,
    val date: String,
    val time: String,
    val status: String, // "Confirmada", "Completada", "Cancelada"
    val reason: String = "Consulta General"
)

/**
 * Data class que representa un registro dentro del historial médico del paciente.
 */
data class MedicalHistoryItem(
    val id: Int,
    val date: String,
    val doctorName: String,
    val specialty: String,
    val diagnosis: String,
    val treatment: String,
    val prescription: String
)

/**
 * Objeto MockData con datos estáticos e iniciales para simular la persistencia en memoria.
 */
object MockData {

    val patientName: String = "Juan Pérez"
    val patientEmail: String = "juan.perez@saludplus.com"

    val filterCategories: List<String> = listOf(
        "Todos",
        "Cardiología",
        "Pediatría",
        "Dermatología",
        "Neurología",
        "Ginecología",
        "Medicina General"
    )

    val doctors: List<Doctor> = listOf(
        Doctor(
            id = 1,
            name = "Dr. Carlos Mendoza",
            specialty = "Cardiología",
            experienceYears = 12,
            rating = 4.9,
            reviewsCount = 142,
            biography = "Especialista en cardiología clínica e intervencionista con posgrado en cirugía cardiovascular. Experto en diagnóstico y tratamiento de enfermedades cardíacas.",
            avatarUrl = "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=400",
            consultationFee = 80.0,
            availableDates = listOf("15 de Mayo", "16 de Mayo", "17 de Mayo"),
            availableTimes = listOf("09:00 AM", "11:30 AM", "03:00 PM")
        ),
        Doctor(
            id = 2,
            name = "Dra. Sofía Ramírez",
            specialty = "Pediatría",
            experienceYears = 8,
            rating = 4.8,
            reviewsCount = 98,
            biography = "Pediatra dedicada al cuidado integral del desarrollo infantil y prevención de enfermedades pediátricas tempranas con un enfoque cálido y humano.",
            avatarUrl = "https://images.unsplash.com/photo-1594824813566-88855ce78347?w=400",
            consultationFee = 65.0,
            availableDates = listOf("15 de Mayo", "18 de Mayo", "19 de Mayo"),
            availableTimes = listOf("08:30 AM", "10:00 AM", "02:30 PM")
        ),
        Doctor(
            id = 3,
            name = "Dr. Alejandro Torres",
            specialty = "Dermatología",
            experienceYears = 10,
            rating = 4.7,
            reviewsCount = 115,
            biography = "Experto en dermatología clínica y estética, tratamientos de salud cutánea y cuidado integral de la piel para pacientes de todas las edades.",
            avatarUrl = "https://images.unsplash.com/photo-1537368910025-700350fe46c7?w=400",
            consultationFee = 75.0,
            availableDates = listOf("16 de Mayo", "17 de Mayo", "20 de Mayo"),
            availableTimes = listOf("10:00 AM", "01:00 PM", "04:30 PM")
        ),
        Doctor(
            id = 4,
            name = "Dra. Elena Gómez",
            specialty = "Neurología",
            experienceYears = 15,
            rating = 5.0,
            reviewsCount = 180,
            biography = "Especialista en trastornos del sistema nervioso, cefaleas complejas y enfermedades neurodegenerativas con amplia trayectoria clínica e investigativa.",
            avatarUrl = "https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=400",
            consultationFee = 90.0,
            availableDates = listOf("17 de Mayo", "18 de Mayo", "21 de Mayo"),
            availableTimes = listOf("09:30 AM", "12:00 PM", "05:00 PM")
        ),
        Doctor(
            id = 5,
            name = "Dr. Roberto Vargas",
            specialty = "Medicina General",
            experienceYears = 7,
            rating = 4.6,
            reviewsCount = 76,
            biography = "Médico cirujano enfocado en medicina preventiva, diagnóstico oportuno y atención médica primaria de alta calidad orientada a la familia.",
            avatarUrl = "https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=400",
            consultationFee = 50.0,
            availableDates = listOf("15 de Mayo", "16 de Mayo", "17 de Mayo"),
            availableTimes = listOf("08:00 AM", "11:00 AM", "02:00 PM")
        ),
        Doctor(
            id = 6,
            name = "Dra. Lucía Paredes",
            specialty = "Ginecología",
            experienceYears = 11,
            rating = 4.9,
            reviewsCount = 130,
            biography = "Especialista en salud femenina integral, control prenatal, ginecología médica y atención personalizada en cada etapa de la mujer.",
            avatarUrl = "https://images.unsplash.com/photo-1527613426441-4da17471b66d?w=400",
            consultationFee = 70.0,
            availableDates = listOf("16 de Mayo", "19 de Mayo", "20 de Mayo"),
            availableTimes = listOf("09:00 AM", "01:30 PM", "04:00 PM")
        )
    )

    val initialAppointments: List<Appointment> = listOf(
        Appointment(
            id = 1,
            doctorId = 1,
            doctorName = "Dr. Carlos Mendoza",
            doctorSpecialty = "Cardiología",
            doctorAvatar = "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=400",
            patientName = patientName,
            date = "15 de Mayo",
            time = "09:00 AM",
            status = "Confirmada",
            reason = "Evaluación cardiovascular de rutina"
        ),
        Appointment(
            id = 2,
            doctorId = 2,
            doctorName = "Dra. Sofía Ramírez",
            doctorSpecialty = "Pediatría",
            doctorAvatar = "https://images.unsplash.com/photo-1594824813566-88855ce78347?w=400",
            patientName = patientName,
            date = "10 de Abril",
            time = "10:00 AM",
            status = "Completada",
            reason = "Chequeo preventivo de desarrollo"
        )
    )

    val medicalHistory: List<MedicalHistoryItem> = listOf(
        MedicalHistoryItem(
            id = 1,
            date = "10 de Abril",
            doctorName = "Dra. Sofía Ramírez",
            specialty = "Pediatría",
            diagnosis = "Evaluación preventiva favorable",
            treatment = "Dieta balanceada e hidratación constante.",
            prescription = "Suplemento vitamínico 10ml cada 24 horas por 30 días."
        ),
        MedicalHistoryItem(
            id = 2,
            date = "22 de Febrero",
            doctorName = "Dr. Alejandro Torres",
            specialty = "Dermatología",
            diagnosis = "Dermatitis de contacto leve",
            treatment = "Aplicación de crema emoliente y evitar jabones abrasivos.",
            prescription = "Hidrocortisona 1% crema tópica cada 12 horas por 5 días."
        ),
        MedicalHistoryItem(
            id = 3,
            date = "15 de Diciembre",
            doctorName = "Dr. Roberto Vargas",
            specialty = "Medicina General",
            diagnosis = "Cuadro gripal / Rinitis estacional",
            treatment = "Abundante consumo de líquidos y descanso.",
            prescription = "Paracetamol 500mg cada 8 horas por 3 días."
        )
    )
}
