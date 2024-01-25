package com.backend.phonebook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.phonebook.entity.Logging;

public interface LoggingRepository extends MongoRepository<Logging, String>{

}
