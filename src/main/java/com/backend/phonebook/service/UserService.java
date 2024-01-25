package com.backend.phonebook.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.phonebook.entity.Logging;
import com.backend.phonebook.entity.ModelUserAndRoles;
import com.backend.phonebook.entity.Role;
import com.backend.phonebook.entity.User;
import com.backend.phonebook.entity.Role.ROLE;
import com.backend.phonebook.kernel.AuthEntryPointJwt;
import com.backend.phonebook.kernel.JwtTokenProvider;
import com.backend.phonebook.message.BaseResponse;
import com.backend.phonebook.message.RequestRegisterUser;
import com.backend.phonebook.message.ResponseCreateToken;
import com.backend.phonebook.message.ResponseGetAllUsers;
import com.backend.phonebook.repository.RoleRepository;
import com.backend.phonebook.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	public ResponseEntity<Object> loginUser(String userName, String password) {
		BaseResponse response = new BaseResponse();
		try {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, password);
			Authentication authenticate = authenticationManager.authenticate(authentication); //HOW THIS WORKS?
			// ternyata proses aunthenticate call loadUserByUsername, untuk mendapatkan data dari database
			
			ResponseCreateToken responseJwt = jwtTokenProvider.createToken(userName);
			responseJwt.setMessage(BaseResponse.SUCCESS);
			log.info("Login success");

			return new ResponseEntity<>(responseJwt, HttpStatus.OK);
		} catch (AuthenticationException e) {
			response.setMessage(BaseResponse.FAILED);
			log.info("Login failed");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Transactional(readOnly = false)
	public ResponseEntity<Object> registerUser(RequestRegisterUser data) {
		BaseResponse response = new BaseResponse();
		User findByName = userRepository.findByName(data.getUsername());
		if (findByName != null) {
			response.setMessage(BaseResponse.ALREADY_EXIST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		user.setName(data.getUsername());
		user.setPassword(passwordEncoder.encode(data.getPassword()));
		user.setCreatedDate(new Date());
		user.setActive(true);
		user = userRepository.save(user);

		for (String roleName : data.getRoles()) {
			Role role = new Role();
			role.setName(ROLE.valueOf(roleName).toString());
			role.setCreatedDate(new Date());
			role.setUser(user);
			role = roleRepository.save(role);
		}
		response.setMessage(BaseResponse.SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@Transactional(readOnly = false)
	public ResponseEntity<Object> updateUser(RequestRegisterUser data) {
		BaseResponse response = new BaseResponse();
		User user = userRepository.findByName(data.getUsername());

		if (user != null) {
			user.setActive(true);
			user = userRepository.save(user);
			roleRepository.deleteByUser(user);
			for (String roleName : data.getRoles()) {
				Role role = new Role();
				role.setName(ROLE.valueOf(roleName).toString());
				role.setCreatedDate(new Date());
				role.setUser(user);
				role = roleRepository.save(role);
			}

			response.setMessage(BaseResponse.SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setMessage(BaseResponse.NOT_FOUND);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
	
	public ResponseEntity<Object> getAllUser() {
				
		List<ModelUserAndRoles> datas = roleRepository.getAllUserAndRoles();
		List<ResponseGetAllUsers> response = new ArrayList<>();

		for (ModelUserAndRoles data : datas) {
			ResponseGetAllUsers resp = new ResponseGetAllUsers();
			resp.setName(data.getName());
			resp.setUuid(data.getUuid());
			resp.setRoles(Arrays.asList(data.getRoles().split(";")));
			response.add(resp);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getUser(String name) {

		ModelUserAndRoles data = roleRepository.getUserAndRoles(name);
		ResponseGetAllUsers resp = new ResponseGetAllUsers();
		resp.setName(data.getName());
		resp.setUuid(data.getUuid());
		resp.setRoles(Arrays.asList(data.getRoles().split(";")));

		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@Transactional(readOnly = false)
	public ResponseEntity<Object> deleteUser(String name) {
		BaseResponse response = new BaseResponse();
		User user = userRepository.findByName(name);

		if (user != null) {
			user.setActive(false);
			user = userRepository.save(user);

			response.setMessage(BaseResponse.SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setMessage(BaseResponse.NOT_FOUND);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

}
