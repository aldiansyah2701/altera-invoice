package com.altera.invoice.message;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestRegisterUser {

	private String fullName;
	
	private String password;
	
	private List<String> roles;
	
}
