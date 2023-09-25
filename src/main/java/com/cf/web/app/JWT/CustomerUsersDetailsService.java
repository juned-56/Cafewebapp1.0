package com.cf.web.app.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cf.web.app.dao.UserDao;

import ch.qos.logback.classic.Logger;

@Service
public class CustomerUsersDetailsService implements UserDetailsService{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(CustomerUsersDetailsService.class);
	@Autowired
	UserDao userDao;
	
	private com.cf.web.app.model.User userDetail;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Inside loadUserByUsername {}",username);
		userDetail = userDao.findByEmailId(username);
		if(!Objects.isNull(userDetail))
			return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
		else
			throw new UsernameNotFoundException("User not Found!!!");
	}
	
	public com.cf.web.app.model.User getUserDetail(){
		return userDetail;
	}

	
}
