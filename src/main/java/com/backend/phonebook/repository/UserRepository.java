package com.backend.phonebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.phonebook.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{
	User findByName(String name);

}
