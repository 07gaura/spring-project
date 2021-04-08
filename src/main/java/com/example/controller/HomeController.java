package com.example.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.userRepository;
import com.example.entities.User;
import com.example.entities.contact;
import com.example.entities.message;

@Controller
public class HomeController {

	@Autowired
	private userRepository userrepo; 
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@GetMapping("/")
	public String test(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");		
		return "home";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "Login - Smart Contact Manager");		
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "signup- Smart Contact Manager");	
		model.addAttribute("user", new User());
		return "signup";
	}
	
	//handler for register user
	@PostMapping("/register")
	public String registeruser(@Valid @ModelAttribute("user") User user, BindingResult error_result,@RequestParam(value="agreement",defaultValue="false") boolean agreement, Model m, HttpSession session ) throws Exception {
		
		if(error_result.hasErrors()) {
			System.out.println(error_result);
			return "signup";
		}
		
		if(!agreement) {
			System.out.println("You have not agreed terms and condition");
			m.addAttribute("user", user);
			session.setAttribute("message", new message("agreement not accepted","alert-danger"));
			return "signup";
		}
		
		try {
			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setPassword(bcrypt.encode(user.getPassword()));
			User result = this.userrepo.save(user);
			
			System.out.println("Agreement"+ agreement);
			System.out.println("user data "+ result);
			m.addAttribute("user", new User());
			session.setAttribute("message", new message("data inserted successfully","alert-success"));
		}catch(Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new message("Some Thing went Wrong"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		return "signup";
	} 
	
}
