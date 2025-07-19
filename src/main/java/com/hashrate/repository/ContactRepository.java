package com.hashrate.repository;

import com.hashrate.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    @Query("SELECT c FROM Contact c ORDER BY c.createdAt DESC")
    List<Contact> findAllOrderByCreatedAtDesc();
    
    List<Contact> findByStatusOrderByCreatedAtDesc(Contact.ContactStatus status);
    
    @Query("SELECT c FROM Contact c WHERE c.createdAt >= :startDate ORDER BY c.createdAt DESC")
    List<Contact> findByCreatedAtAfterOrderByCreatedAtDesc(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT c FROM Contact c ORDER BY c.createdAt DESC LIMIT :limit")
    List<Contact> findTopNOrderByCreatedAtDesc(@Param("limit") int limit);
    
    long countByStatus(Contact.ContactStatus status);
    
    @Query("SELECT COUNT(c) FROM Contact c WHERE c.createdAt >= :startDate")
    long countByCreatedAtAfter(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT c FROM Contact c WHERE LOWER(c.email) = LOWER(:email) ORDER BY c.createdAt DESC")
    List<Contact> findByEmailIgnoreCaseOrderByCreatedAtDesc(@Param("email") String email);
    
    @Query("SELECT c FROM Contact c WHERE c.contactType = :contactType ORDER BY c.createdAt DESC")
    List<Contact> findByContactTypeOrderByCreatedAtDesc(@Param("contactType") Contact.ContactType contactType);
}