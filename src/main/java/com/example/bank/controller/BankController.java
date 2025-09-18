package com.example.bank.controller;

import com.example.bank.model.Bank;
import com.example.bank.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }
    
    @GetMapping
    public ResponseEntity<List<Bank>> getAllBanks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        List<Bank> banks;
        if (search != null) {
            banks = bankService.findBySearchAndSort(search, sortBy, sortOrder);
        } else if (sortBy != null) {
            banks = bankService.findAllSorted(sortBy, sortOrder);
        } else {
            banks = bankService.findAll();
        }
        
        return ResponseEntity.ok(banks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(@PathVariable Integer id) {
        return bankService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Bank> createBank(@RequestBody Bank bank) {
        Bank saved = bankService.save(bank);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bank> updateBank(@PathVariable Integer id,
                                           @RequestBody Bank bank) {
        return bankService.findById(id)
                .map(existing -> {
                    bank.setId(id);
                    return ResponseEntity.ok(bankService.save(bank));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable Integer id) {
        if (bankService.findById(id).isPresent()) {
            bankService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}