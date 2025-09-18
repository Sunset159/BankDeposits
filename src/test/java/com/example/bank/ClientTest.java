package com.example.bank;

import com.example.bank.model.Client;
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
public class ClientTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Полный CRUD для Client")
    void fullCrudForClient() throws Exception {

        // Creat
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
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        int clientId = JsonPath.read(clientResp, "$.id");
        client.setId(clientId);

        // Read by id
        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.name").value("ООО Пальма"));

        // Update
        client.setName("ООО Пальма Обновлено");
        client.setAddress("г. Москва, ул. Пушкина, д.10");

        mockMvc.perform(put("/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ООО Пальма Обновлено"))
                .andExpect(jsonPath("$.address").value("г. Москва, ул. Пушкина, д.10"));

        // Delete
        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isNotFound());
    }
}
