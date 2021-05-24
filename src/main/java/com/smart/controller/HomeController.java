/**
 * 
 */
package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.helper.Message;
import com.smart.model.User;
import com.smart.repository.UserRepository;

/**
 * @author Adnan
 *
 */
@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/do_register")
	public String userRegister(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		try {

			System.out.println("agreement..>" + agreement);
			System.out.println("USer:-" + user);
			if (bindingResult.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			if (!agreement) {
				throw new Exception("You have not agreed condtion !!");
			}
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setImageUrl("defualt.png");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			System.out.println(user);
			userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Register Sucessfully !!", "alert-success"));

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));
		}

		return "signup";
	}

	@GetMapping("/signin")
	public String signIn(Model model) {
		model.addAttribute("title", "Login - Smart Contact Manager");

		return "login";
	}

}
