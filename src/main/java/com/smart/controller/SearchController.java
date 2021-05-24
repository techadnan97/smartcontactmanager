/**
 * 
 */
package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smart.model.Contact;
import com.smart.model.EmailSend;
import com.smart.model.User;
import com.smart.repository.ContactRepository;
import com.smart.repository.UserRepository;

/**
 * @author Adnan
 *
 */
@RestController
/* @CrossOrigin */
public class SearchController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	@GetMapping("/serach/{query}")
	public ResponseEntity<?> seacrh(@PathVariable("query") String name,Principal principal){
		User user = userRepository.getUserByUserName(principal.getName());
		List<Contact> contact = contactRepository.findByNameContainingAndUser(name,user);
		return ResponseEntity.ok(contact);
		
	}
	/*
	 * @PostMapping("/sendEmails") public ResponseEntity<?> sendEmail(@RequestBody
	 * EmailSend emailSend){ System.out.println("emailSend"+emailSend); return
	 * ResponseEntity.ok(emailSend); }
	 */
}
