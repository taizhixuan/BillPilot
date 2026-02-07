package com.billpilot.integration;

import com.billpilot.TestcontainersConfig;
import com.billpilot.domain.auth.dto.AuthResponse;
import com.billpilot.domain.auth.dto.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class TenantIsolationIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void differentOrgs_shouldNotSeeEachOthersData() throws Exception {
        // Create org1
        SignupRequest signup1 = new SignupRequest();
        signup1.setOrgName("Org One");
        signup1.setEmail("user@orgone.com");
        signup1.setPassword("password123");
        signup1.setFirstName("User");
        signup1.setLastName("One");

        String res1 = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token1 = objectMapper.readValue(res1, AuthResponse.class).getAccessToken();

        // Create org2
        SignupRequest signup2 = new SignupRequest();
        signup2.setOrgName("Org Two");
        signup2.setEmail("user@orgtwo.com");
        signup2.setPassword("password123");
        signup2.setFirstName("User");
        signup2.setLastName("Two");

        String res2 = mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String token2 = objectMapper.readValue(res2, AuthResponse.class).getAccessToken();

        // Create plan in org1
        mockMvc.perform(post("/api/v1/plans")
                        .header("Authorization", "Bearer " + token1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Org1 Plan\",\"price\":10,\"billingInterval\":\"MONTHLY\"}"))
                .andExpect(status().isCreated());

        // Org1 should see the plan
        mockMvc.perform(get("/api/v1/plans")
                        .header("Authorization", "Bearer " + token1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        // Org2 should NOT see org1's plan
        mockMvc.perform(get("/api/v1/plans")
                        .header("Authorization", "Bearer " + token2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
