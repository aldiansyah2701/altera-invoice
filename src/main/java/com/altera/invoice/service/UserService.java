package com.altera.invoice.service;

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

import com.altera.invoice.entity.Logging;
import com.altera.invoice.entity.ModelUserAndRoles;
import com.altera.invoice.entity.Role;
import com.altera.invoice.entity.Role.ROLE;
import com.altera.invoice.entity.User;
import com.altera.invoice.kernel.AuthEntryPointJwt;
import com.altera.invoice.kernel.JwtTokenProvider;
import com.altera.invoice.message.BaseResponse;
import com.altera.invoice.message.RequestRegisterUser;
import com.altera.invoice.message.ResponseCreateToken;
import com.altera.invoice.message.ResponseGetAllUsers;
import com.altera.invoice.repository.RoleRepository;
import com.altera.invoice.repository.UserRepository;

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
		User findByName = userRepository.findByName(data.getFullName());
		if (findByName != null) {
			response.setMessage(BaseResponse.ALREADY_EXIST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		user.setName(data.getFullName());
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
		User user = userRepository.findByName(data.getFullName());

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
		
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create("https://api.rajaongkir.com/starter/province"))
				  .headers("key", "766038aafa282632fb8bd394dd49102b")
				  .GET()
				  .build();
		
		try {
			HttpResponse<String> responseHttp = HttpClient.newBuilder()
					  .followRedirects(HttpClient.Redirect.ALWAYS)
					  .build()
					  .send(request, BodyHandlers.ofString());
			
			System.out.println(responseHttp.body());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		List<ModelUserAndRoles> datas = roleRepository.getAllUserAndRoles();
		List<ResponseGetAllUsers> response = new ArrayList<>();

		for (ModelUserAndRoles data : datas) {
			ResponseGetAllUsers resp = new ResponseGetAllUsers();
			resp.setName(data.getName());
			resp.setActive(data.getActive() != 0);
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
		resp.setActive(data.getActive() != 0);
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
