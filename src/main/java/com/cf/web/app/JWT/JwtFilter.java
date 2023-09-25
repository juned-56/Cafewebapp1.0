package com.cf.web.app.JWT;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private CustomerUsersDetailsService service;
	
	Claims claims = null;
	private String userName = null;
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		if(httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {  
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		else {
			String authorizationHeader = httpServletRequest.getHeader("Authorization");
			String token = null;
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				userName = jwtUtils.extractUsername(token);
				claims = jwtUtils.extraAllClaims(token);
			}
			if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = service.loadUserByUsername(userName);
				if(jwtUtils.ValidateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
							);
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			}
		}
	public boolean isAdmin() {
		return "admin".equalsIgnoreCase((String) claims.get("role"));
}
	public boolean isUser() {
		return "user".equalsIgnoreCase((String) claims.get("role"));
	}
	
	public String getCurrentUser() {
		return userName;
	}
}
	
//	@Override
//	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
//			throws ServletException, IOException {
//		if(httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {
//			filterChain.doFilter(httpServletRequest, httpServletResponse);
//		}
//		else {
//			String authorizationHeader = httpServletRequest.getHeader("Authorization");
//			String token = null;
//			
//			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//				token = authorizationHeader.substring(7);
//				userName = jwtUtils.extractUsername(token);
//				claims = jwtUtils.extraAllClaims(token);
//			}
//			if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
//				UserDetails userDetails = service.loadUserByUsername(userName);
//				if(jwtUtils.ValidateToken(token, userDetails)) {
//					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//					usernamePasswordAuthenticationToken.setDetails(
//							new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
//							);
//					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//				}
//			}
//			filterChain.doFilter(httpServletRequest, httpServletResponse);
//			}
//		}
//	public boolean isAdmin() {
//		return "admin".equalsIgnoreCase((String) claims.get("role"));
//}
//	public boolean isUser() {
//		return "user".equalsIgnoreCase((String) claims.get("role"));
//	}
//	
//	public String getCurrentUser() {
//		return userName;
//	}
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
//	@Override
//	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest httpServletRequest,
//			jakarta.servlet.http.HttpServletResponse httpServletResponse, jakarta.servlet.FilterChain filterChain)
//			throws jakarta.servlet.ServletException, IOException {
//		if(httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {
//			filterChain.doFilter(httpServletRequest, httpServletResponse);
//		}
//		else {
//			String authorizationHeader = httpServletRequest.getHeader("Authorization");
//			String token = null;
//			
//			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//				token = authorizationHeader.substring(7);
//				userName = jwtUtils.extractUsername(token);
//				claims = jwtUtils.extraAllClaims(token);
//			}
//			if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
//				UserDetails userDetails = service.loadUserByUsername(userName);
//				if(jwtUtils.ValidateToken(token, userDetails)) {
//					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//					usernamePasswordAuthenticationToken.setDetails(
//							new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
//							);
//					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//				}
//			}
//			filterChain.doFilter(httpServletRequest, httpServletResponse);
//			}
//		}
//	public boolean isAdmin() {
//		return "admin".equalsIgnoreCase((String) claims.get("role"));
//}
//	public boolean isUser() {
//		return "user".equalsIgnoreCase((String) claims.get("role"));
//	}
//	
//	public String getCurrentUser() {
//		return userName;
//	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
