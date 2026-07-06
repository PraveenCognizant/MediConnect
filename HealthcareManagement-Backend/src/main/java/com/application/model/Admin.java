package com.application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Admin
{
	@Id
	private String email;
	private String adminname;
	private String password;

	public Admin() {}

	public Admin(String email, String adminname, String password)
	{
		this.email = email;
		this.adminname = adminname;
		this.password = password;
	}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getAdminname() { return adminname; }
	public void setAdminname(String adminname) { this.adminname = adminname; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
}
