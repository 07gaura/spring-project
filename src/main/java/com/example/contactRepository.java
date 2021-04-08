package com.example;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.entities.User;
import com.example.entities.contact;

public interface contactRepository extends CrudRepository<contact, Integer>{
	
	@Query("select a from contact a where a.user.id = :userid")
	public Page<contact> findContactByUser(@Param("userid") int userid, Pageable pePageable);
}
