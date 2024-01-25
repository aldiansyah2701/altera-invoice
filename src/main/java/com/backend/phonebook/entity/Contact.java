package com.backend.phonebook.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name ="contact")
@Table(name="contact")
public class Contact extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 4211854570169058068L;
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;
	
	@Column
	private String name;
	
	@Column
	private String phoneNumber;
	
	@Column(name= "is_active")
	private boolean isActive;

}