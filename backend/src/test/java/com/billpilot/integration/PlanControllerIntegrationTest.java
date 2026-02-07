package com.billpilot.integration;

import com.billpilot.TestcontainersConfig;
import com.billpilot.domain.auth.dto.AuthResponse;
import com.billpilot.domain.auth.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfig.class)
class PlanControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String accessToken;

    @BeforeEach
    void authenticate() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail("admin@acme.com");
        login.setPassword("password123");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andReturn().getResponse().getContentAsString();

        AuthResponse auth = objectMapper.readValue(response, AuthResponse.class);
        this.accessToken = auth.getAccessToken();
    }

    @Test
    void listPlans_shouldReturnSeededPlans() throws Exception {
        mockMvc.perform(get("/api/v1/plans")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void createPlan_shouldReturnCreatedPlan() throws Exception {
        String body = """
                {"name":"Test Plan","price":49.99,"billingInterval":"MONTHLY","trialDays":7}
                """;
        mockMvc.perform(post("/api/v1/plans")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Plan"))
                .andExpect(jsonPath("$.price").value(49.99));
    }

    @Test
    void listPlans_unauthenticated_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/plans"))
                .andExpect(status().isUnauthorized());
    }
}
