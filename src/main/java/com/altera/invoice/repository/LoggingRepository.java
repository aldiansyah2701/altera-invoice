package com.altera.invoice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.altera.invoice.entity.Logging;

public interface LoggingRepository extends MongoRepository<Logging, String>{

}
