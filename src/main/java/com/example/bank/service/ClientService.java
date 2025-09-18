package com.example.bank.service;

import com.example.bank.model.Client;
import com.example.bank.repository.ClientRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public List<Client> findBySearch(String search, String legalForm) {
        if ((search == null || search.trim().isEmpty()) && (legalForm == null || legalForm.trim().isEmpty())) {
            return findAll();
        }
        
        if (legalForm != null && !legalForm.trim().isEmpty()) {
            if (search != null && !search.trim().isEmpty()) {
                return clientRepository.findBySearchAndLegalForm(search.trim(), legalForm.trim());
            } else {
                return clientRepository.findByLegalForm(legalForm.trim());
            }
        } else {
            return clientRepository.findBySearch(search.trim());
        }
    }

    public List<Client> findAllSorted(String sortBy, String sortOrder) {
        if ("shortName".equalsIgnoreCase(sortBy)) {
            return "desc".equalsIgnoreCase(sortOrder) 
                ? clientRepository.findAllByOrderByShortNameDesc() 
                : clientRepository.findAllByOrderByShortNameAsc();
        } else {
            return "desc".equalsIgnoreCase(sortOrder) 
                ? clientRepository.findAllByOrderByNameDesc() 
                : clientRepository.findAllByOrderByNameAsc();
        }
    }

    public List<Client> findAll(Specification<Client> spec, Sort sort) {
        return clientRepository.findAll(spec, sort);
    }

    public Optional<Client> findById(Integer id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void deleteById(Integer id) {
        clientRepository.deleteById(id);
    }
}