package com.application.model;

public class AuthResponse {

    private String token;

    private String email;

    private String name;

    private String role;

    private String gender;

    private String age;

    private String specialization;

    public AuthResponse() {}

    public AuthResponse(String token, String email, String name,
                        String role, String gender, String age, String specialization) {
        this.token        = token;
        this.email        = email;
        this.name         = name;
        this.role         = role;
        this.gender       = gender;
        this.age          = age;
        this.specialization = specialization;
    }

    public String getToken()           { return token; }
    public void   setToken(String t)   { this.token = t; }

    public String getEmail()           { return email; }
    public void   setEmail(String e)   { this.email = e; }

    public String getName()            { return name; }
    public void   setName(String n)    { this.name = n; }

    public String getRole()            { return role; }
    public void   setRole(String r)    { this.role = r; }

    public String getGender()          { return gender; }
    public void   setGender(String g)  { this.gender = g; }

    public String getAge()             { return age; }
    public void   setAge(String a)     { this.age = a; }

    public String getSpecialization()              { return specialization; }
    public void   setSpecialization(String s)      { this.specialization = s; }
}
