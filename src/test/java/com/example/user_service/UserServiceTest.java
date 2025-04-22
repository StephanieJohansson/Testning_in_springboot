package com.example.user_service;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class UserServiceTest {


    @Test
    void createUserShouldCallRepositorySave(){

        //Arrange
        // creating a mocked repository to try the test with a "fake repo"
        UserRepository mockedRepository = mock(UserRepository.class);
        UserService service = new UserService(mockedRepository);
        //creating a fake user to try in the fake repo
        User user = new User(1L,"Benny", "Benny@email.com", "Street 1");

        //fake scenario: when trying to call ID 1 it will return false
        //when(mockedRepository.existsById(1L)).thenReturn(false);

        //Act
        //getting the fake user I just created under Arrange
        service.createUser(user);


        //Assert
        // verifying that save was called
        verify(mockedRepository).save(user);

    }

    @Test
    void deleteUserWhenUserExistsShouldCallRepositoryDeleteById(){

        //Arrange
        // again creating a mocked repo as a fake repo for the test
        UserRepository mockedRepository = mock(UserRepository.class);
        UserService service = new UserService(mockedRepository);
        //setting a fake userId to 1L (Long) to be able to delete that user by the preset ID
        Long userId = 1L;

        // simulate that the user with id 1L exists in the fake repo
        when(mockedRepository.existsById(userId)).thenReturn(true);

        //Act
        service.deleteUser(userId);

        //Assert
        // verifying that deleteById was called
        verify(mockedRepository).deleteById(userId);
    }

    @Test
    void getUserByIdWhenUserExistsShouldCallRepositoryFindById(){

        //Arrange
        // again creating a mocked repo as a fake repo for the test
        UserRepository mockedRepository = mock(UserRepository.class);
        UserService service = new UserService(mockedRepository);
        Long userId = 1L;
        User user = new User(1L,"Benny", "Benny@email.com", "Street 1");

        //fake mocked repo to return the fake user when id (1L) is called
        when(mockedRepository.findById(userId)).thenReturn(Optional.of(user));

        //Act
        Optional<User> result = service.getUserById(userId);

        //Assert
        // controlling that Optional isnt empty
        assertTrue(result.isPresent());
        // controlling that the right user exists in Optional
        assertEquals(user, result.get());
        // verifying that findById was called
        verify(mockedRepository).findById(userId);
    }


}