package com.camera.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.camera.dto.LoginRequestDTO;
import com.camera.dto.SignUpRequestDTO;
import com.camera.entity.RefreshToken;
import com.camera.entity.UserEntity;
import com.camera.modelAPI.AuthEntryErrModel;
import com.camera.modelAPI.AuthenticationResult;
import com.camera.repo.UserRepository;
import com.camera.request.TokenRefreshRequest;
import com.camera.response.JwtResponse;
import com.camera.response.TokenRefreshResponse;
import com.camera.security.jwt.JwtUtils;
import com.camera.security.service.RefreshTokenService;
import com.camera.security.service.UserDetailsImpl;
import com.camera.service.TokenRefreshException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
public class LoginController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RefreshTokenService refreshTokenService;
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/sign-up")
	@ApiOperation(value = "Login API", notes = "login by json userName and password. ex: userName: samz123 password: 123")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User registered successfully!"),
			@ApiResponse(code = 409, message = "409 conflict: User address already exists"),
			@ApiResponse(code = 500, message = "500 Internal Server Error: Error occurred while registering user") })
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
		try {
			// Create new user's account
			UserEntity userEntity = new UserEntity();
			userEntity.setName(signUpRequestDTO.getName());
			userEntity.setUserName(signUpRequestDTO.getUserName());
			userEntity.setEmail(signUpRequestDTO.getEmail());
			userEntity.setPassword(encoder.encode(signUpRequestDTO.getPassword()));

			userEntity = userRepository.save(userEntity);
			return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
		} catch (DataIntegrityViolationException ex) {
			// Duplicate email address
			return ResponseEntity.status(HttpStatus.CONFLICT).body("409 conflict: User address already exists");
		} catch (Exception ex) {
			// Other errors
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("500 Internal Server Error: Error occurred while registering user");
		}
	}

	@PostMapping("/sign-in")
	@ApiOperation(value = "Login API", notes = "login by json userName and password. ex: userName: sam1 password: 123")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Login success", response = JwtResponse.class),
			@ApiResponse(code = 401, message = "Invalid username or password", response = AuthEntryErrModel.class),
			@ApiResponse(code = 404, message = "User not found") })
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
		if (userRepository.findByUserName(loginRequestDTO.getUserName()) == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found user!!");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(), loginRequestDTO.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.generateJwtToken(userDetails);

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
		AuthenticationResult authenticationResult = new AuthenticationResult(jwt, jwt, refreshToken.getToken(),
				jwtUtils.getJwtExpirationMs());
		JwtResponse jwtResponse = new JwtResponse(authenticationResult);
		return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
	}

	@PostMapping("/sign-out")
	@ApiOperation(value = "Log out API", notes = "log out by Authorization, type: Baerer token. ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "The user was successfully logged out"),
			@ApiResponse(code = 401, message = "The user is not authenticated"),
			@ApiResponse(code = 500, message = "An internal server error occurred while attempting to log out the user") })
	public ResponseEntity<?> logoutUser() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Long userId = userDetails.getId();
		refreshTokenService.deleteByUserId(userId);
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok("Log out successful!");

	}

	@PostMapping("/refresh-token")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Refresh token success", response = TokenRefreshResponse.class),
			@ApiResponse(code = 404, message = "Refresh token is not in database!") })
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();
		if (refreshTokenService.findByToken(requestRefreshToken) == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Refresh token is not in database!");

		return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String token = jwtUtils.generateTokenFromUserName(user.getUserName());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				}).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "FORBIDDEN"));
	}
}
