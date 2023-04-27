package com.academy.mortgage.services;

import com.academy.mortgage.exceptions.DuplicateUserException;
import com.academy.mortgage.exceptions.UserNotFoundException;
import com.academy.mortgage.model.User;
import com.academy.mortgage.model.api.request.ApplicationRequest;
import com.academy.mortgage.model.api.response.UserResponse;
import com.academy.mortgage.model.enums.Role;
import com.academy.mortgage.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public Boolean checkEmail(String email) {
        System.out.println("checkEmail in Service");
        return userRepository.existsByEmail(email);
    }

    public User addUser(ApplicationRequest applicationRequest, String password) {
        User user = User.builder()
                .firstName(applicationRequest.getFirstName())
                .lastName(applicationRequest.getLastName())
                .email(applicationRequest.getEmail())
                .password(passwordEncoder.encode(password))
                .personalNumber(applicationRequest.getPersonalNumber())
                .phoneNumber(applicationRequest.getPhoneNumber())
                .address(applicationRequest.getAddress())
                .role(Role.USER)
                .build();

        try {
            User savedUser = userRepository.save(user);

            return savedUser;
        } catch (DataIntegrityViolationException e) {
            if (e.getRootCause() instanceof PSQLException && ((PSQLException) e.getRootCause()).getSQLState().equals("23505")) {
                throw new DuplicateUserException("A user with this email already exists.");
            } else {
                throw e;
            }
        }
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public UserResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }
}
