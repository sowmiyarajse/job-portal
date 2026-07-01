package com.jobportal.service;

import com.jobportal.model.Application;
import com.jobportal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Save application with resume file
     */
    public Application saveApplication(Application application, MultipartFile resumeFile) throws IOException {
        if (resumeFile != null && !resumeFile.isEmpty()) {
            String fileName = saveResumeFile(resumeFile);
            application.setResumeFileName(resumeFile.getOriginalFilename());
            application.setResumeFilePath(fileName);
        }

        application.setCreatedAt(LocalDateTime.now());
        application.setApplicationDate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    /**
     * Save resume file to disk
     */
    private String saveResumeFile(MultipartFile file) throws IOException {
        // Create uploads directory if not exists
        File uploadsFolder = new File(uploadDir);
        if (!uploadsFolder.exists()) {
            uploadsFolder.mkdirs();
        }

        // Generate unique filename
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = Paths.get(uploadDir + uniqueFileName);
        Files.write(filePath, file.getBytes());

        return uniqueFileName;
    }

    /**
     * Get all applications
     */
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    /**
     * Get application by ID
     */
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    /**
     * Get applications by email
     */
    public List<Application> getApplicationsByEmail(String email) {
        return applicationRepository.findByEmail(email);
    }

    /**
     * Get applications by company
     */
    public List<Application> getApplicationsByCompany(String company) {
        return applicationRepository.findByCompany(company);
    }

    /**
     * Update application
     */
    public Application updateApplication(Long id, Application application) {
        Application existingApp = getApplicationById(id);
        if (existingApp != null) {
            existingApp.setFirstName(application.getFirstName());
            existingApp.setLastName(application.getLastName());
            existingApp.setEmail(application.getEmail());
            existingApp.setPhone(application.getPhone());
            existingApp.setLocation(application.getLocation());
            existingApp.setExperience(application.getExperience());
            existingApp.setJobTitle(application.getJobTitle());
            existingApp.setCoverLetter(application.getCoverLetter());
            existingApp.setCompany(application.getCompany());
            existingApp.setPosition(application.getPosition());

            return applicationRepository.save(existingApp);
        }
        return null;
    }

    /**
     * Delete application
     */
    public boolean deleteApplication(Long id) {
        Application app = getApplicationById(id);
        if (app != null) {
            // Delete resume file if exists
            if (app.getResumeFilePath() != null) {
                try {
                    Path filePath = Paths.get(uploadDir + app.getResumeFilePath());
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            applicationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
