package com.example.usermanagement.controller;

import com.example.usermanagement.dto.response.ResponseMessage;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.repository.bean.User;
import com.example.usermanagement.service.UserService;
import com.example.usermanagement.util.CSVUtils;
import com.example.usermanagement.util.ObjectToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getUsers(){

        log.info("Received request to GET all Users");

        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id){

        log.info("Received request to GET User with Id: |{}", id);

        return userService.getUser(id);
    }

    @GetMapping("/surname/{surname}")
    public List<User> getUsersBySurname(@PathVariable("surname") String surname){

        log.info("Received request to GET Users with surname: |{}", surname);

        return userService.getUsersBySurname(surname);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createUser(@Valid @RequestBody UserDto userDto){

        log.info("Received request to Create User: |{}", ObjectToJson.convert(userDto));

        userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(HttpStatus.OK,"User Created"));
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

        String message = "";

        //Check that the file is a csv
        if (!CSVUtils.hasCSVFormat(file)) {
            message = "Please upload a csv file";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage(HttpStatus.BAD_REQUEST, message));
        }

        try {
            List<String> messageList = userService.uploadUsers(file);

            //if the list is empty then there have been no errors. Otherwise there were conflicts
            if(messageList.isEmpty()){
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage(HttpStatus.OK, message));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseMessage(HttpStatus.CONFLICT, ObjectToJson.convert(messageList)));
            }

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(HttpStatus.EXPECTATION_FAILED,message));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable("id") Long id){

        log.info("Received request to delete User with Id: |{}", id);

        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(HttpStatus.OK,"User Deleted"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserDto userDto){

        log.info("Received request to Update User: |{}", ObjectToJson.convert(userDto));

        userService.updateUser(userDto, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(HttpStatus.OK,"User Updated"));
    }
}
