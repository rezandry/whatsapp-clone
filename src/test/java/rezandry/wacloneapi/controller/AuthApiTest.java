package rezandry.wacloneapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import rezandry.wacloneapi.entity.User;
import rezandry.wacloneapi.model.LoginUserRequest;
import rezandry.wacloneapi.model.TokenResponse;
import rezandry.wacloneapi.model.WebResponse;
import rezandry.wacloneapi.repository.UserRepository;
import rezandry.wacloneapi.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testLoginSuccess() throws Exception {

        User user = new User();
        user.setName("Reza");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setPhoneNumber("085234123123");
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setPhoneNumber("085234123123");
        request.setPassword("secret");

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getData().getToken());
                    assertNotNull(response.getData().getExpiredAt());

                    User userDb = userRepository.findById(request.getPhoneNumber()).orElse(null);
                    assertEquals(response.getData().getToken(), userDb.getToken());
                    assertEquals(response.getData().getExpiredAt(), userDb.getTokenExpiredAt());
                });
    }

}
