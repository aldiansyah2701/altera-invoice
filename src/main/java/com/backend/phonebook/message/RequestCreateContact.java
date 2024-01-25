package com.backend.phonebook.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestCreateContact {
	private String name;
	private String phoneNumber;
}
