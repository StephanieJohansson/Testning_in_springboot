package com.example.user_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

// integration test "testing a whole flow" from the app together - rest api, controller, service, database/repository
// with a random port in the SpringBootTest annotation to avoid conflict and a LocalServerPort to work with it.
// Autowire a TestRestTemplate for automatic configurations. This test is testing the flow from API to the database,
// verifying both post(create) and get(get) endpoints are working (save created user in database and GET it)

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateAndGetUserByIdByHTTP(){

        // sending in a user with no id since the database sets one automatically for new users, testing that this works
        User user = new User(null,"Bosse", "Bosse@email.com", "Street 3");
        ResponseEntity<User> postResponse =
                restTemplate.postForEntity("http://localhost:" + port + "/users", user, User.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode() );

        Long userId = postResponse.getBody().getId();

        ResponseEntity<User> getResponse =
                restTemplate.getForEntity("http://localhost:" + port + "/users/" + userId, User.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode() );
        assertEquals("Bosse", getResponse.getBody().getName() );
        assertEquals("Bosse@email.com", getResponse.getBody().getEmail() );
        assertEquals("Street 3", getResponse.getBody().getAddress() );


    }

}