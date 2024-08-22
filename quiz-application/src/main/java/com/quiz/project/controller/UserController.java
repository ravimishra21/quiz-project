package com.quiz.project.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.quiz.project.dto.JwtResponse;
import com.quiz.project.dto.MessageResponse;
import com.quiz.project.dto.UserDto;
import com.quiz.project.entity.ERole;
import com.quiz.project.entity.Role;
import com.quiz.project.entity.StoreToken;
import com.quiz.project.entity.User;
import com.quiz.project.exception.DataNotCreatedException;
import com.quiz.project.exception.UserNotFoundException;
import com.quiz.project.exception.UsernameNotFoundException;
import com.quiz.project.repository.RoleRepository;
import com.quiz.project.repository.StoreTokenRepository;
import com.quiz.project.repository.UserRepository;
import com.quiz.project.security.JwtUtils;
import com.quiz.project.service.UserService;
import com.quiz.project.serviceImpl.UserDetailsImpl;
import com.quiz.project.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StoreTokenRepository storeTokenRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {

		if (userRepository.existsByUsername(userDto.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(userDto.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		if (userRepository.existsByMobileNumber(userDto.getMobileNumber())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: This phone number is already exist"));
		}

		// Create new user's details
		User user = new User(userDto.getUsername(), userDto.getFullName(), userDto.getMobileNumber(),
				userDto.getEmail(), encoder.encode(userDto.getPassword()), userDto.getCountry(), userDto.getState(),
				userDto.getDistrict(), userDto.getCity(), userDto.getPincode(), userDto.getStatus(),
				userDto.getCreatedDate(), userDto.getUpdatedDate()

		);

		Set<String> strRoles = userDto.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MOD)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		User savedUser = userRepository.save(user);

		// Authenticate the user details
//		Authentication authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
//
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		// Generate JWT token
//		String jwt = jwtUtils.generateJwtToken(authentication);

		String jwt = null;

		if (savedUser.toString().length() > 0) {
			return ResponseEntity.ok(new MessageResponse("User registered successfully!", jwt));
		} else {
			throw new DataNotCreatedException("User is not registered !! ");
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {

		Authentication authentication = null;

		if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {

			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

		} else if (userDto.getMobileNumber() != null && userDto.getMobileNumber().matches("^[0-9]{10}$")) {

			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userDto.getMobileNumber(), userDto.getPassword()));

		} else {

			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		}

		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			String currentDateTime = DateUtils.currentDate();
			String tokenExpireDateTime = DateUtils.expireDateForToken(currentDateTime);

			StoreToken tokenDetails = new StoreToken(jwt, userDetails.getId(), currentDateTime, tokenExpireDateTime);

			Optional<StoreToken> tokenDtl = storeTokenRepository.findByUserId(userDetails.getId());

			if (tokenDtl.isEmpty()) {

				storeTokenRepository.save(tokenDetails);
			} else {
				userService.updateToken(userDetails, jwt, tokenDtl.get());
			}

			return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(),

					userDetails.getUsername(), userDetails.getFullName(), userDetails.getMobileNumber(),
					userDetails.getEmail(), userDetails.getPassword(), userDetails.getCountry(), userDetails.getState(),
					userDetails.getDistrict(), userDetails.getCity(), userDetails.getPincode(), userDetails.getStatus(),
					userDetails.getCreatedDate(), userDetails.getUpdatedDate(),

					roles));
		} else {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
		}

	}

	@GetMapping(value = "/userDetailsById/{user_id}")
	public ResponseEntity<?> getUserDetailsById(@PathVariable Integer user_id) {

		User user = userService.getUserDetailById(user_id);

		return ResponseEntity.ok().body(user);

	}

	@Transactional
	@PutMapping(value = "/updateUser/{id}")
	public ResponseEntity<User> updateUserById(@RequestBody User user, @PathVariable("id") Integer id) {

		try {
			User updatedUser = userService.updateUserById(user, id);

			return ResponseEntity.ok().body(updatedUser);

		} catch (UserNotFoundException ex) {

			throw new UserNotFoundException("This user id is not available for update !! ");
		} catch (UsernameNotFoundException ex) {

			throw new UsernameNotFoundException("This user name is not available for update !! ");
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping(value = "/deleteUser/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable("id") Integer id) {

		try {
			String deletedUser = userService.deleteUserById(id);
			return ResponseEntity.ok().body("User has been deleted successfully !! ");
		} catch (UserNotFoundException ex) {

			throw new UserNotFoundException("This user id is not available for delete !! ");
		}

		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}