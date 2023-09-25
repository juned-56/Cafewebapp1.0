package com.cf.web.app.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cf.web.app.wrapper.UserWrapper;

@RequestMapping(path = "/user")
public interface UserRest {

	@PostMapping(path = "/signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);
	 
	
	@PostMapping(path = "/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requeMap);

	
	@GetMapping(path = "/get")
	public ResponseEntity<List<UserWrapper>> getAllUser();
	
	
	@PostMapping(path = "/update")
	public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requMap);

	
	@GetMapping(path = "/checkToken")
	ResponseEntity<String> checkToken();
	
	
	@PostMapping(path = "/changePaswword")
	ResponseEntity<String> changePAssword(@RequestBody Map<String, String> requestMap);
	
	
	@PostMapping(path = "/forgotPassword")
	ResponseEntity<String> forgotPassowrd(@RequestBody Map<String, String> requeMap); 
}
