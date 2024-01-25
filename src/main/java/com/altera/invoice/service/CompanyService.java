package com.altera.invoice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.altera.invoice.entity.Company;
import com.altera.invoice.entity.CompanyProduct;
import com.altera.invoice.message.BaseResponse;
import com.altera.invoice.repository.CompanyProductRepository;
import com.altera.invoice.repository.CompanyRepository;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private CompanyProductRepository companyProductRepository;
	
	public ResponseEntity<Object> getProducts(Long companyId) {
		BaseResponse response = new BaseResponse();

		Optional<Company> company = companyRepository.findById(companyId);
		if (!company.isPresent()) {
			response.setMessage(BaseResponse.NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		Company getComp = company.get();
		List<CompanyProduct> products = companyProductRepository.findByCompany(getComp);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

}
