package com.example.controller;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import com.example.email.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.email.EmailCfg;
import com.example.contactRepository;
import com.example.userRepository;
import com.example.entities.User;
import com.example.entities.contact;

@Controller
@RequestMapping(value={"/user"})
public class userController {
	@Autowired
	private EmailCfg emailcfg;
	@Autowired
	public userRepository userRepo;
	
	@Autowired
	public contactRepository contRepo;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@GetMapping("/index")
	public String dashboard( Model model, Principal principal) {
		String name = principal.getName();
		User user = this.userRepo.getUserByUserName(name);
		model.addAttribute("title", "User Dashboard");
		model.addAttribute("contact", new contact());
		model.addAttribute("userName", user.getName());
		return "normal/userdash";
	}
	
	@PostMapping("/savePass")
	public String savepassword(@ModelAttribute("user") contact cont, Principal principal) {
		String name = principal.getName();
		cont.setPassword(encode(cont.getPassword()));
		User user = this.userRepo.getUserByUserName(name);
		
		cont.setUser(user);
		user.getContacts().add(cont);
		this.userRepo.save(user);
		return "normal/userdash";
	}
	
	@GetMapping("/allpass/{page}")
	public String passwords(@PathVariable("page") Integer page, Model m, Principal principal) {
		String username = principal.getName();
		User user = this.userRepo.getUserByUserName(username);
		Pageable pageable = PageRequest.of(page, 24);
		Page<contact>contacts = this.contRepo.findContactByUser(user.getId(), pageable);
		m.addAttribute("title", "Passwords");
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/allpass";
	}
	
	@GetMapping("/confirm")
	public String confirmPass(@RequestParam int num, Model m) {
		System.out.println(num);
		m.addAttribute("title", "Password Mananger");
		m.addAttribute("value", num);
		return "normal/confirmpage";
	}
	
	@PostMapping("/updatecomplete")
	public String updateForm(@ModelAttribute("contact") contact cont, Principal principal, Model m) {
		User user = this.userRepo.getUserByUserName(principal.getName());
		contact conts = this.contRepo.findById(cont.getCid()).get();
		User user1 = conts.getUser();
		m.addAttribute("title", "Password Mananger");
		System.out.println(user1.getId());
		if(user.getId()==user1.getId()) {
			cont.setUser(user);
			cont.setPassword(encode(cont.getPassword()));
			this.contRepo.save(cont);

			return "normal/userdash";
		}
		else {
			return "normal/idmismatch";
		}
	}
	
	@GetMapping("/update-form/{id}")
	public String updates(@PathVariable("id") Integer id, Model m) {
		contact con = this.contRepo.findById(id).get();
		String str = con.getPassword();
		con.setPassword(decoder(str));
		m.addAttribute("contact", con);
		return "normal/update";
	}
	
	@PostMapping("/sendpass")
	public String sendMail(@ModelAttribute User user, Principal principal, Model m) {
		m.addAttribute("title", "Password Mananger");
		int id = user.getId();
		String password = user.getPassword();
		String username = principal.getName();
		User user1 = this.userRepo.getUserByUserName(username);
		contact cont = this.contRepo.findById(id).get();
		String userpass = user1.getPassword();
		System.out.println(cont.getUser());
		User user2 =cont.getUser();
		if(bcrypt.matches(password, userpass)&&user2.getId()==user1.getId()) {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost(this.emailcfg.getHost());
			mailSender.setPort(this.emailcfg.getPort());
			mailSender.setUsername(this.emailcfg.getUsername());
			mailSender.setPassword(this.emailcfg.getPassword());
			
			SimpleMailMessage mailmsg = new SimpleMailMessage();
			mailmsg.setFrom("gouravlokhande198@gmail.com");
			mailmsg.setTo(user1.getEmail());
			mailmsg.setSubject("No reply and dont share with any one");
			mailmsg.setText("You have requested your saved password of "+cont.getCname()+" and its password is "+decoder(cont.getPassword()));
			mailSender.send(mailmsg);
		}
		else {
			System.out.println("password Not Matched");
			return "normal/idmismatch";
		}
		return "normal/sendmail";
	}
	
	@GetMapping("/generator")
	public String genratePass(@RequestParam int password, Model m) {
		m.addAttribute("title", "Password Generator");
		String str="";
		for(int i=0;i<=password;i++) {
			int character = (int)(Math.random()*93+33);
			char ch = (char)character;
			str = str+ch;
		}
		m.addAttribute("generatedpass", str);
		return "normal/passpage";
	}
	
	//Base64 encoder
	public static String encode(String str) {
		Base64.Encoder encoder= Base64.getEncoder();
		byte[] encoded = encoder.encode(str.getBytes());
		return new String(encoded);
	}
	
	//base64 decoder
	public static String decoder(String str) {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] decoded = decoder.decode(str);
		return new String(decoded);
	}
}
