package org.celllife.appointmentreminders.domain.message;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

    Iterable<Message> findByAppointmentIdAndMessageDateAndMessageTime(Long patientId, Date messageDate, Date messageTime);

    Iterable<Message> findByMessageStateAndMessageDate(MessageState messageState, Date messageDate);

}
