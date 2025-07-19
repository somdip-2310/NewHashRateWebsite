package com.hashrate.repository;

import com.hashrate.model.Contact;
import com.hashrate.model.Contact.ContactStatus;
import com.hashrate.model.Contact.Industry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    Page<Contact> findByStatus(ContactStatus status, Pageable pageable);
    
    Page<Contact> findByIndustry(Industry industry, Pageable pageable);
    
    Page<Contact> findByAssignedTo(String assignedTo, Pageable pageable);
    
    @Query("SELECT c FROM Contact c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.company) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Contact> searchContacts(@Param("keyword") String keyword, Pageable pageable);
    
    List<Contact> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Contact> findByStatusAndCreatedAtBefore(ContactStatus status, LocalDateTime date);
    
    @Query("SELECT c FROM Contact c WHERE c.status = :status ORDER BY c.createdAt DESC")
    Page<Contact> findByStatusOrderByCreatedAtDesc(@Param("status") ContactStatus status, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Contact c WHERE c.status = :status")
    long countByStatus(@Param("status") ContactStatus status);
    
    @Query("SELECT c.industry, COUNT(c) FROM Contact c GROUP BY c.industry")
    List<Object[]> countByIndustryGrouped();
    
    @Query("SELECT DATE(c.createdAt), COUNT(c) FROM Contact c " +
           "WHERE c.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(c.createdAt) ORDER BY DATE(c.createdAt)")
    List<Object[]> getContactStatsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT c FROM Contact c WHERE c.status = 'NEW' ORDER BY c.createdAt ASC")
    List<Contact> findUnassignedContacts();
    
    boolean existsByEmailAndCreatedAtAfter(String email, LocalDateTime date);
}