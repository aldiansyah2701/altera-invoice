package com.altera.invoice.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.altera.invoice.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, String>{
	
	Product findByName(String name);

}
