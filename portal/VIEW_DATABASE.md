# Database View Methods

## Method 1: MySQL Command Line (Fastest)

### Open MySQL Terminal
```powershell
mysql -u root -p
```
**Password:** `2004`

### View All Data
```sql
-- Show all applications
SELECT * FROM job_portal.applications;

-- Show with readable format
SELECT id, first_name, last_name, email, phone, company, resume_file_name, created_at 
FROM job_portal.applications 
ORDER BY created_at DESC;

-- Count total applications
SELECT COUNT(*) as total_applications FROM job_portal.applications;

-- Find specific application
SELECT * FROM job_portal.applications WHERE email = 'rajesh@example.com';

-- Get resume file names and paths
SELECT id, first_name, resume_file_name, resume_file_path 
FROM job_portal.applications;
```

### Exit MySQL
```sql
EXIT;
```

---

## Method 2: MySQL Workbench (Visual GUI)

### Download & Install
https://dev.mysql.com/downloads/workbench/

### Steps
1. Open MySQL Workbench
2. Double-click "Local instance MySQL80"
3. Enter password: `2004`
4. Left panel → Databases → job_portal
5. Double-click tables → applications
6. See all data in table view

---

## Method 3: Browser API Testing

### Check Backend is Running
Open browser and go to:
```
http://localhost:8080/api/applications
```

### You'll see JSON with all applications:
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
    "company": "HCL",
    "position": "Senior Java Developer",
    "resumeFileName": "resume.pdf",
    "resumeFilePath": "550e8400-e29b-41d4-a716-446655440000.pdf",
    "createdAt": "2024-02-21T10:30:00"
  }
]
```

### Get Specific Application by ID
```
http://localhost:8080/api/applications/1
```

### Search by Email
```
http://localhost:8080/api/applications/email/rajesh@example.com
```

### Search by Company
```
http://localhost:8080/api/applications/company/HCL
```

---

## Method 4: Using PowerShell

### Get All Applications
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/applications" -Method Get
$response | ConvertTo-Json | Out-Host

# Or display in table format
$response | Format-Table id, firstName, lastName, email, company
```

### Get Specific Application
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/applications/1" -Method Get
$response | ConvertTo-Json
```

### Count Applications
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/applications" -Method Get
Write-Host "Total Applications: $($response.Count)"
```

---

## Method 5: View Resume Files

### Where are they stored?
```powershell
# List all uploaded resume files
dir c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\

# View specific file
Get-Item c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\550e8400*
```

### Example Output
```
Directory: C:\Users\rajas\OneDrive\Desktop\portal\backend\uploads

Mode                 LastWriteTime         Length Name
----                 ---------------         ------ ----
-a---           2/21/2026  10:30 AM        2548576 550e8400-e29b-41d4-a716-446655440000.pdf
-a---           2/21/2026  11:15 AM        1234567 660e8400-e29b-41d4-a716-446655440001.docx
-a---           2/21/2026  11:45 AM        3456789 770e8400-e29b-41d4-a716-446655440002.pdf
```

---

## Complete Database Viewing Script

### PowerShell Script: `view-database.ps1`

Save this as file and run it:

```powershell
# View Database Script

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Job Portal Database Viewer" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Check if backend is running
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/api/applications/health" -Method Get
    Write-Host "✓ Backend Status: " -ForegroundColor Green -NoNewline
    Write-Host $health.message -ForegroundColor Green
} catch {
    Write-Host "✗ Backend is NOT running! Start it first with: mvn spring-boot:run" -ForegroundColor Red
    exit
}

Write-Host ""

# Get all applications
try {
    $apps = Invoke-RestMethod -Uri "http://localhost:8080/api/applications" -Method Get
    Write-Host "Total Applications: $($apps.Count)" -ForegroundColor Yellow
    Write-Host ""
    
    if ($apps.Count -gt 0) {
        $apps | Format-Table @(
            @{Label="ID"; Expression={$_.id}; Width=4},
            @{Label="Name"; Expression={"$($_.firstName) $($_.lastName)"}; Width=20},
            @{Label="Email"; Expression={$_.email}; Width=25},
            @{Label="Company"; Expression={$_.company}; Width=15},
            @{Label="Resume"; Expression={$_.resumeFileName}; Width=15},
            @{Label="Date"; Expression={$_.createdAt}; Width=20}
        ) -AutoSize
    } else {
        Write-Host "No applications found in database" -ForegroundColor Yellow
    }
} catch {
    Write-Host "Error fetching applications: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan

# Show resume files
Write-Host "Resume Files Location:" -ForegroundColor Cyan
Write-Host "c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\" -ForegroundColor Yellow

$uploadDir = "c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\"
if (Test-Path $uploadDir) {
    $files = Get-ChildItem $uploadDir -File
    Write-Host "Total Resume Files: $($files.Count)" -ForegroundColor Yellow
    if ($files.Count -gt 0) {
        $files | Format-Table Name, Length, LastWriteTime -AutoSize
    }
} else {
    Write-Host "Uploads folder not found" -ForegroundColor Red
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
```

---

## Quick Command Reference

### 🔍 View Everything

```powershell
# Terminal 1: Start MySQL
mysql -u root -p

# Then run these queries:
SELECT * FROM job_portal.applications;
```

### 📊 View in JSON Format

```powershell
# PowerShell
Invoke-RestMethod http://localhost:8080/api/applications -Method Get | ConvertTo-Json
```

### 📁 View File System

```powershell
# List resume files
dir c:\Users\rajas\OneDrive\Desktop\portal\backend\uploads\
```

### 🌐 View in Browser

```
http://localhost:8080/api/applications
http://localhost:8080/api/applications/1
http://localhost:8080/api/applications/email/rajesh@example.com
```

---

## Database Queries Cheat Sheet

```sql
-- Connect to database
USE job_portal;

-- View all applications with important columns
SELECT id, first_name, last_name, email, phone, company, 
       role, resume_file_name, created_at 
FROM applications 
ORDER BY created_at DESC;

-- Count applications
SELECT COUNT(*) FROM applications;

-- Count by company
SELECT company, COUNT(*) as count 
FROM applications 
GROUP BY company;

-- Count by date
SELECT DATE(created_at) as date, COUNT(*) as count 
FROM applications 
GROUP BY DATE(created_at);

-- Find user
SELECT * FROM applications WHERE email = 'user@example.com';

-- View latest 5 applications
SELECT * FROM applications ORDER BY created_at DESC LIMIT 5;

-- View resume information only
SELECT id, first_name, email, resume_file_name, resume_file_path 
FROM applications;

-- Delete old applications (be careful!)
DELETE FROM applications WHERE created_at < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- Export to CSV (MySQL to file)
SELECT * FROM applications 
INTO OUTFILE '/tmp/applications.csv' 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\n';
```

---

## Step-by-Step: View Your First Application

### Step 1: Start Backend
```powershell
cd c:\Users\rajas\OneDrive\Desktop\portal\backend
mvn spring-boot:run
```

### Step 2: Open MySQL
```powershell
mysql -u root -p
# Password: root@123
```

### Step 3: View Data
```sql
USE job_portal;
SELECT * FROM applications;
```

### Step 4: See Results
```
+----+------------+-----------+-------+-------+...
| id | first_name | last_name | email | phone |...
+----+------------+-----------+-------+-------+...
```

Done! 🎉

---

## Real Example Query Results

```
mysql> SELECT id, first_name, last_name, email, resume_file_name 
       FROM job_portal.applications;

+----+--------+-------+------------------+------------------+
| id | first_name | last_name | email       | resume_file_name |
+----+--------+-------+------------------+------------------+
|  1 | Rajesh | Kumar | rajesh@email.com | resume.pdf       |
|  2 | Priya  | Singh | priya@email.com  | resume.pdf       |
|  3 | Amit   | Patel | amit@email.com   | cv.docx          |
+----+--------+-------+------------------+------------------+

3 rows in set (0.01 sec)
```

That's it! Your data is safely stored in the database! ✅
