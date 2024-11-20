package com.sap.refactoring.api.controller;

import ch.qos.logback.core.util.StringUtil;
import com.sap.refactoring.persistence.entity.UserInfo;
import com.sap.refactoring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserInfo> getUsers()
    {
        return userService.getUsers();
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserInfo user) {
        ResponseEntity responseEntity = new ResponseEntity("User was created successfully", HttpStatus.CREATED);

        try {
            validateUserPayload(user);
            userService.createUser(user);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity(illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            log.error("Unexpected error on createUser:",e);
            return ResponseEntity.internalServerError().build();
        }

        return responseEntity;
    }

    private void validateUserPayload(UserInfo user) throws IllegalArgumentException{
        if (user == null || StringUtil.isNullOrEmpty(user.getName())) {
            throw  new IllegalArgumentException("User name is empty");
        }

        if(StringUtil.isNullOrEmpty(user.getEmail())) {
            throw  new IllegalArgumentException("User email is empty");
        }

        if(user.getRoles() == null || user.getRoles().isEmpty()) {
            throw  new IllegalArgumentException("User must have a role");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable Integer userId){
        UserInfo userInfo = userService.getUser(userId);
        if(userInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping
    public ResponseEntity updateUser(@RequestBody UserInfo user){
        UserInfo updatedUser;

        try {
            validateUserPayload(user);
            updatedUser =  this.userService.updateUser(user);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity(illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error on updateUser:",e);
            return ResponseEntity.internalServerError().build();
        }

        return (updatedUser == null)? ResponseEntity.badRequest().build() : ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
