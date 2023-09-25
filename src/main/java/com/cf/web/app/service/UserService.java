package com.cf.web.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cf.web.app.wrapper.UserWrapper;

public interface UserService {

	ResponseEntity<String> signUp(Map<String, String> requestMap);

	ResponseEntity<String> login(Map<String, String> requeMap);

	ResponseEntity<List<UserWrapper>> getAllUser();

	ResponseEntity<String> update(Map<String, String> requMap);

	ResponseEntity<String> checkToken();

	ResponseEntity<String> changePassowrd(Map<String, String> requestMap);

	ResponseEntity<String> forgotPassowrd(Map<String, String> requeMap);
}
