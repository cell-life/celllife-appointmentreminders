package org.celllife.appointmentreminders.domain.message;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

    List<Message> findByAppointmentIdAndMessageDateAndMessageTime(Long appointmentId, Date messageDate, Date messageTime);

    List<Message> findByMessageStateAndMessageDate(MessageState messageState, Date messageDate);

    List<Message> findByMessageState(MessageState messageState);
    
    @Query("select case when count(m) > 0 then true else false end "
            + "from Message m where m.appointmentId = :appointmentId "
            + "and m.messageDate = :messageDate "
            + "and m.messageTime = :messageTime")
    Boolean exists(@Param("appointmentId") Long appointmentId, @Param("messageDate") Date messageDate, @Param("messageTime") Date messageTime);

}
