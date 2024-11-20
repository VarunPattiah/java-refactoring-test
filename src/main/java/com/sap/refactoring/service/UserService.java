package com.sap.refactoring.service;

import ch.qos.logback.core.util.StringUtil;
import com.sap.refactoring.persistence.entity.UserInfo;
import com.sap.refactoring.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<UserInfo> getUsers() {
        log.info("UserService: fetching all Users");
        return userRepository.findAll();
    }

    public UserInfo createUser(UserInfo user) throws  IllegalArgumentException{
        String email = user.getEmail();
        validateEmail(email);
        user.setEmail(email.toLowerCase(Locale.ENGLISH));
        log.info("UserService: Going to create User");
        return this.userRepository.save(user);
    }

    private void validateEmail(String email) throws IllegalArgumentException {

        if (StringUtil.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("Email Field is Missing");
        }

        List<UserInfo> userInfoList = this.userRepository.findByEmail(email);
        log.info("UserService: validateEmail -> Existing user with same email: {}", userInfoList);

        if (userInfoList != null && userInfoList.size() > 0) {
            throw new IllegalArgumentException("Email already Used");
        }

    }

    public UserInfo updateUser(UserInfo user) {
       boolean isExist =  this.userRepository.existsById(user.getUserId());
        log.info("UserService: updateUser -> Did user exist: {}", isExist);

       if (isExist) {
           String email = user.getEmail();
           validateEmail(email);
           user.setEmail(email.toLowerCase(Locale.ENGLISH));
           this.userRepository.save(user);
           return user;
       }

       return null;
    }

    public UserInfo getUser(Integer userId) {
        log.info("UserService: Finding User: {}", userId);
        return this.userRepository.findById(userId).orElse(null);
    }

    public void deleteUser(Integer userId) {
        log.info("UserService: Deleting User: {}", userId);
        this.userRepository.deleteById(userId);
    }
}
