package com.jobportal.repository;

import com.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByEmail(String email);
    List<Application> findByCompany(String company);
    List<Application> findByPosition(String position);
}
