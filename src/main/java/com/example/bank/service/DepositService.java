package com.example.bank.service;

import com.example.bank.model.Deposit;
import com.example.bank.repository.DepositRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepositService {

    private final DepositRepository depositRepository;

    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    public List<Deposit> findAll() {
        return depositRepository.findAll();
    }

    public List<Deposit> findByFilters(Integer clientId, Integer bankId, Double minPercent, Double maxPercent) {
        if (clientId != null) {
            return depositRepository.findByClientId(clientId);
        }
        if (bankId != null) {
            return depositRepository.findByBankId(bankId);
        }
        if (minPercent != null && maxPercent != null) {
            return depositRepository.findByPercentRange(minPercent, maxPercent);
        }
        if (minPercent != null) {
            return depositRepository.findByMinPercent(minPercent);
        }
        if (maxPercent != null) {
            return depositRepository.findByMaxPercent(maxPercent);
        }
        return findAll();
    }

    public List<Deposit> findAllSorted(String sortBy, String sortOrder) {
        if ("percent".equalsIgnoreCase(sortBy)) {
            return "desc".equalsIgnoreCase(sortOrder) 
                ? depositRepository.findAllByOrderByPercentDesc() 
                : depositRepository.findAllByOrderByPercentAsc();
        } else {
            return "desc".equalsIgnoreCase(sortOrder) 
                ? depositRepository.findAllByOrderByOpenDateDesc() 
                : depositRepository.findAllByOrderByOpenDateAsc();
        }
    }

    public List<Deposit> findAll(Specification<Deposit> spec, Sort sort) {
        return depositRepository.findAll(spec, sort);
    }

    public Optional<Deposit> findById(Integer id) {
        return depositRepository.findById(id);
    }

    public Deposit save(Deposit deposit) {
        return depositRepository.save(deposit);
    }

    public void deleteById(Integer id) {
        depositRepository.deleteById(id);
    }
}