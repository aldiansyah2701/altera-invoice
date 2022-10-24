package com.altera.invoice.entity;
import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name="organization")
@Table(name="organization")
public class Organization extends BaseEntity implements Serializable {
	
	public enum TYPE{
		SELLER, BUYER
	}
	
	private static final long serialVersionUID = 4211854570169058068L;
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;
	
	@Column(name="name")
	private String name;
	
	@Column(name="type")
	private String type;
	
	@JoinColumn(name= "user_uuid")
	@ManyToOne
	private User user;

}
