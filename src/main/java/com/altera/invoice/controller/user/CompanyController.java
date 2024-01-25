package com.altera.invoice.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altera.invoice.service.CompanyService;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	
	@GetMapping(value = "/get-product/{companyid}")
	public ResponseEntity<Object> getProducts(@PathVariable("companyid") Long companyid) {
		return companyService.getProducts(companyid);
	}

}
