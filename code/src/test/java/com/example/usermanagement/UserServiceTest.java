package com.example.usermanagement;

import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.repository.bean.User;
import com.example.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    User USER_1 = new User(1L, "Andrea", "Rossi", "a.g@test", "pass", "Piazza Puccini");
    User USER_2 = new User(2L,"Antonio", "Rossi", "ant.g@test", "pass", "Piazza Mazzini");

    @Test
    @DisplayName("Test find all user - Success")
    public void findAllUsers_success() throws Exception {
        List<User> listUser = new ArrayList<User>();
        listUser.add(USER_1);
        listUser.add(USER_2);
        when(userRepository.findAll()).thenReturn(listUser);

        Assertions.assertEquals(2, listUser.size());
    }

    @Test
    @DisplayName("Test find user ById - Success")
    void findById_success() {

        when(userRepository.findById(USER_1.getId())).thenReturn(Optional.of(USER_1));

        User user = userService.getUser(1L);

        // Assert the response
        Assertions.assertTrue(user.getEmail().equals(USER_1.getEmail()));
     }

    @Test
    @DisplayName("Test find user ById - Not Found")
    void findByIdNotFound() {

        when(userRepository.findById(USER_1.getId())).thenReturn(Optional.empty());

        EntityNotFoundException thrown =Assertions.assertThrows(EntityNotFoundException.class, () ->{
            User user = userService.getUser(1L);
        });

        Assertions.assertEquals("User with Id: 1 does not exists", thrown.getMessage());
    }

    @Test
    @DisplayName("Test find all users - Not Found")
    void findAll_NotFound() {

        List<User> listUser = new ArrayList<User>();

        when(userRepository.findAll()).thenReturn(listUser);

        EntityNotFoundException thrown =Assertions.assertThrows(EntityNotFoundException.class, () ->{
            List<User> users = userService.getUsers();
        });

        Assertions.assertEquals("Users not found", thrown.getMessage());
    }

    @Test
    @DisplayName("Test delete user - Success")
    void delete_success() {

        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);
    }

    @Test
    @DisplayName("Test delete user - Not Found")
    void delete_user_notfound() {

        when(userRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException thrown =Assertions.assertThrows(EntityNotFoundException.class, () ->{
            userService.deleteUser(1L);
        });

        Assertions.assertEquals("user with id: 1 does not exists", thrown.getMessage());
    }

    @Test
    @DisplayName("Test create user - Success")
    void create_user_success() {

        UserDto USER_1_DTO = new UserDto("Andrea", "Rossi", "a.g@test", "pass", "Piazza Puccini");

        when(userRepository.findUserByEmail(USER_1.getEmail())).thenReturn(Optional.empty());

        userService.createUser(USER_1_DTO);

    }

    @Test
    @DisplayName("Test create user already exists")
    void create_user_alreadyexists() {

        UserDto USER_1_DTO = new UserDto("Andrea", "Rossi", "a.g@test", "pass", "Piazza Puccini");

        when(userRepository.findUserByEmail(USER_1.getEmail())).thenReturn(Optional.of(USER_1));

        IllegalStateException thrown =Assertions.assertThrows(IllegalStateException.class, () ->{
            userService.createUser(USER_1_DTO);
        });

        Assertions.assertEquals("User with email: a.g@test already exist", thrown.getMessage());

    }

    @Test
    @DisplayName("Test update user - Success")
    void update_user_success() {

        UserDto USER_1_DTO = new UserDto("Andrea", "Rossi", "a.g@test", "pass", "Piazza Puccini");

        when(userRepository.findById(USER_1.getId())).thenReturn(Optional.of(USER_1));

        userService.updateUser(USER_1_DTO, 1L);

    }

    @Test
    @DisplayName("Test update user not exists - Not Found")
    void update_user_notexists() {

        UserDto USER_1_DTO = new UserDto("Andrea", "Rossi", "a.g@test", "pass", "Piazza Puccini");

        when(userRepository.findUserByEmail(USER_1.getEmail())).thenReturn(Optional.empty());

        EntityNotFoundException thrown =Assertions.assertThrows(EntityNotFoundException.class, () ->{
            userService.updateUser(USER_1_DTO, USER_1.getId());
        });

        Assertions.assertEquals("user with id: 1 does not exists", thrown.getMessage());

    }
}
