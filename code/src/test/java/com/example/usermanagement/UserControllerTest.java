package com.example.usermanagement;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.example.usermanagement.controller.UserController;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.exception.EntityAlreadyExistsException;
import com.example.usermanagement.repository.bean.User;
import com.example.usermanagement.service.UserService;
import com.example.usermanagement.util.ObjectToJson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    User USER_1 = new User(1L, "Andrea", "Rossi", "a.g@test", "pass", "Piazza Puccini");
    User USER_2 = new User(2L,"Antonio", "Rossi", "ant.g@test","pass", "Piazza Mazzini");

    @Test
    @DisplayName("Test Get all users - Success")
    public void getAllUsers_success() throws Exception {

        List<User> listUser = new ArrayList<User>();
        listUser.add(USER_1);
        listUser.add(USER_2);

        when(userService.getUsers())
                .thenReturn(listUser);

        this.mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test Get all users - Not Found")
    public void getAllUsers_notFound() throws Exception {

        when(userService.getUsers())
                .thenThrow(new EntityNotFoundException("Users not found"));

        this.mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test Get user by Id - Success")
    public void getUserById_success() throws Exception {

        when(userService.getUser(USER_2.getId()))
                .thenReturn(USER_2);

        this.mockMvc.perform(get("/api/v1/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(2)));
    }

    @Test
    @DisplayName("Test Get user by Id - Not Found")
    public void getUserById_Notfound() throws Exception {

        when(userService.getUser(USER_2.getId()))
                .thenThrow(new EntityNotFoundException("User with Id: " + USER_2.getId() + " does not exists"));

        this.mockMvc.perform(get("/api/v1/users/2"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Test Get user by Surname - Success")
    public void getUserBySurname_success() throws Exception {

        List<User> listUser = new ArrayList<User>();
        listUser.add(USER_2);

        when(userService.getUsersBySurname(USER_2.getSurname()))
                .thenReturn(listUser);

        this.mockMvc.perform(get("/api/v1/users/surname/Rossi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Test Get user by Surname - Not Found")
    public void getUserBySurname_NotFound() throws Exception {

        when(userService.getUsersBySurname(USER_2.getSurname()))
                .thenThrow(new EntityNotFoundException("Users not found"));

        this.mockMvc.perform(get("/api/v1/users/surname/Rossi"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test Delete user by Id - Success")
    public void deleteUserById_success() throws Exception {

        this.mockMvc.perform(delete("/api/v1/users/2"))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Test Delete user by Id - Not Found")
    public void deleteUserById_notfound() throws Exception {

        doThrow(new EntityNotFoundException("user with id: " + USER_2.getId() + " does not exists"))
                .when(userService)
                .deleteUser(2L);

        this.mockMvc.perform(delete("/api/v1/users/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test Create user without body - Bad Request")
    public void postUser_withoutbody_badrequest() throws Exception {

        this.mockMvc.perform(post("/api/v1/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Create user - Success")
    public void postUser_success() throws Exception {

        this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectToJson.convert(USER_1)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Create user already exists - Internal Server Error")
    public void postUser_alreadyExist() throws Exception {

        UserDto USER_1_DTO = new UserDto( "Andrea", "Rossi", "a.g@test", "pass", "Piazza Mazzini");

        doThrow(new EntityAlreadyExistsException("User with email: " + USER_1_DTO.getEmail() + " already exist"))
                .when(userService)
                .createUser(USER_1_DTO);

        this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectToJson.convert(USER_1_DTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("already exist"))
                );
    }

    @Test
    @DisplayName("Test Create user validation Error - Bad Request")
    public void postUser_validationError_email() throws Exception {

        User USER_3 = new User(3L,"Antonio", "Rossi", "ant.gtest", "pass", "Piazza Tasso");

        this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectToJson.convert(USER_3)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("{email=must be a well-formed email address}")));
    }

    @Test
    @DisplayName("Test Update user without body - Bad Request")
    public void putUser_withoutbody_badrequest() throws Exception {

        this.mockMvc.perform(put("/api/v1/users/" + USER_1.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Update user - Success")
    public void putUser_success() throws Exception {

        this.mockMvc.perform(put("/api/v1/users/" + USER_1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectToJson.convert(USER_1)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Update user validation Error - Bad Request")
    public void putUser_validationError_email() throws Exception {

        User USER_3 = new User(3L,"Antonio", "Rossi", "ant.gtest", "pass", "Piazza Tasso");

        this.mockMvc.perform(put("/api/v1/users/" +USER_3.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectToJson.convert(USER_3)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("{email=must be a well-formed email address}")));
    }

}