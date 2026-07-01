# 🚀 QUICK START GUIDE - Job Portal

## 5 MINUTES TO GET RUNNING

### ⚡ STEP 1: Install Prerequisites (One-time)
```powershell
# Download & Install (choose one):
# Java JDK 11+: https://www.oracle.com/java/technologies/downloads/
# Maven: https://maven.apache.org/download.cgi
# MySQL: https://dev.mysql.com/downloads/mysql/

# Verify installation
java -version
mvn -version
mysql --version
```

### 📁 STEP 2: Create Database
```powershell
# Open MySQL
mysql -u root -p

# Run this:
CREATE DATABASE job_portal;
EXIT;
```

### 🔧 STEP 3: Start Backend (Terminal 1)
```powershell
cd c:\Users\rajas\OneDrive\Desktop\portal\backend
mvn spring-boot:run
```

Wait for: `Tomcat started on port(s): 8080`

### 🌐 STEP 4: Open Frontend (Terminal 2)
```powershell
cd c:\Users\rajas\OneDrive\Desktop\portal
start index.html
```

### ✅ STEP 5: Test
1. Login with any credentials → Dashboard
2. Click "Apply Now" → Fill form + upload resume
3. Submit → Check success page

---

## 📊 WHERE DOES RESUME GO?

**Files saved to:** `backend/uploads/` 
**Database:** MySQL `job_portal` table
**API:** `http://localhost:8080/api/applications`

---

## ⚠️ TROUBLESHOOTING

| Problem | Solution |
|---------|----------|
| Port 8080 already in use | `netstat -ano \| findstr :8080` → Kill process |
| MySQL not running | Start from Services → MySQL80 |
| "Cannot find Maven" | Add Maven to PATH or use full path |
| CORS error | Restart backend, check console |
| File not uploading | Check `backend/uploads/` folder exists |

---

## 📚 DOCUMENTATION FILES

- **SETUP_GUIDE.md** - Complete detailed setup
- **DATA_FLOW.md** - Visual data flow diagram
- **backend/README.md** - Backend specifics
- **backend/pom.xml** - Maven dependencies

---

## 🔗 IMPORTANT URLs

| Service | URL |
|---------|-----|
| Frontend (HTML) | `file:///c:/Users/rajas/OneDrive/Desktop/portal/index.html` |
| Backend API | `http://localhost:8080` |
| API Endpoints | `http://localhost:8080/api` |
| MySQL Database | `localhost:3306` |

---

## 📝 KEY ENDPOINTS

```
POST   /api/applications/submit       - Submit application with resume
GET    /api/applications              - Get all applications
GET    /api/applications/{id}         - Get specific application
GET    /api/applications/email/{email} - Find by email
GET    /api/applications/health       - Check backend status
```

---

## 🎯 SYSTEM ARCHITECTURE

```
┌─────────────────────┐
│   Your Browser      │
│  (HTML/JavaScript)  │
└──────────┬──────────┘
           │ HTTP/CORS
           ↓
┌─────────────────────┐
│  Spring Boot API    │ Port 8080
│  (REST Endpoints)   │
└──────────┬──────────┘
           │ JDBC
           ↓
┌─────────────────────┐
│   MySQL Database    │ Port 3306
│  (job_portal)       │
└─────────────────────┘

┌─────────────────────┐
│  File System        │
│  backend/uploads/   │ Resume PDFs
└─────────────────────┘
```

---

## ✨ FEATURES INCLUDED

✅ User registration & login
✅ Job browsing & application
✅ Resume file upload (PDF/DOC/DOCX)
✅ Application history
✅ MySQL database persistence
✅ REST API for all operations
✅ CORS enabled for frontend
✅ Input validation
✅ Error handling
✅ File management with UUID naming

---

## 🛠️ TROUBLESHOOTING CHECKLIST

- [ ] Java installed? → `java -version`
- [ ] Maven installed? → `mvn -version`
- [ ] MySQL running? → Check Services or `mysql -u root -p`
- [ ] Database created? → `USE job_portal;`
- [ ] Backend started? → Check `http://localhost:8080/api/applications/health`
- [ ] Frontend opens? → Check `index.html` accessible
- [ ] CORS working? → Check browser console for errors
- [ ] Resume uploads? → Check `backend/uploads/` folder

---

## 📞 QUICK COMMANDS

```powershell
# Start everything fresh
cd c:\Users\rajas\OneDrive\Desktop\portal\backend
mvn clean install
mvn spring-boot:run

# Check what's using port 8080
Get-NetTCPConnection -LocalPort 8080

# View backend logs
type nul > backend.log
mvn spring-boot:run >> backend.log 2>&1

# Access MySQL
mysql -u root -p
# Password: root@123
# Command: SELECT * FROM job_portal.applications;
```

---

## 🎓 LEARNING RESOURCES

- Spring Boot: https://spring.io/projects/spring-boot
- MySQL: https://dev.mysql.com/doc/
- JPA/Hibernate: https://hibernate.org/orm/
- REST APIs: https://restfulapi.net/

---

**You're all set! Follow the 5 steps above and you'll have a fully functional job portal! 🎉**

For detailed info, see **SETUP_GUIDE.md**
