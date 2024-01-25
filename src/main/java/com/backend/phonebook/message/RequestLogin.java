package com.backend.phonebook.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLogin {
	
	private String username;
	private String password;

}
