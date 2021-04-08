package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message="User Name can not be empty")
	private String name;
	@NotBlank(message="Please enter your mail")
	@Column(unique=true)
	private String email;
	@NotBlank(message="Please enter password")
	private String password;
	private String role;
	private String imageurl;
	private boolean enable;
	@NotBlank(message="Please tell us about Your self")
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="user")
	private List<contact> contacts = new ArrayList<>();
	
	
	public int getId() {
		return id;
	}
	
	public List<contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<contact> contacts) {
		this.contacts = contacts;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public User(int id, String name, String email, String password, String role, String imageurl, boolean enable,
			String about) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.imageurl = imageurl;
		this.enable = enable;
		this.about = about;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", imageurl=" + imageurl + ", enable=" + enable + ", contacts=" + contacts + ", about=" + about + "]";
	}
	
	
}
