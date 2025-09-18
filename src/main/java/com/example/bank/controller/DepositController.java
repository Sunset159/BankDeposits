package com.example.bank.controller;

import com.example.bank.model.Bank;
import com.example.bank.model.Client;
import com.example.bank.model.Deposit;
import com.example.bank.service.BankService;
import com.example.bank.service.ClientService;
import com.example.bank.service.DepositService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposits")
public class DepositController {

    private final DepositService depositService;
    private final ClientService clientService;
    private final BankService bankService;

    public DepositController(DepositService depositService, 
                            ClientService clientService, 
                            BankService bankService) {
        this.depositService = depositService;
        this.clientService = clientService;
        this.bankService = bankService;
    }

    @GetMapping
    public ResponseEntity<List<Deposit>> getAllDeposits(
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) Integer bankId,
            @RequestParam(required = false) Double minPercent,
            @RequestParam(required = false) Double maxPercent,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        List<Deposit> deposits;
        if (clientId != null || bankId != null || minPercent != null || maxPercent != null) {
            deposits = depositService.findByFilters(clientId, bankId, minPercent, maxPercent);
            if (sortBy != null) {
                deposits.sort((d1, d2) -> {
                    int result = 0;
                    if ("openDate".equalsIgnoreCase(sortBy)) {
                        result = d1.getOpenDate().compareTo(d2.getOpenDate());
                    } else if ("percent".equalsIgnoreCase(sortBy)) {
                        result = Double.compare(d1.getPercent(), d2.getPercent());
                    } else if ("termMonths".equalsIgnoreCase(sortBy)) {
                        result = Integer.compare(d1.getTermMonths(), d2.getTermMonths());
                    }
                    return "desc".equalsIgnoreCase(sortOrder) ? -result : result;
                });
            }
        } else if (sortBy != null) {
            deposits = depositService.findAllSorted(sortBy, sortOrder);
        } else {
            deposits = depositService.findAll();
        }
        
        return ResponseEntity.ok(deposits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deposit> getDepositById(@PathVariable Integer id) {
        return depositService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Deposit> createDeposit(@RequestBody Deposit deposit) {
        if (deposit.getClient() == null || deposit.getClient().getId() == null) {
            throw new RuntimeException("Требуется ID клиента");
        }
        if (deposit.getBank() == null || deposit.getBank().getId() == null) {
            throw new RuntimeException("Требуется ID банка");
        }

        Client client = clientService.findById(deposit.getClient().getId())
                .orElseThrow(() -> new RuntimeException("ID клиента не найден: " + deposit.getClient().getId()));
        Bank bank = bankService.findById(deposit.getBank().getId())
                .orElseThrow(() -> new RuntimeException("ID банка не найден: " + deposit.getBank().getId()));

        deposit.setClient(client);
        deposit.setBank(bank);
        
        Deposit saved = depositService.save(deposit);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposit> updateDeposit(@PathVariable Integer id,
                                                 @RequestBody Deposit deposit) {
        return depositService.findById(id)
                .map(existing -> {
                    deposit.setId(id);
                    return ResponseEntity.ok(depositService.save(deposit));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Integer id) {
        if (depositService.findById(id).isPresent()) {
            depositService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}