package com.example.bank.service;

import com.example.bank.model.Bank;
import com.example.bank.repository.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> findAll() {
        return bankRepository.findAll();
    }

    public List<Bank> findBySearch(String search) {
        if (search == null || search.trim().isEmpty()) {
            return findAll();
        }
        return bankRepository.findBySearch(search.trim());
    }

    public List<Bank> findAllSorted(String sortBy, String sortOrder) {
        if ("bik".equalsIgnoreCase(sortBy)) {
            return "desc".equalsIgnoreCase(sortOrder) 
                ? bankRepository.findAllByOrderByBikDesc() 
                : bankRepository.findAllByOrderByBikAsc();
        } else {
            return "desc".equalsIgnoreCase(sortOrder) 
                ? bankRepository.findAllByOrderByNameDesc() 
                : bankRepository.findAllByOrderByNameAsc();
        }
    }

    public List<Bank> findBySearchAndSort(String search, String sortBy, String sortOrder) {
        List<Bank> banks;
        if (search == null || search.trim().isEmpty()) {
            banks = findAll();
        } else {
            banks = bankRepository.findBySearch(search.trim());
        }
        
        if (sortBy != null) {
            banks.sort((b1, b2) -> {
                int result = 0;
                if ("name".equalsIgnoreCase(sortBy)) {
                    result = b1.getName().compareToIgnoreCase(b2.getName());
                } else if ("bik".equalsIgnoreCase(sortBy)) {
                    result = b1.getBik().compareTo(b2.getBik());
                }
                return "desc".equalsIgnoreCase(sortOrder) ? -result : result;
            });
        }
        
        return banks;
    }

    public Optional<Bank> findById(Integer id) {
        return bankRepository.findById(id);
    }

    public Bank save(Bank bank) {
        return bankRepository.save(bank);
    }

    public void deleteById(Integer id) {
        bankRepository.deleteById(id);
    }
}