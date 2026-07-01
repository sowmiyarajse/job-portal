# Testing Job Portal API

## Using cURL (Command Line)

### 1. Check Backend Health
```powershell
curl -X GET "http://localhost:8080/api/applications/health"
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Backend is running"
}
```

### 2. Submit Application with Resume

```powershell
# PowerShell example
$FilePath = "C:\path\to\resume.pdf"
$Form = @{
    firstName = "Rajesh"
    lastName = "Kumar"
    email = "rajesh@example.com"
    phone = "9876543210"
    location = "Chennai"
    experience = "6"
    jobTitle = "Senior Java Developer"
    coverLetter = "I am interested in this position"
    company = "HCL"
    position = "Senior Java Developer"
}

Invoke-WebRequest -Uri "http://localhost:8080/api/applications/submit" `
    -Method POST `
    -Form $Form `
    -Body @{Resume; resume=$FilePath}
```

### 3. Get All Applications
```powershell
curl -X GET "http://localhost:8080/api/applications"
```

### 4. Get Application by ID
```powershell
curl -X GET "http://localhost:8080/api/applications/1"
```

### 5. Get Applications by Email
```powershell
curl -X GET "http://localhost:8080/api/applications/email/rajesh@example.com"
```

### 6. Get Applications by Company
```powershell
curl -X GET "http://localhost:8080/api/applications/company/HCL"
```

### 7. Delete Application
```powershell
curl -X DELETE "http://localhost:8080/api/applications/1"
```

---

## Using Postman (GUI Tool)

### Download Postman
https://www.postman.com/downloads/

### Setup Collection

#### 1. Check Health
- **Method:** GET
- **URL:** `http://localhost:8080/api/applications/health`
- **Click Send** → Should get success response

#### 2. Submit Application
- **Method:** POST
- **URL:** `http://localhost:8080/api/applications/submit`
- **Headers:** Leave empty (Postman auto-sets Content-Type)
- **Body:** Select `form-data`

| Key | Value (select file for resume) |
|-----|---------|
| firstName | Rajesh |
| lastName | Kumar |
| email | rajesh@example.com |
| phone | 9876543210 |
| location | Chennai |
| experience | 6 |
| jobTitle | Senior Java Developer |
| coverLetter | Sample cover letter text |
| company | HCL |
| position | Senior Java Developer |
| resume | [Select your PDF/DOC file] |

- **Click Send** → Check response with applicationId

#### 3. Get All Applications
- **Method:** GET
- **URL:** `http://localhost:8080/api/applications`
- **Click Send** → View all submitted applications

#### 4. Get Specific Application
- **Method:** GET
- **URL:** `http://localhost:8080/api/applications/1`
- **Click Send** → View application details

#### 5. Delete Application
- **Method:** DELETE
- **URL:** `http://localhost:8080/api/applications/1`
- **Click Send** → Should delete the application

---

## Using Browser DevTools

### 1. Open Developer Tools
- Press F12 in browser
- Go to Console tab

### 2. Test Fetch Request
```javascript
// Check backend is running
fetch('http://localhost:8080/api/applications/health')
    .then(r => r.json())
    .then(d => console.log(d))

// Get all applications
fetch('http://localhost:8080/api/applications')
    .then(r => r.json())
    .then(d => console.log(d))

// Get application by ID
fetch('http://localhost:8080/api/applications/1')
    .then(r => r.json())
    .then(d => console.log(d))
```

### 3. Test Form Submission
```javascript
// Create FormData
const formData = new FormData();
formData.append('firstName', 'Rajesh');
formData.append('lastName', 'Kumar');
formData.append('email', 'rajesh@example.com');
formData.append('phone', '9876543210');
formData.append('location', 'Chennai');
formData.append('experience', 6);
formData.append('company', 'HCL');
formData.append('position', 'Senior Java Developer');

// Note: Can't add file from console, but you can test other endpoints

// Submit
fetch('http://localhost:8080/api/applications', {
    method: 'POST',
    body: formData
})
.then(r => r.json())
.then(d => console.log(d))
```

---

## Expected API Responses

### Success Response (Submit)
```json
{
  "success": true,
  "message": "Application submitted successfully",
  "applicationId": 1,
  "resumePath": "550e8400-e29b-41d4-a716-446655440000.pdf"
}
```

### Success Response (Get All)
```json
[
  {
    "id": 1,
    "firstName": "Rajesh",
    "lastName": "Kumar",
    "email": "rajesh@example.com",
    "phone": "9876543210",
    "location": "Chennai",
    "experience": 6,
    "jobTitle": "Senior Java Developer",
    "coverLetter": "Sample text",
    "company": "HCL",
    "position": "Senior Java Developer",
    "resumeFileName": "resume.pdf",
    "resumeFilePath": "550e8400-e29b-41d4-a716-446655440000.pdf",
    "applicationDate": "2024-02-21T10:30:00",
    "createdAt": "2024-02-21T10:30:00"
  }
]
```

### Error Response (Invalid Email)
```json
{
  "success": false,
  "message": "Invalid email format"
}
```

### Error Response (Invalid Phone)
```json
{
  "success": false,
  "message": "Invalid phone number. Must be 10 digits or +91XXXXXXXXXX"
}
```

### Error Response (Backend Down)
```
Error: Failed to fetch
(Check if backend is running at http://localhost:8080)
```

---

## Testing Workflow

### Step 1: Start Backend
```powershell
cd c:\Users\rajas\OneDrive\Desktop\portal\backend
mvn spring-boot:run
```

### Step 2: Test Health
```powershell
curl http://localhost:8080/api/applications/health
```

### Step 3: Submit Application (use Postman easier)
- Create application with resume file
- Note the `applicationId`

### Step 4: View in Database
```powershell
mysql -u root -p
# Password: root@123

use job_portal;
SELECT * FROM applications;
SELECT * FROM applications WHERE id = 1;
```

### Step 5: Check File Storage
```powershell
# List uploaded resumes
dir c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\
```

---

## Common Error Codes

| Status | Meaning | Solution |
|--------|---------|----------|
| 200 | Success | ✓ Request successful |
| 400 | Bad Request | Check form data, email/phone format |
| 404 | Not Found | Application ID doesn't exist |
| 500 | Server Error | Check backend logs, MySQL connection |
| CORS Error | Frontend blocked | Backend CORS not configured |
| Network Error | Backend down | Start backend with `mvn spring-boot:run` |

---

## Performance Testing

### Load Test Example
```powershell
# Submit multiple applications
for ($i = 1; $i -le 10; $i++) {
    $email = "user$i@example.com"
    curl -X GET "http://localhost:8080/api/applications/email/$email"
}

# In MySQL, check performance
SELECT COUNT(*) as total_applications FROM applications;
SELECT COUNT(*) as hcl_applications FROM applications WHERE company = 'HCL';
```

---

## Debugging Tips

### 1. Check Backend Logs
```powershell
# Watch live logs
mvn spring-boot:run 2>&1 | Tee-Object -FilePath backend.log

# View error details in console output
```

### 2. Check MySQL Connection
```powershell
mysql -u root -p
SHOW DATABASES;
USE job_portal;
SHOW TABLES;
DESCRIBE applications;
```

### 3. Check File System
```powershell
# Verify uploads folder exists
dir c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\

# Check file was saved
Get-Item c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\550e8400*
```

### 4. Browser DevTools
- Open F12 → Network tab
- Submit application
- View request/response details
- Check CORS headers

---

**Ready to test! Follow the workflow above and you'll verify everything is working correctly.** 🧪
