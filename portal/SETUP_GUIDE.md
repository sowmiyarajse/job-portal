# Job Portal - Frontend & Backend Setup Guide

## Complete Architecture Overview

```
Frontend (HTML/CSS/JavaScript)
          ↓
          ↓ HTTP Requests (CORS enabled)
          ↓
Backend (Spring Boot REST API)
          ↓
          ↓ JPA/Hibernate
          ↓
MySQL Database
          ↓
Resume Files Storage (/uploads folder)
```

---

## PART 1: DATABASE SETUP (MySQL)

### Step 1: Install MySQL
- Download from: https://dev.mysql.com/downloads/mysql/
- Install with default settings
- Remember username (default: `root`) and password (default: `root@123`)

### Step 2: Create Database
Open MySQL Command Line or MySQL Workbench and run:

```sql
CREATE DATABASE job_portal;
USE job_portal;
```

That's it! Spring Boot will automatically create all tables when the application starts.

---

## PART 2: BACKEND SETUP (Java Spring Boot)

### Prerequisites
- **Java JDK 11 or higher** installed
- **Maven 3.6+** installed
- **MySQL Server** running

### Step 1: Navigate to Backend Directory
```powershell
cd c:\Users\rajas\OneDrive\Desktop\portal\backend
```

### Step 2: Update MySQL Connection (if different from defaults)
Edit `src\main\resources\application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/job_portal
spring.datasource.username=root
spring.datasource.password=root@123
```

Change the username/password if your MySQL credentials are different.

### Step 3: Build the Project
```powershell
mvn clean install
```

This downloads all dependencies (takes 2-3 minutes on first run).

### Step 4: Run the Backend Server
```powershell
mvn spring-boot:run
```

Or:
```powershell
java -jar target/job-portal-backend-1.0.0.jar
```

**Expected Output:**
```
Job Portal Backend started successfully
Server is running on: http://localhost:8080
Database connected: job_portal
```

### Step 5: Test Backend is Running
Open browser and go to:
```
http://localhost:8080/api/applications/health
```

You should see:
```json
{
  "success": true,
  "message": "Backend is running"
}
```

---

## PART 3: FRONTEND SETUP

The frontend files are already in place:
- `application.html` - Updated with backend API calls
- `index.html` - Login page
- `dashboard.html` - Dashboard page
- `style.css` - Styling

### How the Frontend Connects to Backend

When user submits application form:

1. **Frontend JavaScript** (in application.html):
   ```javascript
   fetch('http://localhost:8080/api/applications/submit', {
       method: 'POST',
       body: formData  // Contains all form fields + resume file
   })
   ```

2. **Backend REST API** receives the request at:
   ```
   POST http://localhost:8080/api/applications/submit
   ```

3. **Spring Boot Controller** validates and:
   - Saves resume file to `backend/uploads/` folder
   - Stores application details in MySQL database
   - Returns response with application ID

4. **Frontend** redirects to success page with confirmation

---

## PART 4: RUNNING THE COMPLETE SYSTEM

### Terminal 1: Start MySQL
```powershell
# MySQL should run as a service (usually auto-starts)
# Or manually start from Services
```

### Terminal 2: Start Backend
```powershell
cd c:\Users\rajas\OneDrive\Desktop\portal\backend
mvn spring-boot:run
```

Wait for message: `Tomcat started on port(s): 8080`

### Terminal 3: Open Frontend
```powershell
# Option 1: Double-click on index.html
cd c:\Users\rajas\OneDrive\Desktop\portal
start index.html

# Option 2: Use a simple HTTP server (if you have Python)
python -m http.server 8000
# Then open: http://localhost:8000
```

---

## BACKEND API ENDPOINTS

### 1. Submit Application (Main endpoint)
```
POST /api/applications/submit
```
**Form Data:**
- firstName (required)
- lastName (required)
- email (required)
- phone (required)
- location
- experience (required)
- jobTitle
- coverLetter
- company
- position
- resume (file - required)

**Response:**
```json
{
  "success": true,
  "message": "Application submitted successfully",
  "applicationId": 1,
  "resumePath": "550e8400-e29b-41d4-a716-446655440000.pdf"
}
```

### 2. Get All Applications
```
GET /api/applications
```

### 3. Get Application by ID
```
GET /api/applications/{id}
```

### 4. Get Applications by Email
```
GET /api/applications/email/{email}
```

### 5. Get Applications by Company
```
GET /api/applications/company/{company}
```

### 6. Update Application
```
PUT /api/applications/{id}
```

### 7. Delete Application
```
DELETE /api/applications/{id}
```

---

## DATABASE SCHEMA (Auto-created)

### applications table
```sql
CREATE TABLE applications (
  id BIGINT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NOT NULL,
  location VARCHAR(255),
  experience INT,
  job_title VARCHAR(255),
  cover_letter LONGTEXT,
  company VARCHAR(255),
  position VARCHAR(255),
  resume_file_name VARCHAR(255),
  resume_file_path VARCHAR(255),
  application_date DATETIME,
  created_at DATETIME,
  PRIMARY KEY (id)
);
```

---

## WHERE ARE RESUMES STORED?

### Files Location:
```
c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\
```

### Format:
- Original filename: `resume.pdf`
- Stored as: `550e8400-e29b-41d4-a716-446655440000.pdf` (UUID + extension)

### Why UUID?
- Prevents conflicts if two people upload `resume.pdf`
- Unique mapping in database
- Original filename stored in database for reference

### Database Reference:
```sql
SELECT id, first_name, resume_file_name, resume_file_path, created_at 
FROM applications 
ORDER BY created_at DESC;
```

---

## TROUBLESHOOTING

### Problem: Backend won't start
**Solution:**
```
1. Check Java is installed: java -version
2. Check Maven is installed: mvn -version
3. Delete target folder and rebuild: mvn clean install
4. Check MySQL is running
```

### Problem: "Connection refused" error
**Solution:**
```
1. Ensure MySQL is running
2. Check credentials in application.properties
3. Verify database job_portal exists
4. Restart backend
```

### Problem: Resume not uploading
**Solution:**
```
1. Check /uploads folder exists in backend directory
2. Check file size < 5MB (configurable in application.properties)
3. Check file format is .pdf, .doc, or .docx
4. Check backend console for detailed error
```

### Problem: "CORS error" in browser
**Solution:**
- CORS is already configured in JobPortalApplication.java
- Backend allows requests from: `http://localhost:3000` and `file:///`
- If using different origin, add to `addCorsMappings()`

### Problem: Database already exists
**Solution:**
```sql
DROP DATABASE job_portal;
CREATE DATABASE job_portal;
```

---

## PROJECT STRUCTURE

```
portal/
├── frontend files (HTML, CSS, JS)
│   ├── index.html
│   ├── application.html (UPDATED - calls backend API)
│   ├── dashboard.html
│   └── style.css
│
└── backend/
    ├── pom.xml (Maven configuration with dependencies)
    ├── uploads/ (Resume files stored here)
    └── src/main/
        ├── java/com/jobportal/
        │   ├── JobPortalApplication.java
        │   ├── controller/ApplicationController.java
        │   ├── service/ApplicationService.java
        │   ├── model/Application.java
        │   └── repository/ApplicationRepository.java
        └── resources/
            └── application.properties (Database config)
```

---

## NEXT STEPS

1. **Start MySQL** - Ensure it's running
2. **Run Backend** - `mvn spring-boot:run`
3. **Open Frontend** - Open `index.html` in browser
4. **Test Flow** - Login → Browse Jobs → Apply with Resume → Check Database

---

## TECHNICAL STACK

| Layer | Technology | Version |
|-------|-----------|---------|
| Frontend | HTML5/CSS3/JavaScript | - |
| Backend | Spring Boot | 3.0.0 |
| Framework | Spring Data JPA | - |
| Database | MySQL | 8.0+ |
| Build Tool | Maven | 3.6+ |
| Java Version | JDK | 11+ |

---

## USEFUL MYSQL QUERIES

```sql
-- View all applications
SELECT * FROM applications;

-- View applications with resume details
SELECT id, first_name, last_name, email, resume_file_name, created_at 
FROM applications 
ORDER BY created_at DESC;

-- Count applications by company
SELECT company, COUNT(*) as count 
FROM applications 
GROUP BY company;

-- Find specific application
SELECT * FROM applications WHERE email = 'user@example.com';

-- Delete old applications
DELETE FROM applications WHERE created_at < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

---

## MONITORING & LOGS

Backend logs are saved to console. To redirect to file:

```powershell
mvn spring-boot:run > backend.log 2>&1
```

View logs in real-time:
```powershell
Get-Content -Path backend.log -Wait
```

---

**Your complete job portal is now ready to use!**
