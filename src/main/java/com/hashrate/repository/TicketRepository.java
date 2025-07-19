package com.hashrate.repository;

import com.hashrate.model.Ticket;
import com.hashrate.model.Ticket.TicketPriority;
import com.hashrate.model.Ticket.TicketStatus;
import com.hashrate.model.Ticket.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByType(TicketType type, Pageable pageable);

    Page<Ticket> findByPriority(TicketPriority priority, Pageable pageable);

    Page<Ticket> findByAssignedTo(String assignedTo, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE " +
           "LOWER(t.ticketNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Ticket> searchTickets(@Param("keyword") String keyword, Pageable pageable);

    List<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority);

    List<Ticket> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Ticket t WHERE t.status = 'OPEN' ORDER BY " +
           "CASE t.priority " +
           "WHEN 'CRITICAL' THEN 1 " +
           "WHEN 'HIGH' THEN 2 " +
           "WHEN 'MEDIUM' THEN 3 " +
           "WHEN 'LOW' THEN 4 " +
           "END, t.createdAt ASC")
    List<Ticket> findOpenTicketsOrderByPriority();

    @Query("SELECT t.status, COUNT(t) FROM Ticket t GROUP BY t.status")
    List<Object[]> getTicketCountByStatus();

    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, t.createdAt, t.resolvedAt)) FROM Ticket t " +
           "WHERE t.status = 'RESOLVED' AND t.resolvedAt IS NOT NULL")
    Double getAverageResolutionTime();

    long countByStatus(TicketStatus status);

    long countByStatusAndPriority(TicketStatus status, TicketPriority priority);
}