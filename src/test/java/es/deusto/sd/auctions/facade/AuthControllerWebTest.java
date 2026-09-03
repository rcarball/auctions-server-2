package es.deusto.sd.auctions.facade;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.deusto.sd.auctions.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerWebTest {

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService)).build();
    }

    @Test
    void loginReturnsTokenForValidCredentials() throws Exception {
        when(authService.login("student@example.com", "password-hash")).thenReturn(Optional.of("token-123"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"student@example.com\",\"password\":\"password-hash\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("token-123"));
    }

    @Test
    void loginRejectsInvalidCredentials() throws Exception {
        when(authService.login(anyString(), anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"student@example.com\",\"password\":\"invalid\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutReturnsNoContentForValidToken() throws Exception {
        when(authService.logout("token-123")).thenReturn(Optional.of(true));

        mockMvc.perform(post("/auth/logout").content("token-123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void logoutRejectsUnknownToken() throws Exception {
        when(authService.logout("unknown-token")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/logout").content("unknown-token"))
                .andExpect(status().isUnauthorized());
    }
}
