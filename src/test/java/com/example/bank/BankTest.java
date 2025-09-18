package com.example.bank;

import com.example.bank.model.Bank;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BankTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Полный CRUD для Bank")
    void fullCrudForBank() throws Exception {

        // Create
        Bank bank = new Bank();
        bank.setName("ПАО Бонусбанк");
        bank.setBik("123456789");

        String bankJson = objectMapper.writeValueAsString(bank);

        String bankResp = mockMvc.perform(post("/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bankJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        int bankId = JsonPath.read(bankResp, "$.id");
        bank.setId(bankId);

        // Read by id
        mockMvc.perform(get("/banks/{id}", bankId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bankId))
                .andExpect(jsonPath("$.name").value("ПАО Бонусбанк"));

        // Update
        bank.setName("ПАО Бонусбанк Обновлено");
        bank.setBik("987654321");

        mockMvc.perform(put("/banks/{id}", bankId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bank)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ПАО Бонусбанк Обновлено"))
                .andExpect(jsonPath("$.bik").value("987654321"));

        // Delete
        mockMvc.perform(delete("/banks/{id}", bankId))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/banks/{id}", bankId))
                .andExpect(status().isNotFound());
    }
}
