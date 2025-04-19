package com.project.resumevalidation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.resumevalidation.dto.UserLoginProjection;
import com.project.resumevalidation.entity.UserTable;

@Repository
public interface OnboardRepository extends JpaRepository<UserTable, String> {
	
	Optional<UserLoginProjection> findByGmail(String userGmail);

}
