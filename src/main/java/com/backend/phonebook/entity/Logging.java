package com.backend.phonebook.entity;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document("logging")
public class Logging  implements Serializable{
	
	private static final long serialVersionUID = 4211854570169058068L;

	@Id
	private String id;
	
	private HashMap<String, Object> data;
	
	private String type;
}
