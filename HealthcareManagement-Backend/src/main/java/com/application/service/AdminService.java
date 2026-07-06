package com.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.model.Admin;
import com.application.repository.AdminRepository;

@Service
public class AdminService
{
	@Autowired
	private AdminRepository adminRepository;

	public Admin findByEmail(String email)
	{
		return adminRepository.findByEmail(email);
	}

	public Admin save(Admin admin)
	{
		return adminRepository.save(admin);
	}
}
