package com.altera.invoice.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name= "company")
@Table(name= "company")
public class Company extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 4211854570169058068L;
	
	@Id
    private Long companyId;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "create_datetime")
	private Date createDatetime;
	
}
