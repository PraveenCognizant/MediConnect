package com.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.AuthRequest;
import com.application.model.AuthResponse;
import com.application.model.Doctor;
import com.application.model.User;
import com.application.service.DoctorRegistrationService;
import com.application.service.UserRegistrationService;
import com.application.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
public class LoginController 
{
	
	@Autowired
	private UserRegistrationService userRegisterService;
	
	@Autowired
    private JwtUtils jwtUtil;
	
    @Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private DoctorRegistrationService doctorRegisterService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
    public String welcomeMessage()
    {
    	return "Welcome to HealthCare Management system !!!";
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception 
    {
        try 
        {
        	System.out.println(authRequest.getEmail());
        	System.out.println(authRequest.getPassword());
        	List<User> users = userRegisterService.getAllUsers();
        	String currentEmail = "";
    		for(User obj:users)
    		{
    			if(obj.getEmail().equalsIgnoreCase(authRequest.getEmail()))
    			{
    				currentEmail = obj.getUsername();
    			}
    		}
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentEmail, authRequest.getPassword()));
        } 
        catch (Exception ex) 
        {
            throw new Exception("Invalid Username/password");
        }
        return new ResponseEntity<String>(jwtUtil.generateToken(authRequest.getEmail()), HttpStatus.OK);
    }
	
	@PostMapping("/loginuser")
	public ResponseEntity<?> loginUser(@RequestBody User user) throws Exception
	{
		String currEmail = user.getEmail();
		String currPassword = user.getPassword();

		User userObj = null;
		if(currEmail != null && currPassword != null)
		{
			userObj = userRegisterService.fetchUserByEmail(currEmail);
			if(userObj == null || !passwordEncoder.matches(currPassword, userObj.getPassword()))
			{
				userObj = null;
			}
		}
		if(userObj == null)
		{
			throw new Exception("User does not exists!!! Please enter valid credentials...");
		}

		// Generate a fresh JWT for this login session
		String token = jwtUtil.generateToken(userObj.getEmail());

		// Return the unified AuthResponse — same shape as registration response
		AuthResponse response = new AuthResponse(
			token,
			userObj.getEmail(),
			userObj.getUsername(),
			"user",
			userObj.getGender(),
			userObj.getAge(),
			null
		);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/logindoctor")
	public ResponseEntity<?> loginDoctor(@RequestBody Doctor doctor) throws Exception
	{
		String currEmail = doctor.getEmail();
		String currPassword = doctor.getPassword();

		Doctor doctorObj = null;
		if(currEmail != null && currPassword != null)
		{
			doctorObj = doctorRegisterService.fetchDoctorByEmail(currEmail);
			if(doctorObj == null || !passwordEncoder.matches(currPassword, doctorObj.getPassword()))
			{
				doctorObj = null;
			}
		}
		if(doctorObj == null)
		{
			throw new Exception("User does not exists!!! Please enter valid credentials...");
		}

		// Generate a fresh JWT for this login session
		String token = jwtUtil.generateToken(doctorObj.getEmail());

		// Return the unified AuthResponse — same shape as registration response
		AuthResponse response = new AuthResponse(
			token,
			doctorObj.getEmail(),
			doctorObj.getDoctorname(),
			"doctor",
			doctorObj.getGender(),
			null,
			doctorObj.getSpecialization()
		);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
