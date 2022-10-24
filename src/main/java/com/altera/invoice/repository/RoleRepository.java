package com.altera.invoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.altera.invoice.entity.ModelUserAndRoles;
import com.altera.invoice.entity.Role;
import com.altera.invoice.entity.User;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
	
	void deleteByUser(User user);
	
	@Query(nativeQuery = true, value = "select u.name, u.uuid as uuid, ( select u2.is_active from users u2 where u2.uuid = u.uuid) as active, GROUP_CONCAT(r.name separator ';') as roles from roles r join users u on r.user_uuid = u.uuid group by u.uuid")
	List<ModelUserAndRoles> getAllUserAndRoles();
	
	@Query(nativeQuery = true, value = "select u.name, u.uuid as uuid,  ( select u2.is_active from users u2 where u2.uuid = u.uuid) as active, GROUP_CONCAT(r.name separator ';') as roles from roles r join users u on r.user_uuid = u.uuid where u.name = :name group by r.user_uuid")
	ModelUserAndRoles getUserAndRoles(@Param("name") String name);
}
