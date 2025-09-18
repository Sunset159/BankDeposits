package com.example.bank.repository;

import com.example.bank.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Integer>, JpaSpecificationExecutor<Bank> {
    @Query("SELECT b FROM Bank b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR b.bik LIKE CONCAT('%', :search, '%')")
    List<Bank> findBySearch(@Param("search") String search);
    
    List<Bank> findAllByOrderByNameAsc();
    List<Bank> findAllByOrderByNameDesc();
    List<Bank> findAllByOrderByBikAsc();
    List<Bank> findAllByOrderByBikDesc();
}