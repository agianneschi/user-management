package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.exception.EntityAlreadyExistsException;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.repository.bean.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.util.CSVUtils;
import com.example.usermanagement.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Transactional
    public List<User> getUsers(){

        List<User> users = userRepository.findAll();

        if (users.isEmpty()){
            throw new EntityNotFoundException("Users not found");
        }

        return userRepository.findAll();
    }

    @Transactional
    public List<User> getUsersBySurname(String surname){

        List<User> users = userRepository.findAll();

        //filter Users bySurname
        List<User> usersFilter = users
                .stream().filter(u -> u.getSurname().toLowerCase().equals(surname.toLowerCase()))
                .collect(Collectors.toList());

        if (usersFilter.isEmpty()){
            throw new EntityNotFoundException("Users not found");
        }

        return usersFilter;
    }

    @Transactional
    public User getUser(Long userId){

        Optional<User> userOptional =  userRepository.findById(userId);

        //check if User is present
        if(!userOptional.isPresent()){
            throw new EntityNotFoundException("User with Id: " +userId+ " does not exists");
        }

        return userOptional.get();

    }

    @Transactional
    public void createUser(UserDto userDto){

        User user = userMapper.userDtoToUser(userDto);

        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());

        //check if User is present
        if(userOptional.isPresent()){
            throw new EntityAlreadyExistsException(
                    "User with email: " + user.getEmail() + " already exist"
            );
        }

        log.debug("Creating user with id: {}", user.getId());
        userRepository.save(user);
        log.debug("Created user with id: {}", user.getId());
    }

    @Transactional
    public void deleteUser(Long userId){

        boolean exists = userRepository.existsById(userId);

        //check if User is present
        if (!exists){
            throw new EntityNotFoundException(
                    "user with id: " + userId + " does not exists"
            );
        }

        log.debug("Deleting user with id: {}", userId);
        userRepository.deleteById(userId);
        log.debug("Deleted user with id: {}", userId);
    }


    public void updateUser(UserDto userDto, Long id){

        User user = userMapper.userDtoToUser(userDto);

        Optional<User> userOptional = userRepository.findById(id);

        //check if User is present
        if(!userOptional.isPresent()){
            throw new EntityNotFoundException(
                    "user with id: " + id + " does not exists"
            );
        }

        if (user.getName()!= null && user.getName().length() > 0){
            userOptional.get().setName(user.getName());
        }

        if (user.getSurname() != null && user.getSurname().length() > 0){
            userOptional.get().setSurname(user.getSurname());
        }

        if (user.getEmail() != null && user.getEmail().length() > 0){
            userOptional.get().setEmail(user.getEmail());
        }

        if (user.getPassword() != null && user.getPassword().length() > 0){
            userOptional.get().setPassword(user.getPassword());
        }

        log.debug("Updating user with id: {}", id);
        userRepository.save(userOptional.get());
        log.debug("Updated user with id: {}", id);
    }

    @Transactional
    public List<String> uploadUsers(MultipartFile file) {

        //initialize List of validation error
        List<String> errorList = new LinkedList<>();

        try {

            //convert csv lines to userDto
            List<UserDto> usersDto = CSVUtils.csvToUsersDto((file.getInputStream()));
            usersDto.forEach(userDto -> {

                List<User> users = new LinkedList<>();

                //validate userDto
                String validationErr = ValidationUtils.validateUserDto(userDto);

                if (!validationErr.isEmpty()){
                    errorList.add("User with email: " + userDto.getEmail() + " not saved due: validation error: " + validationErr);
                } else {

                    User userToStore = userMapper.userDtoToUser(userDto);
                    Optional<User> userOptional = userRepository.findUserByEmail(userToStore.getEmail());

                    if(userOptional.isPresent()){
                        errorList.add("User with email: " + userDto.getEmail() + " not saved due: email already exists");
                    }
                    else {
                        users.add(userToStore);
                    }
                }
                userRepository.saveAll(users);
            });

        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }

        return errorList;
    }

}
