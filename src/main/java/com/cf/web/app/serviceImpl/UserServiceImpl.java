package com.cf.web.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cf.web.app.JWT.CustomerUsersDetailsService;
import com.cf.web.app.JWT.JwtFilter;
import com.cf.web.app.JWT.JwtUtils;
import com.cf.web.app.constants.CafeConstants;
import com.cf.web.app.dao.UserDao;
import com.cf.web.app.model.User;
import com.cf.web.app.service.UserService;
import com.cf.web.app.utils.CafeUtils;
import com.cf.web.app.utils.EmailUtils;
import com.cf.web.app.wrapper.UserWrapper;
import com.google.common.base.Strings;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomerUsersDetailsService customerUsersDetailsService;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	EmailUtils emailUtils;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		logger.info("Inside SignUp {}", requestMap);
		try {
		if(validateSignUpMap(requestMap)) {
			User user = userDao.findByEmailId(requestMap.get("email"));
					if(Objects.isNull(user)) {
						userDao.save(getUserFromMap(requestMap));
						return CafeUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
					}
					else {
						return CafeUtils.getResponseEntity("Email is Already exist.", HttpStatus.BAD_REQUEST);
					}
		}
		else {
			return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
		&& requestMap.containsKey("email") && requestMap.containsKey("password")) {
			return true;
		}
		return false;
	}
	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requeMap) {
		logger.info("Inside login");
		try {
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requeMap.get("email"),
					requeMap.get("password"))
					);
			if(auth.isAuthenticated()) {
				if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>("{\"token\":\""+
				jwtUtils.generateToken(customerUsersDetailsService.getUserDetail().getEmail(), 
						customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
				HttpStatus.OK);
				}
				else {
					return new ResponseEntity<String>("{\"message\":\""+"wait for admin approval."
				+"\"}",HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			logger.error("{}", e);
		}
		return new ResponseEntity<String>("{\"message\":\""+"Bad credentials"
				+"\"}",HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if(jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
			}else {
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
			Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
			if(!optional.isEmpty()) {
				
				userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
				sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), 
						userDao.getAllAdmin());
				return CafeUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
			}else {
				return CafeUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
			}
			}else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR); 
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
		allAdmin.remove(jwtFilter.getCurrentUser());
		if(status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), 
					"Account Approved", "USER:- "+user+" "
							+ "\n is approved by \nADMIN:-"+
		jwtFilter.getCurrentUser(), allAdmin);
		}else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), 
					"Account Disabled", "USER:- "+user+" "
							+ "\n is Disabled by \nADMIN:-"+
		jwtFilter.getCurrentUser(), allAdmin);
		}
	}

	@Override
	public ResponseEntity<String> checkToken() {
		return CafeUtils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassowrd(Map<String, String> requestMap) {
		try {
			User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
			if(!userObj.equals(null)) {
				if(userObj.getPassword().equals(requestMap.get("oldPassword"))) {
					userObj.setPassword(requestMap.get("newPassword"));
					userDao.save(userObj);
					return CafeUtils.getResponseEntity("Password Update Successfully", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
			}
			return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgotPassowrd(Map<String, String> requeMap) {
		try {
			User user = userDao.findByEmail(requeMap.get("email"));
			if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())) {
				emailUtils.forgotMail(user.getEmail(), "Credentials By Cafe's", user.getPassword());
			return CafeUtils.getResponseEntity("Check your email for Credentials", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
