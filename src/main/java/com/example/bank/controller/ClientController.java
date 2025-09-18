package com.example.bank.controller;

import com.example.bank.model.Client;
import com.example.bank.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String legalForm,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        List<Client> clients;
        if (search != null || legalForm != null) {
            clients = clientService.findBySearch(search, legalForm);
            if (sortBy != null) {
                clients.sort((c1, c2) -> {
                    int result = 0;
                    if ("name".equalsIgnoreCase(sortBy)) {
                        result = c1.getName().compareToIgnoreCase(c2.getName());
                    } else if ("shortName".equalsIgnoreCase(sortBy)) {
                        result = c1.getShortName().compareToIgnoreCase(c2.getShortName());
                    }
                    return "desc".equalsIgnoreCase(sortOrder) ? -result : result;
                });
            }
        } else if (sortBy != null) {
            clients = clientService.findAllSorted(sortBy, sortOrder);
        } else {
            clients = clientService.findAll();
        }
        
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Integer id) {
        return clientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client saved = clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer id,
                                               @RequestBody Client client) {
        return clientService.findById(id)
                .map(existing -> {
                    client.setId(id);
                    return ResponseEntity.ok(clientService.save(client));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        if (clientService.findById(id).isPresent()) {
            clientService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}