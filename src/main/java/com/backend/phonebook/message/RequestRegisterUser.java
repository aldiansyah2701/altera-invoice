package com.backend.phonebook.message;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestRegisterUser {

	private String username;
	
	private String password;
	
	private List<String> roles;
	
}
