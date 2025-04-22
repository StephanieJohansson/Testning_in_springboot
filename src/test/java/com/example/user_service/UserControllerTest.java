package com.example.user_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// component test

class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    private UserController userController; // manuell instance

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock WebClient.Builder
        WebClient mockWebClient = mock(WebClient.class);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(mockWebClient);

        // Mock UserRepository behavior to return fake user
        User testUser = new User(1L, "Benny", "Benny@email.com", "Street 1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // initializing UserController with mocked dependencies
        String orderServiceUrl = "http://localhost:8080";
        this.userController = new UserController(webClientBuilder, orderServiceUrl, userRepository, userService);

        // initializing MockMvc
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void testGetUserByIdWhenUserExists() throws Exception {
        // creating the fake user
        User testUser = new User(1L, "Benny", "Benny@email.com", "Street 1");

        // mock to find the fake user
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));


        // perform GET /users/1 to verify the response
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk()) // Kontrollera HTTP 200
                .andExpect(jsonPath("$.name").value(testUser.getName())) // Kontrollera namn
                .andExpect(jsonPath("$.email").value(testUser.getEmail())) // Kontrollera e-post
                .andExpect(jsonPath("$.address").value(testUser.getAddress())); // Kontrollera adress
    }

}