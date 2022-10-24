package com.altera.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.altera.invoice.entity.Organization;
import com.altera.invoice.entity.User;


@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String>{
	
	Organization findByUserAndNameAndType(User user, String Name, String Type);
	
	List<Organization> findByUser(User user);

}
