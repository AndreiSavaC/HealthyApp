package com.proiectpdm.services.appointment

import com.proiectpdm.models.Appointment
import com.proiectpdm.models.AppointmentsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class AppointmentServiceImpl : AppointmentService {
    override suspend fun getAppointments(): List<Appointment> = transaction {
        AppointmentsTable.selectAll().map { resultRowToAppointment(it) }
    }

    override suspend fun getAppointmentById(id: Int): Appointment? = transaction {
        AppointmentsTable.selectAll().where(AppointmentsTable.id eq id).singleOrNull()
            ?.let { resultRowToAppointment(it) }
    }

    override suspend fun getAppointmentsByDate(date: String): List<Appointment> = transaction {
        AppointmentsTable.selectAll().where(AppointmentsTable.date eq date).map { resultRowToAppointment(it) }
    }

    override suspend fun getAppointmentsByDoctorId(doctorId: Int): List<Appointment> = transaction {
        AppointmentsTable.selectAll().where(AppointmentsTable.doctorId eq doctorId).map { resultRowToAppointment(it) }
    }

    override suspend fun getAppointmentsByPatientId(patientId: Int): List<Appointment> = transaction {
        AppointmentsTable.selectAll().where(AppointmentsTable.patientId eq patientId).map { resultRowToAppointment(it) }
    }

    override suspend fun getAppointmentsByDoctorIdAndDate(doctorId: Int, date: String): List<Appointment> =
        transaction {
            AppointmentsTable.selectAll()
                .where((AppointmentsTable.date eq date) and (AppointmentsTable.doctorId eq doctorId))
                .map { resultRowToAppointment(it) }
        }

    override suspend fun addAppointment(appointment: Appointment): Appointment? = transaction {
        val insertStmt = AppointmentsTable.insert {
            it[patientId] = appointment.patientId
            it[doctorId] = appointment.doctorId
            it[date] = appointment.date
            it[time] = appointment.time
        }
        insertStmt.resultedValues?.singleOrNull()?.let { resultRowToAppointment(it) }
    }


    override suspend fun updateAppointment(appointment: Appointment): Boolean = transaction {
        AppointmentsTable.update({ AppointmentsTable.id.eq(appointment.id) }) {
            it[date] = appointment.date
            it[time] = appointment.time
            it[patientId] = appointment.patientId
            it[doctorId] = appointment.doctorId
        } > 0
    }

    override suspend fun deleteAppointment(id: Int): Boolean = transaction {
        AppointmentsTable.deleteWhere { AppointmentsTable.id eq id } > 0
    }

    private fun resultRowToAppointment(resultRow: ResultRow): Appointment {
        return Appointment(
            patientId = resultRow[AppointmentsTable.patientId],
            doctorId = resultRow[AppointmentsTable.doctorId],
            date = resultRow[AppointmentsTable.date],
            time = resultRow[AppointmentsTable.time],
            id = resultRow[AppointmentsTable.id],
        )
    }
}