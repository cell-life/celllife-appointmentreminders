package org.celllife.appointmentreminders.application.appointment;

import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;

import java.util.Date;
import java.util.List;

public interface AppointmentService {

    /**
     * Saves an appointment. Will throw an exception if the patient id, appointment date or appointment time is null.
     * If an appointment already exists, will not create a new appointment for the Patient.
     * @param appointment
     * @return The new or updated appointment.
     * @throws RequiredFieldIsNullException
     */
    Appointment save(Appointment appointment) throws RequiredFieldIsNullException;

    /**
     * Gets an appointment by Id.
     * @param appointmentId
     * @return
     */
    Appointment get(Long appointmentId);

    /**
     * Deletes the specified appointment (and all its messages)
     * @param appointmentId Long appointment identifier
     * @return Appointment
     */
    Appointment delete(Long appointmentId);

    /**
     * Returns true if an appointment of this date and time already exists for the patient.
     * @param patientId
     * @param appointmentDate
     * @param appointmentTime
     * @return
     */
    boolean appointmentExists(Long patientId, Date appointmentDate, Date appointmentTime);

    /**
     * Finds an appointment by patient, date and time.
     * @param patientId
     * @param appointmentDate
     * @param appointmentTime
     * @return
     */
    List<Appointment> findByPatientIdAndDateTimeStamp(Long patientId, Date appointmentDate, Date appointmentTime);

}
