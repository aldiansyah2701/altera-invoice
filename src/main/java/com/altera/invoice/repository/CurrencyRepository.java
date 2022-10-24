package com.altera.invoice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.altera.invoice.entity.Currency;


@Repository
public interface CurrencyRepository extends CrudRepository<Currency, String> {
	
	Currency findByName(String name);

}
