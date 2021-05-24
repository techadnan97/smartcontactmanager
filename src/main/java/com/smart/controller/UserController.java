/**
 * 
 */
package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.annotations.MetaValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.helper.Message;
import com.smart.model.Contact;
import com.smart.model.User;
import com.smart.repository.ContactRepository;
import com.smart.repository.UserRepository;

/**
 * @author Adnan
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void commonUse(Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		System.out.println(user);

		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String dashBoard(Model model, Principal principal) {

		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContact(Model model) {
		Contact contact = new Contact();
		//.setContacts("9120289715");
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact",contact );
		return "normal/add_contact";
	}

	@PostMapping("/process_contact")
	public String addContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
			@RequestParam("profileImage") MultipartFile multipartFile, Principal principal, Model model,
			HttpSession session) {
		System.out.println("contact" + contact);
		try {
			if (result.hasErrors()) {
				model.addAttribute("contact", contact);
				return "normal/add_contact";
			}
			if (!multipartFile.isEmpty()) {
				contact.setImage(multipartFile.getOriginalFilename());

				File file = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
				Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			else {
				contact.setImage("contact.png");

			}
			System.out.println("contact.." + contact);
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);
			user.getContacts().add(contact);
			contact.setUser(user);
			userRepository.save(user);
			model.addAttribute("title", "Add Contact");
			model.addAttribute("contact", new Contact());
			session.setAttribute("message", new Message("Contact Added Sucesfully !! ", "alert-success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));

		}

		return "normal/add_contact";
	}

	@GetMapping("/view-contact/{page}")
	public String showContacts(@PathVariable("page") int page, Model model, Principal principal, HttpSession session) {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = contactRepository.getContactByUserId(user.getId(), pageable);
		model.addAttribute("title", "View Contact");
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", contacts.getTotalPages());
		return "normal/view_contact";
	}

	@GetMapping("/{id}/contact")
	public String showContactDetail(@PathVariable("id") int id, Model model, Principal principal) {
		try {
			Contact contact = contactRepository.findById(id).get();
			User user = userRepository.getUserByUserName(principal.getName());
			if (user.getId() == contact.getUser().getId())
				model.addAttribute("contactDetail", contact);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "normal/contact_detail";
	}

	@GetMapping("/{id}/detete-contact/{page}")
	public String deleteContact(@PathVariable("id") int id, @PathVariable("page") int page, HttpSession session,
			Model model, Principal principal) {
		try {
			Contact contact = contactRepository.findById(id).get();
			User user = userRepository.getUserByUserName(principal.getName());
			if (user.getId() == contact.getUser().getId()) {
				user.getContacts().remove(contact);
				contactRepository.delete(contact);
				userRepository.save(user);
				session.setAttribute("message",
						new Message(contact.getName() + " " + "  Contact deleted sucesfully !! ", "alert-success"));
			} else {
				session.setAttribute("message", new Message(" OPPS !!  you don't have permission", "alert-danger"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message(" OPPS !!  you don't have permission", "alert-danger"));
		}
		return "redirect:/user/view-contact/" + page;
	}

	@GetMapping("/update_contact/{id}")
	public String updateContact(@PathVariable("id") int id, Model model) {
		Contact contact = contactRepository.findById(id).get();
		model.addAttribute("contact", contact);

		return "normal/update_contact";
	}

	@PostMapping("/update_process")
	public String processUpdateContact(@Valid @ModelAttribute Contact contact, BindingResult result, Model model,
			HttpSession session, Principal principal ,@RequestParam("profileImage") MultipartFile multipartFile) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("contact", contact);
				return "normal/update_contact";
			}
			Contact oldcontact = contactRepository.findById(contact.getCid()).get();
			if (!multipartFile.isEmpty()) {
				contact.setImage(multipartFile.getOriginalFilename());
				File deletefile = new ClassPathResource("static/image").getFile();
				File file1= new File(deletefile, oldcontact.getImage());
				file1.delete();
				File file = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
				Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			else {
				contact.setImage(oldcontact.getImage());

			}
			System.out.println(contact.getCid());
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);
			contact.setUser(user);
			Contact save = contactRepository.save(contact);
			System.out.println("save.." + save.getName()+" "+save.getCid());
			model.addAttribute("title", "update Contact");
			model.addAttribute("contact", new Contact());
			session.setAttribute("message", new Message("Contact Updated Sucesfully !! ", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "normal/update_contact";
	}
	@GetMapping("/user-profile")
	public String userProfile(Model model,Principal principal) {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		model.addAttribute("title", user.getName());
		model.addAttribute("user", user);
		
		return "normal/user_profile";
	}

}
