package com.example;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.entities.User;

public interface userRepository extends CrudRepository<User, Integer> {
	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);
}
