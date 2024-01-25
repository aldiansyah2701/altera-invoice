package com.altera.invoice.repository;

import org.springframework.data.repository.CrudRepository;

import com.altera.invoice.entity.Company;

public interface CompanyRepository extends CrudRepository<Company, Long>{

}
