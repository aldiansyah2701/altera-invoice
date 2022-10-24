package com.altera.invoice.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestCreateInvoice {
	private String businessKey;
	
	private String sellerName;
	
	private String buyerName;
	
	private String amount;
	
	private String currencyUuid;
	
	private String productUuid;
	
	private String userUuid;
}
