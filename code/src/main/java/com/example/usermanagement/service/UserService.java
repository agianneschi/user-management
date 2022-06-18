package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.repository.bean.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.util.CSVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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


    public List<User> getUsers(){

        List<User> users = userRepository.findAll();

        if (users.isEmpty()){
            throw new EntityNotFoundException("Users not found");
        }

        return userRepository.findAll();
    }

    public List<User> getUsersBySurname(String surname){

        List<User> users = userRepository.findAll();
        List<User> usersFilter = users
                .stream().filter(u -> u.getSurname().toLowerCase().equals(surname.toLowerCase()))
                .collect(Collectors.toList());

        if (usersFilter.isEmpty()){
            throw new EntityNotFoundException("Users not found");
        }

        return usersFilter;
    }

    public User getUser(Long userId){

        Optional<User> userOptional =  userRepository.findById(userId);

        if(!userOptional.isPresent()){
            throw new EntityNotFoundException("User with Id: " +userId+ " does not exists");
        }

        return userOptional.get();

    }

    public void createUser(UserDto userDto){

        User user = userMapper.userDtoToUser(userDto);

        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());

        if(userOptional.isPresent()){
            throw new IllegalStateException(
                    "User with email: " + user.getEmail() + " already exist"
            );
        }

        log.debug("Creating user with id: {}", user.getId());
        userRepository.save(user);
        log.debug("Created user with id: {}", user.getId());
    }

    public void deleteUser(Long userId){

        boolean exists = userRepository.existsById(userId);

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

    public List<String> uploadUsers(MultipartFile file) {

        List<String> errorList = new LinkedList<>();

        try {
            List<User> users = CSVUtils.csvToUsers((file.getInputStream()));
            users.forEach(user -> {

                Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());

                if(userOptional.isPresent()){
                    errorList.add("User with email: " + user.getEmail() + " not saved due: email already exists");
                }


                if(!userOptional.isPresent()) {
                    try {
                        userRepository.save(user);
                    } catch (Exception e) {
                        errorList.add("User with email: " + user.getEmail() + " not saved due: " + e.getCause().getCause());
                    }
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }

        return errorList;
    }

}
