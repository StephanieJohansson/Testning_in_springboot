package com.example.user_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
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

// component test for UserController with MockMvc to test the controllers behavior isolated from other parts of the app
// MockMvc is used to simulate an HTTP call without a real/complete server and Mock is used to create fake/mocked
// objects for the dependencies UserController needs to isolate the test

@SpringBootTest
class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    // manuel instance
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock WebClient.Builder
        WebClient mockWebClient = mock(WebClient.class);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(mockWebClient);

        // Mock UserRepository behavior to return fake user by id
        User testUser = new User(1L, "Benny", "Benny@email.com", "Street 1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // initializing UserController with mocked dependencies
        String orderServiceUrl = "http://localhost:8081";
        this.userController = new UserController(webClientBuilder, orderServiceUrl, userRepository, userService);

        // initializing MockMvc. Configs to test the controller isolated
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
                // controlling HTTP 200, the name, email and address
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.address").value(testUser.getAddress()));
    }

}