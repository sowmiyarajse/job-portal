# Backend Configuration - Quick Reference

## Key Ports
- Backend API: http://localhost:8080
- API Base Path: http://localhost:8080/api
- MySQL: localhost:3306

## Database Credentials (Default)
- Username: root
- Password: root@123
- Database: job_portal

## Application Features

### Resume Upload
- Max File Size: 5MB
- Supported Formats: .pdf, .doc, .docx
- Storage Location: backend/uploads/
- Files stored with UUID names for uniqueness

### File Upload REST Endpoint
```
POST /api/applications/submit
Content-Type: multipart/form-data

Fields:
- firstName, lastName, email, phone, location, experience (required)
- jobTitle, coverLetter, company, position (optional)
- resume (file upload, required)
```

## Database Auto-Creation
Spring Boot with `spring.jpa.hibernate.ddl-auto=update` automatically:
1. Detects missing tables
2. Creates tables based on entity classes
3. Updates schema if entity changes

No manual SQL needed!

## Validations
- Email format validation ✓
- Phone number validation (10 digits or +91XXXXXXXXXX) ✓
- Required field validation ✓
- File type validation ✓
- File size validation ✓

## CORS Configuration
Enabled for:
- Origins: http://localhost:3000, file:///*
- Methods: GET, POST, PUT, DELETE, OPTIONS
- Headers: All allowed
- Credentials: Yes

## Error Handling
All API responses follow format:
```json
{
  "success": true/false,
  "message": "Description",
  "applicationId": 1,  // if applicable
  "resumePath": "filename.pdf"  // if applicable
}
```

## Useful Maven Commands

```bash
# Clean and build
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Skip tests during build
mvn clean install -DskipTests

# Create executable JAR
mvn clean package

# Run JAR file
java -jar target/job-portal-backend-1.0.0.jar

# View all dependencies
mvn dependency:tree

# Update dependencies
mvn versions:display-dependency-updates
```

## Development Notes

1. **Entity Classes**: Models in `com.jobportal.model` package map to database tables
2. **Repository**: JpaRepository interface provides CRUD operations automatically
3. **Service Layer**: Business logic, file handling, validations
4. **Controller**: REST endpoints, request handling, response formatting
5. **CORS**: Pre-configured in JobPortalApplication.java

## Performance Considerations

- Indexed columns: id (primary key)
- Resume uploads: Stored on disk, filename references in DB
- Database queries: Optimized with JPA
- Max upload size: 5242880 bytes (5MB) - configurable in application.properties

---

**Backend is production-ready!**
