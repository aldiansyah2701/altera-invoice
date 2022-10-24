package com.altera.invoice.entity;
import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name= "invoice")
@Table(name="invoice")
public class Invoice extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 4211854570169058068L;
	
	public enum STATUS{
		PENDING, APPROVED, REJECTED, EXPIRED
	}
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;
	
	@Column(name = "business_key")
	private String businessKey;
	
	@Column(name="seller_code")
	private String sellerCode;
	
	@Column(name="buyer_code")
	private String buyerCode;
	
	@Column
	private String status;
	
	@Column(nullable = false)
	private Double amount;
	
	@JoinColumn(name = "currency_uuid")
	@ManyToOne
	private Currency currency;
	
	@JoinColumn(name = "product_uuid")
	@ManyToOne
	private Product product;
	
	@JoinColumn(name= "user_uuid")
	@ManyToOne
	private User user;

}
