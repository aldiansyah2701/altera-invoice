package com.altera.invoice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.altera.invoice.entity.Invoice;
import com.altera.invoice.entity.User;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, String> {

	Invoice findByBusinessKey(String businessKey);
	
	Page<Invoice> findByUser(User user, Pageable pageable);
	
	List<Invoice> findByStatus(String status);

}