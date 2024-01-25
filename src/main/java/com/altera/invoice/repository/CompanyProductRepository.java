package com.altera.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.altera.invoice.entity.Company;
import com.altera.invoice.entity.CompanyProduct;

public interface CompanyProductRepository extends CrudRepository<CompanyProduct, Long>{
	List<CompanyProduct> findByCompany(Company comp);
}
