package com.example.usermanagement.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserDto {

    @NotNull(message = "Name may not be null")
    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotNull(message = "Surname may not be null")
    @NotBlank(message = "Surname may not be blank")
    private String surname;

    @NotNull(message = "Email may not be null")
    @NotBlank(message = "Email may not be blank")
    @Email
    private String email;

    @NotNull(message = "Password may not be null")
    @NotBlank(message = "Password may not be blank")
    private String password;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public UserDto(@NotNull String name, @NotNull String surname, @NotNull @Email String email, @NotNull String password, String address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public UserDto() {
    }
}
