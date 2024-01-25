package com.backend.phonebook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.phonebook.entity.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String>{

	Contact findByPhoneNumber(String phoneNumb);
	
	Page<Contact> findAll(Pageable pageable);
	
	Page<Contact> findByNameContainingOrPhoneNumberContaining(String value, String value2, Pageable pageable);
}
