package com.application.model;

/**
 * Unified Authentication Response DTO.
 *
 * Returned by both /registerUser, /registerdoctor, /loginuser and /logindoctor
 * so the Angular frontend always receives the same shape of data after any
 * successful auth operation, making auto-login-on-signup trivial.
 */
public class AuthResponse {

    /** JWT bearer token — store this in localStorage as "TOKEN" */
    private String token;

    /** The user's email address — used as the primary identifier in the app */
    private String email;

    /**
     * Display name:
     *   - User   → value of User.username
     *   - Doctor → value of Doctor.doctorname
     */
    private String name;

    /** "user" | "doctor" — drives role-based routing on the frontend */
    private String role;

    /** gender value stored from the entity — pre-fills profile views */
    private String gender;

    /** age stored from the entity (only relevant for users, empty for doctors) */
    private String age;

    /** For doctors: the specialization field */
    private String specialization;

    // ── No-arg constructor (required by Jackson) ──────────────────────────────
    public AuthResponse() {}

    // ── All-arg constructor ───────────────────────────────────────────────────
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

    // ── Getters & Setters ─────────────────────────────────────────────────────
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
