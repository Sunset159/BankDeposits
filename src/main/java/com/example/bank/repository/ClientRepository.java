package com.example.bank.repository;

import com.example.bank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.shortName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.address) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Client> findBySearch(@Param("search") String search);
    
    List<Client> findByLegalForm(String legalForm);
    
    @Query("SELECT c FROM Client c WHERE (:search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.shortName) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:legalForm IS NULL OR c.legalForm = :legalForm)")
    List<Client> findBySearchAndLegalForm(@Param("search") String search, @Param("legalForm") String legalForm);
    
    List<Client> findAllByOrderByNameAsc();
    List<Client> findAllByOrderByNameDesc();
    List<Client> findAllByOrderByShortNameAsc();
    List<Client> findAllByOrderByShortNameDesc();
}