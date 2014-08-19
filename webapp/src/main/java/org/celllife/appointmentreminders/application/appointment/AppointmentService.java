package org.celllife.appointmentreminders.application.appointment;

import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;

public interface AppointmentService {

    /**
     * Saves an appointment. Will throw an exception if the patient id, appointment date or appointment time is null.
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

}
