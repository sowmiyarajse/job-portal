# Complete Data Flow: Frontend ↔ Backend ↔ Database

## 1. USER SUBMITS APPLICATION FORM

```
┌─────────────────────────────────────────────────────────────┐
│           FRONTEND - application.html                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Form Fields (User fills):                                 │
│  ├─ First Name: Rajesh                                    │
│  ├─ Last Name: Kumar                                       │
│  ├─ Email: rajesh@example.com                             │
│  ├─ Phone: 9876543210                                      │
│  ├─ Location: Chennai                                      │
│  ├─ Experience: 6                                          │
│  ├─ Job Title: Senior Java Developer                      │
│  ├─ Cover Letter: (optional text)                          │
│  └─ Resume File: resume.pdf ⬅️ KEY FILE                   │
│                                                             │
│  JavaScript Code:                                          │
│  ├─ Validates all required fields                          │
│  ├─ Creates FormData object                                │
│  ├─ Attaches resume file to FormData                       │
│  └─ Sends POST request to backend                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
                          ↓
                    HTTP Request
                          ↓
```

## 2. BACKEND RECEIVES & PROCESSES

```
┌──────────────────────────────────────────────────────────────┐
│   BACKEND - Spring Boot Server (localhost:8080)             │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ApplicationController.java                                │
│  ├─ Endpoint: POST /api/applications/submit                 │
│  └─ Receives:                                              │
│     • firstName, lastName, email, phone                    │
│     • location, experience, jobTitle                       │
│     • coverLetter, company, position                       │
│     • resume file (MultipartFile)                          │
│                                                              │
│  Validations Performed:                                     │
│  ├─ ✓ Email format check                                   │
│  ├─ ✓ Phone number format (10 digits or +91)              │
│  ├─ ✓ Required fields check                               │
│  └─ ✓ File size validation (max 5MB)                       │
│                                                              │
│  ApplicationService.java                                   │
│  ├─ Saves resume file to disk:                             │
│  │  └─ backend/uploads/UUID.pdf                           │
│  │     (e.g., 550e8400-e29b-41d4-a716-446655440000.pdf)  │
│  │                                                          │
│  └─ Creates Application object:                            │
│     ├─ firstName: "Rajesh"                                 │
│     ├─ lastName: "Kumar"                                   │
│     ├─ email: "rajesh@example.com"                         │
│     ├─ resumeFileName: "resume.pdf" (original name)        │
│     └─ resumeFilePath: "550e8400...pdf" (stored name)     │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                          ↓
                   Database Save
                          ↓
```

## 3. DATABASE STORAGE & RETRIEVAL

```
┌──────────────────────────────────────────────────────────────┐
│    MYSQL DATABASE - job_portal                              │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  applications table:                                        │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ id │ first_name │ last_name │ email         │ phone    │ │
│  ├────────────────────────────────────────────────────────┤ │
│  │ 1  │ Rajesh     │ Kumar     │ rajesh@ex.com │ 987654.. │ │
│  │ 2  │ Priya      │ Singh     │ priya@ex.com  │ 876543.. │ │
│  │ 3  │ Amit       │ Patel     │ amit@ex.com   │ 765432.. │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ location │ experience │ company │ resume_file_name    │ │
│  ├────────────────────────────────────────────────────────┤ │
│  │ Chennai  │ 6          │ HCL     │ resume.pdf          │ │
│  │ Bangalore│ 4          │ TCS     │ resume.pdf          │ │
│  │ Mumbai   │ 5          │ Infosys │ cv.docx             │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ resume_file_path               │ created_at            │ │
│  ├────────────────────────────────────────────────────────┤ │
│  │ 550e8400-e29b-41d4-a716-4466.. │ 2024-02-21 10:30:00  │ │
│  │ 660e8400-e29b-41d4-a716-4466.. │ 2024-02-21 11:15:00  │ │
│  │ 770e8400-e29b-41d4-a716-4466.. │ 2024-02-21 11:45:00  │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                          ↓
                 Backend Response
                          ↓
```

## 4. BACKEND RESPONSE & FRONTEND REDIRECT

```
┌──────────────────────────────────────────────────────────────┐
│    BACKEND RESPONSE (JSON)                                   │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  {                                                           │
│    "success": true,                                         │
│    "message": "Application submitted successfully",         │
│    "applicationId": 1,                                      │
│    "resumePath": "550e8400-e29b-41d4-a716-4466..."         │
│  }                                                           │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                          ↓
              Frontend JavaScript Receives
                          ↓
      Redirects to: application-success.html
            with parameters:
            ?company=HCL&job=Developer&appId=1
                          ↓
```

## 5. FILE SYSTEM STORAGE

```
c:\Users\rajas\OneDrive\Desktop\portal\
│
├── frontend files
│   ├── index.html
│   ├── application.html (UPDATED)
│   ├── dashboard.html
│   ├── application-success.html
│   └── style.css
│
└── backend/
    ├── uploads/  ⬅️ RESUME FILES STORED HERE
    │   ├── 550e8400-e29b-41d4-a716-446655440000.pdf
    │   ├── 660e8400-e29b-41d4-a716-446655440001.pdf
    │   └── 770e8400-e29b-41d4-a716-446655440002.pdf
    │
    ├── src/main/java/
    │   └── com/jobportal/
    │       ├── controller/
    │       ├── service/
    │       ├── model/
    │       └── repository/
    │
    ├── pom.xml (Dependencies)
    └── src/main/resources/
        └── application.properties (Config)
```

---

## COMPLETE REQUEST-RESPONSE CYCLE

### Step 1: Form Submission
```javascript
// application.html JavaScript
const formData = new FormData();
formData.append('firstName', 'Rajesh');
formData.append('lastName', 'Kumar');
formData.append('email', 'rajesh@example.com');
formData.append('phone', '9876543210');
formData.append('location', 'Chennai');
formData.append('experience', 6);
formData.append('company', 'HCL');
formData.append('position', 'Senior Java Developer');
formData.append('resume', fileObject);  // The PDF/DOC file

fetch('http://localhost:8080/api/applications/submit', {
    method: 'POST',
    body: formData
})
```

### Step 2: Backend Processing
```java
// ApplicationController.java
@PostMapping("/submit")
public ResponseEntity<?> submitApplication(
    @RequestParam("firstName") String firstName,
    // ... other parameters
    @RequestParam(value = "resume") MultipartFile resumeFile
) {
    // 1. Validate inputs
    // 2. Save file to disk using ApplicationService
    // 3. Create Application entity
    // 4. Save to MySQL database
    // 5. Return JSON response
}
```

### Step 3: Database Record
```sql
-- MySQL job_portal database
-- New record automatically created:
INSERT INTO applications 
(first_name, last_name, email, phone, location, experience, 
 company, position, resume_file_name, resume_file_path, created_at)
VALUES 
('Rajesh', 'Kumar', 'rajesh@example.com', '9876543210', 'Chennai', 6,
 'HCL', 'Senior Java Developer', 'resume.pdf', 
 '550e8400-e29b-41d4-a716-446655440000.pdf', NOW());
```

### Step 4: File Stored
```
Directory: backend/uploads/
File: 550e8400-e29b-41d4-a716-446655440000.pdf
Size: ~2.5 MB
```

### Step 5: Frontend Receives Response
```json
{
  "success": true,
  "message": "Application submitted successfully",
  "applicationId": 1,
  "resumePath": "550e8400-e29b-41d4-a716-446655440000.pdf"
}
```

### Step 6: Redirect to Success Page
```javascript
window.location.href = 
  'application-success.html?company=HCL&job=Senior Java Developer&appId=1'
```

---

## RETRIEVING DATA FROM DATABASE

### Get All Applications
```
GET /api/applications
```
Response: Array of all applications with resume paths

### Query in MySQL
```sql
SELECT * FROM applications 
ORDER BY created_at DESC;

-- Get resume file path
SELECT id, first_name, last_name, resume_file_path 
FROM applications 
WHERE id = 1;

-- File location: backend/uploads/{resume_file_path}
-- Download and give to user!
```

---

## SECURITY & BEST PRACTICES

✓ **File Validation**
- UUID naming prevents filename conflicts
- Original filename stored separately in DB
- File size limit: 5MB

✓ **Database Integrity**
- Required fields enforced
- Email/Phone format validation
- Timestamps recorded automatically

✓ **CORS Configuration**
- Frontend can communicate with backend
- Configured in JobPortalApplication.java

✓ **Error Handling**
- Invalid input → 400 Bad Request
- File upload error → 500 Internal Error
- Database error → 500 with message

---

## SUMMARY

| Component | Technology | Storage | Access |
|-----------|-----------|---------|--------|
| Form Input | HTML/JavaScript | Browser Memory | User fills form |
| Resume File | MultipartFile | / backend/uploads/ | Disk file system |
| Application Data | Java Objects | MySQL database | SQL queries |
| Response | JSON | HTTP Response | JavaScript fetch |
| Metadata | Database Records | job_portal table | JPA Repository |

**Complete end-to-end resume upload system ready! 🚀**
