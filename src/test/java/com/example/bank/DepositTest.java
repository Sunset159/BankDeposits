package com.example.bank;

import com.example.bank.model.Bank;
import com.example.bank.model.Client;
import com.example.bank.model.Deposit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DepositTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Полный CRUD для Deposit")
    void fullCrudForDeposit() throws Exception {

        // Create client
        Client client = new Client();
        client.setName("ООО Пальма");
        client.setShortName("Пальма");
        client.setAddress("г. Москва, ул. Ленина, д.1");
        client.setLegalForm("ООО");

        String clientJson = objectMapper.writeValueAsString(client);
        String clientResp = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int clientId = JsonPath.read(clientResp, "$.id");
        client.setId(clientId);

        // Create bank
        Bank bank = new Bank();
        bank.setName("ПАО Бонусбанк");
        bank.setBik("123456789");

        String bankJson = objectMapper.writeValueAsString(bank);
        String bankResp = mockMvc.perform(post("/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bankJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int bankId = JsonPath.read(bankResp, "$.id");
        bank.setId(bankId);

        // Create deposit
        Client depositClient = new Client();
        depositClient.setId(clientId);
        
        Bank depositBank = new Bank();
        depositBank.setId(bankId);
        
        Deposit deposit = new Deposit();
        deposit.setClient(depositClient);
        deposit.setBank(depositBank);
        deposit.setOpenDate(LocalDate.of(2025, 1, 15));
        deposit.setPercent(5.75);
        deposit.setTermMonths(12);

        String depositJson = objectMapper.writeValueAsString(deposit);
        String depositResp = mockMvc.perform(post("/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        int depositId = JsonPath.read(depositResp, "$.id");

        // Read by id
        mockMvc.perform(get("/deposits/{id}", depositId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(depositId))
                .andExpect(jsonPath("$.percent").value(5.75));

        // Update
        Client updateClient = new Client();
        updateClient.setId(clientId);
        
        Bank updateBank = new Bank();
        updateBank.setId(bankId);
        
        Deposit updateDeposit = new Deposit();
        updateDeposit.setClient(updateClient);
        updateDeposit.setBank(updateBank);
        updateDeposit.setOpenDate(LocalDate.of(2025, 1, 15));
        updateDeposit.setPercent(6.5);
        updateDeposit.setTermMonths(24);

        mockMvc.perform(put("/deposits/{id}", depositId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDeposit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.percent").value(6.5))
                .andExpect(jsonPath("$.termMonths").value(24));

        // Delete
        mockMvc.perform(delete("/deposits/{id}", depositId))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/deposits/{id}", depositId))
                .andExpect(status().isNotFound());
    }
}