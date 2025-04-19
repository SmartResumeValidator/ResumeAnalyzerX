package com.project.resumevalidation.service;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.resumevalidation.dto.UserLoginDto;
import com.project.resumevalidation.dto.UserLoginProjection;
import com.project.resumevalidation.repository.OnboardRepository;

@Service
public class OnboardServiceImpl implements OnboardService {
	public static final Logger LOGGER = LoggerFactory.getLogger(OnboardServiceImpl.class);

	OnboardRepository onboardRepository;
	private final PasswordEncoder passwordEncoder;

	public OnboardServiceImpl(OnboardRepository onboardRepository) {
		this.onboardRepository = onboardRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public boolean userLogin(UserLoginDto userCredentials) {
		Optional<UserLoginProjection> optionalUser = onboardRepository.findByGmail(userCredentials.getGmail());

		if (optionalUser.isEmpty()) {
			LOGGER.warn("Login failed: User not found for email {}", userCredentials.getGmail());
			return false;
		}

		UserLoginProjection user = optionalUser.get();

//		if (!passwordEncoder.matches(userCredentials.getPassword(), user.getUserPassword())) {
//			LOGGER.warn("Login failed: Incorrect password for email {}", userCredentials.getGmail());
//			return false;
//		}


		LOGGER.info("User login successful: {}", userCredentials.getGmail());
		return true;
	}
}
