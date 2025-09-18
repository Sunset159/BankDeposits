package com.example.bank.repository;

import com.example.bank.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Integer>, JpaSpecificationExecutor<Deposit> {
    List<Deposit> findByClientId(Integer clientId);
    List<Deposit> findByBankId(Integer bankId);
    
    @Query("SELECT d FROM Deposit d WHERE d.percent >= :minPercent")
    List<Deposit> findByMinPercent(@Param("minPercent") Double minPercent);
    
    @Query("SELECT d FROM Deposit d WHERE d.percent <= :maxPercent")
    List<Deposit> findByMaxPercent(@Param("maxPercent") Double maxPercent);
    
    @Query("SELECT d FROM Deposit d WHERE d.percent BETWEEN :minPercent AND :maxPercent")
    List<Deposit> findByPercentRange(@Param("minPercent") Double minPercent, @Param("maxPercent") Double maxPercent);
    
    List<Deposit> findAllByOrderByOpenDateAsc();
    List<Deposit> findAllByOrderByOpenDateDesc();
    List<Deposit> findAllByOrderByPercentAsc();
    List<Deposit> findAllByOrderByPercentDesc();
}