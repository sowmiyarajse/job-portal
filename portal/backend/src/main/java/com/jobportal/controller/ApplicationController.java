package com.jobportal.controller;

import com.jobportal.model.Application;
import com.jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * Submit new job application with resume
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitApplication(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("location") String location,
            @RequestParam("experience") Integer experience,
            @RequestParam(value = "jobTitle", required = false) String jobTitle,
            @RequestParam(value = "coverLetter", required = false) String coverLetter,
            @RequestParam(value = "company", required = false) String company,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "resume", required = false) MultipartFile resumeFile) {

        try {
            // Validate required fields
            if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                email == null || email.isEmpty() ||
                phone == null || phone.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("All required fields must be filled"));
            }

            // Validate email format
            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Invalid email format"));
            }

            // Validate phone format
            if (!phone.matches("^(\\d{10}|\\+91\\d{10})$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Invalid phone number. Must be 10 digits or +91XXXXXXXXXX"));
            }

            // Create application object
            Application application = new Application();
            application.setFirstName(firstName);
            application.setLastName(lastName);
            application.setEmail(email);
            application.setPhone(phone);
            application.setLocation(location);
            application.setExperience(experience);
            application.setJobTitle(jobTitle);
            application.setCoverLetter(coverLetter);
            application.setCompany(company);
            application.setPosition(position);

            // Save application with resume
            Application savedApp = applicationService.saveApplication(application, resumeFile);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application submitted successfully");
            response.put("applicationId", savedApp.getId());
            response.put("resumePath", savedApp.getResumeFilePath());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error uploading resume: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error submitting application: " + e.getMessage()));
        }
    }

    /**
     * Get all applications
     */
    @GetMapping
    public ResponseEntity<?> getAllApplications() {
        try {
            List<Application> applications = applicationService.getAllApplications();
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching applications: " + e.getMessage()));
        }
    }

    /**
     * Get application by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable Long id) {
        try {
            Application application = applicationService.getApplicationById(id);
            if (application != null) {
                return ResponseEntity.ok(application);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Application not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching application: " + e.getMessage()));
        }
    }

    /**
     * Get applications by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getApplicationsByEmail(@PathVariable String email) {
        try {
            List<Application> applications = applicationService.getApplicationsByEmail(email);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching applications: " + e.getMessage()));
        }
    }

    /**
     * Get applications by company
     */
    @GetMapping("/company/{company}")
    public ResponseEntity<?> getApplicationsByCompany(@PathVariable String company) {
        try {
            List<Application> applications = applicationService.getApplicationsByCompany(company);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error fetching applications: " + e.getMessage()));
        }
    }

    /**
     * Update application
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @RequestBody Application application) {
        try {
            Application updatedApp = applicationService.updateApplication(id, application);
            if (updatedApp != null) {
                return ResponseEntity.ok(updatedApp);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Application not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error updating application: " + e.getMessage()));
        }
    }

    /**
     * Delete application
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        try {
            boolean deleted = applicationService.deleteApplication(id);
            if (deleted) {
                return ResponseEntity.ok(createSuccessResponse("Application deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Application not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error deleting application: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(createSuccessResponse("Backend is running"));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }
}
