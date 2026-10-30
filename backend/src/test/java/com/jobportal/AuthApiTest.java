package com.jobportal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") class AuthApiTest {
 @Autowired MockMvc mvc;

 @Test void seekerCanRegisterLoginAndReadOwnDashboardButNotEmployerDashboard() throws Exception {
  String body="""
    {"email":"seeker@example.com","password":"Correct-Horse-Battery","fullName":"Sam Seeker","role":"JOB_SEEKER"}
    """;
  String token=mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
    .andExpect(status().isCreated()).andExpect(jsonPath("$.user.roles[0]").value("JOB_SEEKER"))
    .andReturn().getResponse().getContentAsString();
  String accessToken=com.fasterxml.jackson.databind.json.JsonMapper.builder().build().readTree(token).get("accessToken").asText();
  mvc.perform(get("/api/seeker/dashboard").header("Authorization","Bearer "+accessToken)).andExpect(status().isOk());
  mvc.perform(get("/api/employer/dashboard").header("Authorization","Bearer "+accessToken)).andExpect(status().isForbidden());
  mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"seeker@example.com\",\"password\":\"wrong-password\"}"))
    .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.status").value(401));
 }

 @Test void publicRegistrationCannotAssignAdminRole() throws Exception {
  mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"admin-try@example.com\",\"password\":\"Long-Enough-Password\",\"fullName\":\"No Admin\",\"role\":\"ADMIN\"}"))
    .andExpect(status().isBadRequest());
 }
}
