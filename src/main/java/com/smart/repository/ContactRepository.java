/**
 * 
 */
package com.smart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.model.Contact;
import com.smart.model.User;

/**
 * @author Adnan
 *
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
	@Query("from Contact as c where c.user.id=:id")
	public Page<Contact> getContactByUserId(@Param("id") int id, Pageable pageable);
	public List<Contact> findByNameContainingAndUser(String name, User user);

}
