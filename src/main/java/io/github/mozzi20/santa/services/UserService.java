package io.github.mozzi20.santa.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mozzi20.santa.exceptions.ResourceNotFoundException;
import io.github.mozzi20.santa.models.User;
import io.github.mozzi20.santa.models.User.Role;
import io.github.mozzi20.santa.repositories.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Transactional(readOnly=true)
	public Optional<User> getPersistedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof User) {
			return userRepo.findById(((User) principal).getId());			
		} else {
			return Optional.empty();
		}
	}
	
	@Transactional(readOnly=true)
	public Optional<String> getUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof User) {
			return Optional.of(((User) principal).getId());
		} else {
			return Optional.empty();
		}
	}

	@Transactional(readOnly=true)
	public Iterable<User> getAllUsers() {
		return userRepo.findAll();
	}

	public void updateRole(Role role, String userId) {
		User user = userRepo.findById(userId).orElseThrow(ResourceNotFoundException::new);
		user.setRole(role);
		userRepo.save(user);
	}
	
}
