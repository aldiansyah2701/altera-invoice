package com.altera.invoice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.altera.invoice.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{
	User findByName(String name);

}
