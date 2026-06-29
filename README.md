<div align="center">
https://healthcare-management-system-mediconnect.vercel.app
<img src="https://img.shields.io/badge/MediConnect-Healthcare%20Management-blue?style=for-the-badge&logo=heart&logoColor=white" alt="MediConnect"/>

# 🏥 MediConnect — Healthcare Management System

**A full-stack healthcare platform connecting Patients, Doctors, and Admins**  
*Built with Spring Boot 3.5 · Angular 17 · MySQL · JWT Security*

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17.3-red?style=flat-square&logo=angular)](https://angular.io/)
[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-HS384-purple?style=flat-square&logo=jsonwebtokens)](https://jwt.io/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-blueviolet?style=flat-square&logo=bootstrap)](https://getbootstrap.com/)
[![License: ISC](https://img.shields.io/badge/License-ISC-yellow?style=flat-square)](https://opensource.org/licenses/ISC)

</div>

---

## 📑 Table of Contents

- [📌 Overview](#-overview)
- [✨ Features by Role](#-features-by-role)
- [🏗️ Architecture](#️-architecture)
- [🛠️ Tech Stack](#️-tech-stack)
- [📁 Project Structure](#-project-structure)
- [⚙️ Prerequisites](#️-prerequisites)
- [🚀 Getting Started](#-getting-started)
  - [1. Database Setup](#1-database-setup)
  - [2. Backend Setup](#2-backend-setup)
  - [3. Frontend Setup](#3-frontend-setup)
- [🔐 Default Credentials](#-default-credentials)
- [🌐 API Reference](#-api-reference)
- [🔒 Security Architecture](#-security-architecture)
- [🗄️ Database Schema](#️-database-schema)
- [🔄 Key Workflows](#-key-workflows)
- [🧪 Testing](#-testing)
- [📦 Build & Deploy](#-build--deploy)
- [🐛 Troubleshooting](#-troubleshooting)
- [🤝 Contributing](#-contributing)

---

## 📌 Overview

MediConnect is a full-stack **Healthcare Management System** that digitises the complete patient-doctor lifecycle:

> **Patient** registers → browses doctors → books an appointment → receives a prescription  
> **Doctor** registers → gets approved by admin → schedules slots → manages appointments → writes prescriptions  
> **Admin** approves doctors → oversees users, appointments, and prescriptions

The backend is a **stateless REST API** secured with **JWT (HS384)** and role-based access control. The Angular frontend uses **route guards** and an **HTTP interceptor** to enforce the same role boundaries on the client side.

---

## ✨ Features by Role

<details>
<summary><b>🔵 Admin</b> — click to expand</summary>

| Feature | Description |
|---|---|
| 📊 Admin Dashboard | Live stats: total doctors, users, appointments, slots, prescriptions |
| 👨‍⚕️ Doctor Onboarding | Approve or reject pending doctor registrations |
| ➕ Add Doctor Directly | Create a doctor account without the self-registration flow |
| 👥 User List | View all registered patients |
| 📋 Patient List | View all appointment records across all doctors |
| 🔐 Secure Login | JWT-based admin login with minimal `AdminAuthResponse` (4 fields) |

</details>

<details>
<summary><b>🟢 Doctor</b> — click to expand</summary>

| Feature | Description |
|---|---|
| 📝 Self Registration | Register with specialization, experience, previous hospital |
| ⏳ Pending Approval | Account starts as `pending` — usable only after admin approves |
| 📅 Schedule Slots | Create AM / Noon / PM availability slots for any date |
| 🗓️ Today's Appointments | View appointments booked for today's date |
| ✅ Accept / ❌ Reject | Accept or reject each pending appointment; rejected slots are freed |
| 💊 Write Prescriptions | Add diagnosis, medicine, dosage, and notes per patient |
| 👤 Edit Profile | Update specialization, experience, contact info |

</details>

<details>
<summary><b>🟡 User (Patient)</b> — click to expand</summary>

| Feature | Description |
|---|---|
| 📝 Self Registration | Register with full profile including age, gender, mobile, address |
| 🔍 Browse Doctors | View all approved doctors and their specializations |
| 🗓️ Check Slots | See real-time slot availability (AM / Noon / PM) for any doctor+date |
| 📆 Book Appointment | Book a slot — protected against double-booking via `synchronized` |
| 📄 Prescription List | View all prescriptions written by doctors |
| 👤 Edit Profile | Update personal details (password is never overwritten) |

</details>

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                   Browser (Angular 17)                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐  │
│  │  Route   │  │   HTTP   │  │ Services │  │ Components │  │
│  │  Guards  │  │Interceptor│  │(API calls)│  │ (UI pages) │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────────┘  │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP + Authorization: Bearer <JWT>
                           ▼
┌─────────────────────────────────────────────────────────────┐
│              Spring Boot 3.5 (port 8081)                    │
│                                                             │
│  ┌─────────────┐     ┌──────────────┐     ┌─────────────┐  │
│  │  JwtFilter  │────▶│  Controllers │────▶│  Services   │  │
│  │(every req.) │     │  (REST API)  │     │ (Business)  │  │
│  └─────────────┘     └──────────────┘     └─────────────┘  │
│                                                    │        │
│  ┌──────────────────────────────────────┐          ▼        │
│  │         SecurityConfig               │  ┌─────────────┐  │
│  │  CORS · CSRF disabled · STATELESS   │  │Repositories │  │
│  │  .hasRole(ADMIN / DOCTOR / USER)    │  │  (JPA/SQL)  │  │
│  └──────────────────────────────────────┘  └──────┬──────┘  │
└──────────────────────────────────────────────────┼──────────┘
                                                   │
                                                   ▼
                                        ┌─────────────────┐
                                        │   MySQL 8.0     │
                                        │  DB: healthcare │
                                        │ Tables: user,   │
                                        │ doctor, admin,  │
                                        │ appointments,   │
                                        │ slots,          │
                                        │ prescription    │
                                        └─────────────────┘
```

### Request Flow (protected endpoint)

```
Angular Component
  │  calls service.someMethod()
  ▼
HttpClient.get/post(...)
  │  AuthInterceptor attaches: Authorization: Bearer <JWT>
  ▼
Spring Boot — JwtFilter
  │  extracts email from JWT 'sub' claim
  │  loadUserByEmail(email) → checks User / Doctor / Admin table
  │  assigns ROLE_USER / ROLE_DOCTOR / ROLE_ADMIN
  │  sets SecurityContextHolder
  ▼
SecurityConfig authorization check
  │  .hasRole("ADMIN") / .hasRole("DOCTOR") / .hasRole("USER")
  │  PASS → Controller method runs
  │  FAIL → 403 Forbidden
  ▼
Controller → Service → Repository → MySQL
  ▼
JSON Response → Angular
```

---

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Runtime |
| Spring Boot | 3.5.8 | Application framework |
| Spring Security | 6.x | Authentication + Authorization |
| Spring Data JPA | 3.x | Database ORM |
| Hibernate | 6.x | JPA implementation |
| MySQL Connector/J | 8.1.0 | Database driver |
| JJWT (io.jsonwebtoken) | 0.12.5 | JWT generation & validation |
| BCrypt | (Spring Security built-in) | Password hashing |
| Maven | 3.x | Build tool |

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| Angular | 17.3 | SPA framework |
| TypeScript | 5.3 | Language |
| Bootstrap | 5.3.3 | CSS framework |
| Font Awesome | 4.7.0 | Icons |
| Angular Material | 17.3 | UI components |
| ngx-pagination | 6.0.3 | Pagination |
| RxJS | 7.8 | Reactive programming |
| jQuery | 3.7.1 | DOM utilities |

---

## 📁 Project Structure

```
MediConnect/
├── README.md                              ← You are here
├── MediConnect_TestCases.xlsx             ← 96 test cases across 8 test levels
├── generate_testcases.py                  ← Script to regenerate test case Excel
│
├── HealthcareManagement-Backend/          ← Spring Boot REST API
│   ├── pom.xml
│   └── src/main/java/com/application/
│       ├── HealthcareManagementBackendApplication.java  ← Entry point
│       ├── config/
│       │   └── SecurityConfig.java        ← JWT + CORS + Role rules
│       ├── controller/
│       │   ├── AdminController.java       ← POST /loginadmin, /registeradmin
│       │   ├── DoctorController.java      ← Slots, appointments, prescriptions
│       │   ├── LoginController.java       ← POST /loginuser, /logindoctor
│       │   ├── RegistrationController.java← POST /registerUser, /registerdoctor
│       │   ├── UserController.java        ← Booking, profile, prescriptions
│       │   └── FeedbackController.java
│       ├── filter/
│       │   └── JwtFilter.java             ← Intercepts every request, validates JWT
│       ├── model/                         ← JPA Entities + Response DTOs
│       │   ├── User.java                  ← 'user' table
│       │   ├── Doctor.java                ← 'doctor' table
│       │   ├── Admin.java                 ← 'admin' table
│       │   ├── Appointments.java          ← 'appointments' table
│       │   ├── Slots.java                 ← 'slots' table
│       │   ├── Prescription.java          ← 'prescription' table
│       │   ├── UserAuthResponse.java      ← Login/register response for users
│       │   ├── DoctorAuthResponse.java    ← Login/register response for doctors
│       │   └── AdminAuthResponse.java     ← Login response for admin (4 fields)
│       ├── repository/                    ← Spring Data JPA interfaces
│       ├── service/                       ← Business logic layer
│       └── util/
│           └── JwtUtils.java              ← JWT create, parse, validate
│   └── src/main/resources/
│       ├── application.properties         ← DB config, port 8081
│       └── data.sql                       ← Seed data + admin BCrypt hash
│
└── HealthcareManagement-Frontend/         ← Angular SPA
    ├── package.json
    ├── angular.json
    └── src/app/
        ├── app-routing.module.ts          ← All URL → Component + Guard mappings
        ├── app.module.ts                  ← Root module, interceptor registration
        ├── components/                    ← 20+ page components
        │   ├── welcomepage/               ← Public landing page
        │   ├── login/                     ← 3-tab login (User / Doctor / Admin)
        │   ├── registration/              ← 2-tab registration + password strength UI
        │   ├── admindashboard/
        │   ├── doctordashboard/
        │   ├── userdashboard/
        │   ├── appointments/              ← Doctor's appointment management
        │   ├── bookappointment/           ← Patient booking flow
        │   ├── scheduleslots/             ← Doctor slot creation
        │   ├── checkslots/                ← Patient slot browsing
        │   ├── addprescription/
        │   ├── prescriptionlist/
        │   ├── approvedoctors/            ← Admin approval panel
        │   └── ...
        ├── guards/
        │   ├── router.guard.ts            ← Any authenticated user
        │   ├── admin.guard.ts             ← ROLE === 'admin' only
        │   ├── doctor.guard.ts            ← ROLE === 'doctor' only
        │   ├── user.guard.ts              ← ROLE === 'user' only
        │   └── login.guard.ts             ← Redirects away if already logged in
        ├── models/                        ← TypeScript interfaces
        ├── services/
        │   ├── login.service.ts           ← Auth, localStorage session management
        │   ├── user.service.ts
        │   ├── doctor.service.ts
        │   ├── admin.service.ts
        │   └── registration.service.ts
        └── environments/
            └── environment.ts             ← apiURL: 'http://localhost:8081'
```

---

## ⚙️ Prerequisites

| Tool | Minimum Version | Check Command |
|---|---|---|
| Java JDK | 21 | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Node.js | 18+ | `node -v` |
| npm | 9+ | `npm -v` |
| Angular CLI | 17+ | `ng version` |
| MySQL Server | 8.0+ | `mysql --version` |

> **Windows users:** Use PowerShell or Command Prompt. All commands below are cross-platform.

---

## 🚀 Getting Started

### 1. Database Setup

```sql
-- Connect to MySQL
mysql -u root -p

-- Create the database
CREATE DATABASE healthcare;
USE healthcare;

-- Verify (tables are auto-created by Hibernate on first backend start)
SHOW DATABASES;
```

> ℹ️ Tables (`user`, `doctor`, `admin`, `appointments`, `slots`, `prescription`) are created automatically by Hibernate (`spring.jpa.hibernate.ddl-auto=update`) when the backend starts for the first time.

> ℹ️ `data.sql` seeds sample data and the default admin account on every startup (uses `INSERT IGNORE` to prevent duplicates).

---

### 2. Backend Setup

```bash
# Navigate to backend directory
cd HealthcareManagement-Backend

# (Optional) Edit database credentials if different from defaults
# File: src/main/resources/application.properties
#   spring.datasource.username=root
#   spring.datasource.password=root

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

**Expected output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
...
Started HealthcareManagementBackendApplication in 4.2 seconds
```

✅ Backend is running at **http://localhost:8081**

**Verify:**
```bash
curl http://localhost:8081/
# Expected: Welcome to HealthCare Management system !!!
```

---

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd HealthcareManagement-Frontend

# Install dependencies (only needed once, or after pulling updates)
npm install

# Start the development server
ng serve
# OR
npm start
```

**Expected output:**
```
✔ Compiled successfully.
Watch mode enabled. Watching for file changes...
  ➜  Local:   http://localhost:4200/
```

✅ Frontend is running at **http://localhost:4200**

Open your browser at [http://localhost:4200](http://localhost:4200) to see the welcome page.

---

## 🔐 Default Credentials

> ⚠️ **Change these in production!**

### Admin Account (seeded by `data.sql`)

| Field | Value |
|---|---|
| Email | `admin@mediconnect.com` |
| Password | `Admin@123` |
| Role | `admin` |

### Sample Seeded Doctors (from `data.sql`, **not BCrypt-hashed** — for demo only)

| Email | Password | Specialization |
|---|---|---|
| `dr.smith@hospital.com` | `pass123` | Cardiology |
| `m.patel@health.com` | `doctor789` | Dermatology |
| `emily.white@med.com` | `neuro321` | Neurology |

> **Note:** Self-registered users/doctors via the UI will have proper BCrypt-hashed passwords.

---

## 🌐 API Reference

Base URL: `http://localhost:8081`

### 🔓 Public Endpoints (no token required)

| Method | Endpoint | Description | Response |
|---|---|---|---|
| `GET` | `/` | Health check | `"Welcome to HealthCare Management system !!!"` |
| `POST` | `/registerUser` | Register new patient | `UserAuthResponse` |
| `POST` | `/registeruser` | Same as above (alias) | `UserAuthResponse` |
| `POST` | `/registerdoctor` | Register new doctor | `DoctorAuthResponse` |
| `POST` | `/registeradmin` | Register new admin | `Admin` |
| `POST` | `/loginuser` | Patient login | `UserAuthResponse` |
| `POST` | `/logindoctor` | Doctor login | `DoctorAuthResponse` |
| `POST` | `/loginadmin` | Admin login | `AdminAuthResponse` |
| `GET` | `/doctorlist` | All doctors | `Doctor[]` |
| `GET` | `/slotDetails/{email}` | Slots for a doctor | `Slots[]` |
| `GET` | `/slotDetailsWithUniqueDoctors` | Unique doctor names with slots | `Set<String>` |
| `GET` | `/slotDetailsWithUniqueSpecializations` | Unique specializations | `Set<String>` |
| `GET` | `/gettotaldoctors` | Doctor count | `[N]` |
| `GET` | `/gettotalusers` | User count | `[N]` |
| `GET` | `/gettotalpatients` | Appointment count | `[N]` |
| `GET` | `/gettotalappointments` | Appointment count | `[N]` |
| `GET` | `/gettotalprescriptions` | Prescription count | `[N]` |
| `GET` | `/gettotalslots` | Slot count | `[N]` |

---

### 🔴 Admin-Only Endpoints (`Authorization: Bearer <admin_token>`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/userlist` | All registered patients |
| `GET` | `/patientlist` | All appointment records |
| `GET` | `/acceptstatus/{email}` | Approve a doctor |
| `GET` | `/rejectstatus/{email}` | Reject a doctor |
| `POST` | `/addDoctor` | Add doctor directly (bypasses pending) |
| `GET` | `/approvedoctors` | (Resolved by frontend via `/doctorlist`) |

---

### 🟢 Doctor-Only Endpoints (`Authorization: Bearer <doctor_token>`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/addBookingSlots` | Create AM/Noon/PM slots for a date |
| `PUT` | `/updateAppointmentStatus/{id}/{status}` | Accept or reject appointment (`accept`/`reject`) |
| `GET` | `/patientlistbydoctoremail/{email}` | All appointments for this doctor |
| `GET` | `/patientlistbydoctoremailanddate/{email}` | Today's appointments only |
| `POST` | `/addPrescription` | Write a prescription |
| `GET` | `/doctorProfileDetails/{email}` | Doctor profile data |
| `PUT` | `/updatedoctor` | Update doctor profile |

---

### 🟡 User-Only Endpoints (`Authorization: Bearer <user_token>`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/bookNewAppointment` | Book an appointment (synchronized, prevents double-booking) |
| `GET` | `/patientlistbyemail/{email}` | Patient's own appointments |
| `GET` | `/profileDetails/{email}` | Patient profile |
| `PUT` | `/updateuser` | Update profile (password preserved) |
| `GET` | `/getprescriptionbyname/{patientname}` | Patient's prescriptions |

---

### Response DTOs

<details>
<summary><b>UserAuthResponse</b> (8 fields)</summary>

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "email": "john@test.com",
  "username": "John Doe",
  "role": "user",
  "gender": "male",
  "age": "28",
  "mobile": "9876543210",
  "address": "123 Street, City"
}
```
</details>

<details>
<summary><b>DoctorAuthResponse</b> (11 fields)</summary>

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "email": "drsmith@test.com",
  "doctorname": "Dr. Smith",
  "role": "doctor",
  "gender": "male",
  "specialization": "Cardiology",
  "experience": "5 years",
  "previoushospital": "City Hospital",
  "mobile": "9876543210",
  "address": "Hospital Lane",
  "status": "pending"
}
```
</details>

<details>
<summary><b>AdminAuthResponse</b> (4 fields)</summary>

```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "email": "admin@mediconnect.com",
  "adminname": "Super Admin",
  "role": "admin"
}
```
</details>

---

## 🔒 Security Architecture

### JWT Flow

```
┌─────────────────────────────────────────────────────────────┐
│  1. Login Request                                           │
│     POST /loginuser  { email, password }                    │
│                                                             │
│  2. Backend verifies BCrypt hash, issues JWT                │
│     sub = email, exp = now + 10 hours, alg = HS384          │
│     secret = "examly_secure_secret_key_..." (32+ bytes)     │
│                                                             │
│  3. Angular stores in localStorage                          │
│     TOKEN = "Bearer eyJhbGci..."                            │
│     ROLE  = "user" / "doctor" / "admin"                     │
│     USER  = "john@test.com"                                 │
│                                                             │
│  4. Every subsequent request                                │
│     AuthInterceptor injects:                                │
│     Authorization: Bearer eyJhbGci...                       │
│                                                             │
│  5. Backend JwtFilter                                       │
│     - Strips "Bearer " prefix                               │
│     - Calls JwtUtils.extractUsername() → gets email         │
│     - Calls UserRegistrationService.loadUserByEmail()       │
│       → checks User / Doctor / Admin tables                 │
│       → assigns ROLE_USER / ROLE_DOCTOR / ROLE_ADMIN        │
│     - Calls JwtUtils.validateToken() (signature + expiry)   │
│     - Sets SecurityContextHolder                            │
│                                                             │
│  6. SecurityConfig .hasRole() check                         │
│     PASS → Controller runs                                  │
│     FAIL → 403 Forbidden                                    │
└─────────────────────────────────────────────────────────────┘
```

### Password Security

- Passwords are hashed with **BCrypt** (work factor 10) before being stored
- The plain-text password is **never stored** anywhere in the database
- Profile update (`PUT /updateuser`) **explicitly preserves** the BCrypt hash — the password field from the request body is ignored
- BCrypt `matches()` is timing-safe — prevents timing attacks

### Frontend Guards

| Guard | Protects | Checks |
|---|---|---|
| `RouterGuard` | All dashboard routes | `USER` + `ROLE` exist in localStorage |
| `AdminGuard` | `/addDoctor`, `/approvedoctors` | `ROLE === 'admin'` |
| `DoctorGuard` | `/appointments`, `/scheduleslots`, `/addprescription`, `/editdoctorprofile` | `ROLE === 'doctor'` |
| `UserGuard` | `/bookappointment`, `/checkslots`, `/prescriptionlist`, `/edituserprofile` | `ROLE === 'user'` |
| `LoginGuard` | `/login` | Blocks access if already logged in |

---

## 🗄️ Database Schema

### `user` table
| Column | Type | Notes |
|---|---|---|
| `email` | VARCHAR (PK) | Primary key — cannot be changed |
| `username` | VARCHAR | Display name |
| `password` | VARCHAR | BCrypt hash (`$2a$10$...`) |
| `gender` | VARCHAR | |
| `age` | VARCHAR | |
| `mobile` | VARCHAR | |
| `address` | VARCHAR | |

### `doctor` table
| Column | Type | Notes |
|---|---|---|
| `email` | VARCHAR (PK) | |
| `doctorname` | VARCHAR | |
| `password` | VARCHAR | BCrypt hash |
| `specialization` | VARCHAR | |
| `experience` | VARCHAR | |
| `previoushospital` | VARCHAR | |
| `status` | VARCHAR | `pending` → `accept` / `reject` |
| `gender` | VARCHAR | |
| `mobile` | VARCHAR | |
| `address` | VARCHAR | |

### `admin` table
| Column | Type | Notes |
|---|---|---|
| `email` | VARCHAR (PK) | |
| `adminname` | VARCHAR | |
| `password` | VARCHAR | BCrypt hash |

### `slots` table
| Column | Type | Notes |
|---|---|---|
| `id` | INT (PK, AUTO) | |
| `email` | VARCHAR | Doctor's email |
| `doctorname` | VARCHAR | Used to link with appointments |
| `specialization` | VARCHAR | |
| `date` | VARCHAR | Format: `yyyy-MM-dd` |
| `amslot` | VARCHAR | Label (e.g. "9:00 AM") |
| `amstatus` | VARCHAR | `available` / `pending` / `booked` |
| `noonslot` | VARCHAR | Label |
| `noonstatus` | VARCHAR | `available` / `pending` / `booked` |
| `pmslot` | VARCHAR | Label |
| `pmstatus` | VARCHAR | `available` / `pending` / `booked` |

### `appointments` table
| Column | Type | Notes |
|---|---|---|
| `id` | INT (PK, AUTO) | Used in `/updateAppointmentStatus/{id}` |
| `patientid` | VARCHAR | Random 12-char ID, set after booking |
| `patientname` | VARCHAR | |
| `email` | VARCHAR | Patient's email |
| `doctorname` | VARCHAR | |
| `date` | VARCHAR | |
| `slot` | VARCHAR | `AM slot` / `Noon slot` / `PM slot` |
| `appointmentstatus` | VARCHAR | `pending` → `accepted` / `rejected` |

### `prescription` table
| Column | Type | Notes |
|---|---|---|
| `id` | INT (PK, AUTO) | |
| `patientid` | VARCHAR | Linked from appointments |
| `patientname` | VARCHAR | |
| `doctorname` | VARCHAR | |
| `diagnosis` | VARCHAR | |
| `medicine` | VARCHAR | |
| `dosage` | VARCHAR | |
| `notes` | VARCHAR | |
| `date` | VARCHAR | Auto-set to today's date by server |

---

## 🔄 Key Workflows

### Doctor Onboarding

```
Doctor fills registration form
    │
    ▼
POST /registerdoctor → status = 'pending'
    │
    ▼
Admin logs in → navigates to /approvedoctors
    │
    ├── Click Accept → GET /acceptstatus/{email} → status = 'accept'
    │                   Doctor can now use the app
    │
    └── Click Reject → GET /rejectstatus/{email} → status = 'reject'
```

### Appointment Booking

```
Doctor: POST /addBookingSlots
  → Creates slots row: amstatus='available', noonstatus='available', pmstatus='available'

Patient: POST /bookNewAppointment (synchronized)
  → Checks slot is 'available' (not 'booked' or 'pending')
  → Saves Appointments record with appointmentstatus='pending'
  → Updates slot status to 'pending' (holds the slot)
  → Generates random 12-char patientID

Doctor: PUT /updateAppointmentStatus/{id}/accept
  → appointmentstatus → 'accepted'
  → slot status → 'booked' (permanent)

Doctor: PUT /updateAppointmentStatus/{id}/reject
  → appointmentstatus → 'rejected'
  → slot status → 'available' (FREED for other patients)
```

### Prescription Writing

```
Doctor: POST /addPrescription { patientname, diagnosis, medicine, dosage, notes }
  → Server looks up patientID from appointments table by patientname
  → Server sets date = today (yyyy-MM-dd) — client cannot fake the date
  → Saves prescription

Patient: GET /getprescriptionbyname/{patientname}
  → Returns all prescriptions for that patient name
```

---

## 🧪 Testing

The project includes **96 test cases** across 8 test levels, documented in `MediConnect_TestCases.xlsx`.

| Level | Count | When to Run |
|---|---|---|
| 🔵 Smoke (TC-S01–07) | 7 | Every build — servers start, pages load |
| 🟡 Sanity (TC-SA01–07) | 7 | After smoke — core auth + DTOs work |
| 🟢 Unit (TC-U01–35) | 35 | Per feature — one endpoint or rule at a time |
| 🔴 Integration (TC-I01–11) | 11 | Multi-component flows |
| 🔁 Regression (TC-R01–15) | 15 | After every commit |
| 🌐 System (TC-SY01–09) | 9 | Before every release, via browser UI |
| ⚡ Performance (TC-P01–04) | 4 | Periodically, before release |
| ❌ Negative (TC-N01–08) | 8 | Before every release |

**Regenerate the Excel file:**
```bash
pip install openpyxl
python generate_testcases.py
```

### Postman Quick-Start

1. Import the base URL: `http://localhost:8081`
2. Create environment variables: `user_token`, `doctor_token`, `admin_token`
3. After each login, run in the **Tests** tab:
   ```javascript
   pm.environment.set('user_token', 'Bearer ' + pm.response.json().token);
   ```
4. Set `Authorization: Bearer {{user_token}}` on protected requests

---

## 📦 Build & Deploy

### Backend — Production JAR

```bash
cd HealthcareManagement-Backend
mvn clean package -DskipTests
# Output: target/HealthcareManagement-Backend-0.0.1-SNAPSHOT.jar

java -jar target/HealthcareManagement-Backend-0.0.1-SNAPSHOT.jar
```

### Frontend — Production Build

```bash
cd HealthcareManagement-Frontend
ng build --configuration production
# Output: dist/health-care-management/
```

Deploy the `dist/` folder to any static file server (Nginx, Apache, S3, Netlify).

**Update the API URL for production:**  
Edit `src/environments/environment.prod.ts`:
```typescript
export const environment = {
  production: true,
  apiURL: 'https://your-backend-domain.com'  // Change this
};
```

---

## 🐛 Troubleshooting

<details>
<summary><b>❌ Backend fails to start — DataSource connection error</b></summary>

**Symptom:** `Communications link failure` or `Access denied for user 'root'`

**Fix:**
1. Ensure MySQL service is running: `mysql -u root -p`
2. Check credentials in `application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root
   ```
3. Create the `healthcare` database if it doesn't exist:
   ```sql
   CREATE DATABASE healthcare;
   ```
</details>

<details>
<summary><b>❌ Frontend gets CORS error in browser console</b></summary>

**Symptom:** `Access to XMLHttpRequest blocked by CORS policy`

**Fix:**
1. Ensure the backend is running on port 8081
2. The `SecurityConfig.corsConfigurationSource()` already allows all origins
3. If you changed the backend port, update `src/environments/environment.ts`:
   ```typescript
   export const environment = {
     production: false,
     apiURL: 'http://localhost:8081'  // Match your backend port
   };
   ```
</details>

<details>
<summary><b>❌ 401 Unauthorized on all protected endpoints</b></summary>

**Symptom:** Every API call returns 401 even after login

**Check:**
1. Open DevTools → Application → Local Storage → check `TOKEN` key exists
2. Value must be: `Bearer eyJhbGci...` (with "Bearer " prefix and a space)
3. If TOKEN is missing, the login service's `map()` operator isn't storing it — check `login.service.ts`
4. If TOKEN exists but 401 still happens, the JWT may be expired (10-hour limit)
</details>

<details>
<summary><b>❌ 403 Forbidden on a specific endpoint</b></summary>

**Symptom:** You're logged in but a specific API call returns 403

**Check:**
1. Verify `ROLE` in localStorage matches the endpoint's requirement
2. Example: `/userlist` requires `ROLE_ADMIN` — a `ROLE=user` token gets 403
3. If role is correct, check `UserRegistrationService.loadUserByEmail()` — it must find the email in the right table and assign the right authority
</details>

<details>
<summary><b>❌ Doctor login returns 401 even with correct password</b></summary>

**Symptom:** Doctor seeded via `data.sql` cannot log in

**Cause:** `data.sql` sample doctors have plain-text passwords (not BCrypt hashes)

**Fix:** Use a doctor registered via the Angular UI (`/registration` → Doctor tab), which properly BCrypt-encodes the password on the backend
</details>

<details>
<summary><b>❌ ng serve fails with "Cannot find module" or "Module not found"</b></summary>

**Fix:**
```bash
cd HealthcareManagement-Frontend
rm -rf node_modules package-lock.json
npm install
ng serve
```
</details>

<details>
<summary><b>❌ Port 8081 already in use</b></summary>

**Windows:**
```powershell
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

Or change the port in `application.properties`:
```properties
server.port = 8082
```
Then update `environment.ts` accordingly.
</details>

---

## 🤝 Contributing

1. **Fork** the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Make changes + add tests in `MediConnect_TestCases.xlsx`
4. Run regression tests (TC-R01–R15) before committing
5. Commit: `git commit -m "feat: describe your change"`
6. Push: `git push origin feature/my-feature`
7. Open a **Pull Request** against `main`

### Branch Naming Convention
- `feature/` — new features
- `fix/` — bug fixes
- `chore/` — config, dependencies, scripts
- `docs/` — documentation only

---

<div align="center">

**MediConnect** · Built with ❤️ · Spring Boot + Angular + MySQL

*Star ⭐ this repo if it helped you!*

</div>
